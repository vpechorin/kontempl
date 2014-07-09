package net.pechorina.kontempl.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Thumbnail;
import net.pechorina.kontempl.repos.ImageFileRepo;
import net.pechorina.kontempl.repos.ThumbnailRepo;
import net.pechorina.kontempl.utils.FileUtils;
import net.pechorina.kontempl.utils.ImageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Files;

@Service
public class ImageThumbService {
	static final Logger logger = LoggerFactory.getLogger(ImageThumbService.class);

	@Autowired
	private ImageFileRepo imageFileRepo;
	
	@Autowired
	private ThumbnailRepo thumbRepo;
	
	@Autowired
	private Environment env;
	
	@Transactional
	public void deleteThumb(Integer imageFileId) {
		logger.debug("deleteThumb: imageId=" + imageFileId);
		Thumbnail t = thumbRepo.findByImageFileId(imageFileId);
		if (t != null) {
			deleteThumb(t);
		}
	}
	
	@Transactional
	public void deleteThumb(Thumbnail t) {
		String thumbFile = env.getProperty("fileStoragePath") + t.getAbsolutePath();
		boolean fileDeleted = FileUtils.deleteFileByName( thumbFile );
		if (!fileDeleted) {
			File f = new File(thumbFile);
			if (!f.exists()) {
				logger.debug("File does not exists");
				fileDeleted = true;
			}
		}
		if (fileDeleted) thumbRepo.delete(t);
	}
	
	@Async("KtExecutor")
	@Transactional
	public void asyncRegenThumbs(ImageFile imageFile) {
		logger.debug("asyncRegenThumbs for " + imageFile);
		clearAndRegenThumbs(imageFile);
	}
	
	@Transactional
	public void clearAndRegenThumbs(ImageFile imageFile) {
		logger.debug("clearAndRegenThumbs: " + imageFile.getName());
		deleteThumb(imageFile.getId());
		logger.debug("Ready to create a new thumb for image: " + imageFile.getName());
		Thumbnail newThumb = new Thumbnail(imageFile);
		boolean thumbCreated = makeThumbnail(imageFile, newThumb);
		logger.debug("thumpCreated=" + thumbCreated);
		if (thumbCreated) {
			newThumb = thumbRepo.saveAndFlush(newThumb);
			imageFile.setThumb(newThumb);
			logger.debug("ImageThumb created: " + newThumb);
		}
	}
	
	public boolean makeThumbnail(ImageFile imageFile, Thumbnail thumb) {
		String imgFilename = env.getProperty("fileStoragePath") + imageFile.getAbsolutePath();
		String thumbFilename = env.getProperty("fileStoragePath") + thumb.getAbsolutePath();
		String thumbDir = env.getProperty("fileStoragePath") + thumb.getDirectoryPath();
		
		BufferedImage scaledImage = null;
		try {
			File file = new File(imgFilename);
			BufferedImage in = ImageIO.read( file );
			scaledImage = ImageUtils.createThumbnail(in, Integer.parseInt( env.getProperty("thumbSize") ));
			in.flush();
		} catch (IOException e) {
			logger.error("Cant read image: " + imgFilename + " exception: " + e);
		}
		
		boolean success = false;
		if (scaledImage != null) {
			thumb.setHeight(scaledImage.getHeight());
			thumb.setWidth(scaledImage.getWidth());
			
			try {
				File outputFile = new File(thumbFilename);

				File d = new File(thumbDir);
				if (!d.exists()) {
					logger.debug("Thumb directory does not exists, create new: " + thumbDir);
					Files.createParentDirs(outputFile);
				}
				ImageIO.write(scaledImage, env.getProperty("thumbFormat"), outputFile);
				success = true;
			} catch (IOException e) {
				logger.error("Cant save image: " + thumbFilename + " exception: " + e);
				success = false;
			}
			finally {
				scaledImage.flush();
			}
		}

		return success;
	}

}
