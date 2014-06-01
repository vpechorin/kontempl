package net.pechorina.kontempl.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Thumbnail;
import net.pechorina.kontempl.repos.ImageFileRepo;
import net.pechorina.kontempl.repos.ThumbnailRepo;
import net.pechorina.kontempl.utils.ImageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Files;

@Service("imageFileService")
public class ImageFileService {
	static final Logger logger = LoggerFactory.getLogger(ImageFileService.class);

	@Autowired
	private ImageFileRepo imageFileRepo;
	
	@Autowired
	private ThumbnailRepo thumbRepo;
	
	@Autowired
	private Environment env;
	
	@Transactional
	public List<ImageFile> listImagesForPage(Integer pageId) {
		return imageFileRepo.findByPageId(pageId);
	}
	
	@Transactional
	public List<ImageFile> listImagesForPageOrdered(Integer pageId) {
		return imageFileRepo.getPageImagesOrdered(pageId);
	}
	
	@Transactional
	public ImageFile save(ImageFile im) {
		ImageFile imageFile = imageFileRepo.saveAndFlush(im);
		if (imageFile != null) {
			clearAndRegenThumbs(imageFile);
		}
		return imageFile;
	}
	
	@Transactional
	public void removeAllImagesForPage(Integer pageId) {
		List<ImageFile> images = imageFileRepo.findByPageId(pageId);
		for(ImageFile im: images) {
			deleteImage(im);
		}
		String pageImagesDir = env.getProperty("fileStoragePath") + File.separator + pageId + File.separator + "images";
		File dir = new File(pageImagesDir);
		deleteDirectory(dir);
	}
	
	public boolean deleteFileByName(String fileName) {
		logger.debug("Delete file: " + fileName);
		if (fileName == null) {
			logger.warn("missed filename");
			return false;
		}

		boolean success = false;
		try{
			File file = new File(fileName);

			if(file.delete()){
				logger.debug(file.getName() + " is deleted!");
				success = true;

			}else{
				logger.error(file.getName() + " - file delete operation failed.");
			}

		}catch(Exception e){
			logger.error(fileName + " - file delete operation failed: " + e);
		}

		return success;
	}

