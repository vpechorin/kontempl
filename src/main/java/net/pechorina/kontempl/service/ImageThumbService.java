package net.pechorina.kontempl.service;

import com.google.common.io.Files;
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageThumbService {
	static final Logger logger = LoggerFactory.getLogger(ImageThumbService.class);

	@Autowired
	private ImageFileRepo imageFileRepo;
	
	@Autowired
	private ThumbnailRepo thumbRepo;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private Environment env;
	
	@Transactional
	public void deleteThumb(Integer imageFileId, Page page) {
		logger.debug("deleteThumb: imageId=" + imageFileId);
		Thumbnail t = thumbRepo.findByImageFileId(imageFileId);
		if (t != null) {
			deleteThumb(t, page);
		}
	}
	
	@Transactional
	public void deleteThumb(Thumbnail t, Page page) {
		String thumbFile = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + t.getAbsolutePath();
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
	public void asyncRegenThumbs(ImageFile imageFile, Page p) {
		logger.debug("asyncRegenThumbs for " + imageFile);
		clearAndRegenThumbs(imageFile, p);
	}
	
	@Transactional
	public void clearAndRegenThumbs(ImageFile imageFile, Page page) {
		logger.debug("clearAndRegenThumbs: " + imageFile.getName());
		deleteThumb(imageFile.getId(), page);
		logger.debug("Ready to create a new thumb for image: " + imageFile.getName());
		Thumbnail newThumb = new Thumbnail();
		newThumb.setPropertiesFromImageFile(imageFile);
		boolean thumbCreated = makeThumbnail(imageFile, newThumb, page);
		logger.debug("thumpCreated=" + thumbCreated);
		if (thumbCreated) {
			newThumb = thumbRepo.saveAndFlush(newThumb);
			imageFile.setThumb(newThumb);
			logger.debug("ImageThumb created: " + newThumb);
		}
	}
	
	public boolean makeThumbnail(ImageFile imageFile, Thumbnail thumb, Page page) {
		
		String imgFilename = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + File.separator + imageFile.getAbsolutePath();
		
		String thumbFilename = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + File.separator + thumb.getAbsolutePath();
		
		String thumbDir = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + File.separator + thumb.getDirectoryPath();
		
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