	/**
	 * Force deletion of directory
	 * @param path
	 * @return
	 */
	static public boolean deleteDirectory(File path) {
	    if (path.exists()) {
	        File[] files = path.listFiles();
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isDirectory()) {
	                deleteDirectory(files[i]);
	            } else {
	                files[i].delete();
	            }
	        }
	    }
	    return (path.delete());
	}
	
	@Transactional
	public ImageFile getImageById(Integer imageId) {
		return imageFileRepo.findOne(imageId);
	}
	
	@Transactional
	public ImageFile getMainImageForPage(Integer pageId) {
		List<ImageFile> images = imageFileRepo.locateMainImageForPage(pageId);
		ImageFile im = null;
		if (images != null && (images.size() > 0)) im = images.get(0);
		// logger.debug("Image found: " + im);
		return im;
	}
	
	@Transactional
	public boolean deleteImage(ImageFile im) {
		String filename = env.getProperty("fileStoragePath") + im.getAbsolutePath();
		boolean success = deleteFileByName( filename );
		// check if file still exists
		if (!success) {
			logger.debug("Checking if file still exists: " + filename);
			File f = new File(filename);
			if (!f.exists()) {
				logger.debug("File does not exists");
				success = true;
			}
			else {
				logger.debug("File does exists, can't remove it");
			}
		}
		
		if (success) {
			deleteThumb(im.getId());
			imageFileRepo.delete(im);
			logger.debug("imageFile DB record removed");
		}
		else {
			logger.debug("Will not remove ImageFIle DB record as can't delete the file from the disk");
		}
		
		logger.debug("deleteImage success=" + success);
		
		return success;
	}
	
	@Transactional
	public void markImageAsMainImage(ImageFile mainImage) {
		List<ImageFile> images = imageFileRepo.findByPageId(mainImage.getPageId());
		if (images != null) {
			for(ImageFile im: images) {
				if (im.getId() != mainImage.getId()) {
					im.setMainImage(false);
					imageFileRepo.saveAndFlush(im);
				}
			}
		}
		mainImage.setMainImage(true);
		imageFileRepo.saveAndFlush(mainImage);
	}
	
	@Transactional
	public int getMaxImageIndex(Integer pageId) {
		Integer idx = imageFileRepo.getMaxSortIndexForImages(pageId);
		if (idx == null) {
			idx = 0;
		}
		logger.debug("Max index: " + idx + " for page id: " + pageId);
		return idx;
	}
	
	@Transactional
	public int getNextImageSortIndex(Integer pageId) {
		Integer idx = imageFileRepo.getMaxSortIndexForImages(pageId);
		if (idx == null) {
			idx = 0;
		}
		logger.debug("Max index: " + idx + " for page id: " + pageId);
		idx++;
		logger.debug("Nextindex: " + idx + " for page id: " + pageId);
		return idx;
	}

	@Transactional
	public long countImagesForPage(Integer pageId) {
		Long n = imageFileRepo.countImagesForPage(pageId);
		if (n == null) {
			n = 0l;
		}

		logger.debug("Total images: " + n + " for page id: " + pageId);
		return n;
	}
	
	@Transactional
	public void adjustMainImageStatusForNewImage(ImageFile img) {
		Long n = imageFileRepo.countImagesForPage(img.getPageId());
		if (n == null) {
			n = 0l;
		}
		logger.debug("Total images: " + n + " for page id: " + img.getPageId());
		if (n > 0) {
			logger.debug("Not the first image, will set as not main image");
			img.setMainImage(false);
		}
		else {
			logger.debug("The first image, will set as main image");
			img.setMainImage(true);
		}
	}
	
	@Transactional
	public void moveImage(ImageFile img, String direction) {
		logger.debug("moveImage: " + img.getId() + "/" + img.getName() + " " + direction);
		Integer pageId = img.getPageId();
		List<ImageFile> images = imageFileRepo.getPageImagesOrdered(pageId);
		int idx = images.indexOf(img);
		int newIdx = 0;
		if (direction.equalsIgnoreCase("UP")) {
			// moving element up the list
			if (idx > 0) {
				newIdx = idx -1;
			}
		}
		else {
			// moving element down the list
			if (idx < (images.size() - 1)) {
				newIdx = idx + 1;
			}
			else {
				newIdx = idx;
			}
		}
		logger.debug("Old index: " + idx + " New index: " + newIdx + " Total elements: " + images.size());
		
		ImageFile element = images.remove(idx);
		images.add(newIdx, element);
		
		// renumber all elements
		int i = 1;
		for(ImageFile imageFile: images) {
			imageFile.setSortIndex(i);
			imageFileRepo.save(imageFile);
			i++;
		}
	}
	
	private void deleteThumb(Integer imageFileId) {
		Thumbnail t = thumbRepo.findByImageFileId(imageFileId);
		if (t != null) {
			deleteThumb(t);
		}
	}
	
	private void deleteThumb(Thumbnail t) {
		String thumbFile = env.getProperty("fileStoragePath") + t.getAbsolutePath();
		boolean fileDeleted = deleteFileByName( thumbFile );
		if (!fileDeleted) {
			File f = new File(thumbFile);
			if (!f.exists()) {
				logger.debug("File does not exists");
				fileDeleted = true;
			}
		}
		if (fileDeleted) 	thumbRepo.delete(t);
	}
	
	private void clearAndRegenThumbs(ImageFile imageFile) {
		deleteThumb(imageFile.getId());
		
		Thumbnail newThumb = new Thumbnail(imageFile);
		boolean thumbCreated = makeThumbnail(imageFile, newThumb);
		if (thumbCreated) {
			newThumb = thumbRepo.saveAndFlush(newThumb);
			imageFile.setThumb(newThumb);
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
	
	@Transactional
	public ImageFile copyFileToPage(ImageFile src, Integer pageId) {
		ImageFile target = src.copy();
		target.setPageId(pageId);
		ImageFile savedFile = null;
		logger.debug("Copy " + src.getName() + " file from page #" + src.getPageId() + " to page #" + pageId);
		try {
			File srcFile = new File(env.getProperty("fileStoragePath") + src.getAbsolutePath());
			File targetFile = new File(env.getProperty("fileStoragePath") + target.getAbsolutePath());
			String targetDir = env.getProperty("fileStoragePath") + target.getDirectoryPath();
			File d = new File(targetDir);
			if (!d.exists()) {
				logger.debug("new file directory does not exists, create new: " + targetDir);
				Files.createParentDirs(targetFile);
			}
			logger.debug("Src file: " + srcFile);
			logger.debug("Trg file: " + targetFile);
			Files.copy(srcFile, targetFile);
			logger.debug("Copy op completed");
			savedFile = save(target);
			logger.debug("New file saved: " + savedFile);
		} catch(IOException e) {
			logger.error("Error! Can't copy file: " + e);
		}
		
		return savedFile;
	}
}
