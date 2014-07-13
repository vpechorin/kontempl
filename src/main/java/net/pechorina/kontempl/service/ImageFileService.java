package net.pechorina.kontempl.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.repos.ImageFileRepo;
import net.pechorina.kontempl.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Files;

@Service
public class ImageFileService {
	static final Logger logger = LoggerFactory.getLogger(ImageFileService.class);

	@Autowired
	private ImageFileRepo imageFileRepo;
	
	@Autowired
	private ImageThumbService thumbService;
	
	@Autowired
	private PageService pageService;
	
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
		Page page = pageService.getPage(im.getPageId());
		ImageFile imageFile = imageFileRepo.saveAndFlush(im);
		if (imageFile != null) {
			thumbService.asyncRegenThumbs(imageFile, page);
		}
		return imageFile;
	}
	
	@Transactional
	public ImageFile saveNew(ImageFile im) {
		logger.debug("Saving a new uploaded image: " + im.getName());
		Page page = pageService.getPage(im.getPageId());
		int sortIndex = getNextImageSortIndex(im.getPageId());
		im.setSortIndex(sortIndex);
		logger.debug("Sortindex: " + im.getSortIndex());
		
		adjustMainImageStatusForNewImage(im);
		
		ImageFile imageFile = imageFileRepo.saveAndFlush(im);
		if (imageFile != null) {
			logger.debug("Caller async thumbs creation");
			thumbService.asyncRegenThumbs(imageFile, page);
		}
		logger.debug("Image save completed: " + imageFile);
		
		return imageFile;
	}
	
	@Transactional
	public void removeAllImagesForPage(Page page) {
		List<ImageFile> images = imageFileRepo.findByPageId(page.getId());
		for(ImageFile im: images) {
			deleteImage(im);
		}
		String pageImagesDir = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + File.separator + page.getId() + File.separator + "images";
		File dir = new File(pageImagesDir);
		FileUtils.deleteDirectory(dir);
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
		logger.debug("Image found: " + im + " for page: " + pageId);
		return im;
	}
	
	@Transactional
	public boolean deleteImage(ImageFile im) {
		Page page = pageService.getPage(im.getPageId());
		String filename = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + im.getAbsolutePath();
		boolean success = FileUtils.deleteFileByName( filename );
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
			thumbService.deleteThumb(im.getId(), page);
			imageFileRepo.delete(im);
			logger.debug("imageFile DB record removed");
		}
		else {
			logger.debug("Will not remove ImageFile DB record as can't delete the file from the disk");
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
	
	@Transactional
	public ImageFile copyFileToPage(ImageFile src, Page srcPage, Page targetPage) {
		ImageFile target = src.copy();
		target.setPageId(targetPage.getId());
		ImageFile savedFile = null;
		logger.debug("Copy " + src.getName() + " file from page #" + src.getPageId() + " to page #" + targetPage.getId());
		try {
			File srcFile = new File( env.getProperty("fileStoragePath") + File.separator 
					+ srcPage.getSiteId() + src.getAbsolutePath());
			
			File targetFile = new File(env.getProperty("fileStoragePath") + File.separator 
					+ targetPage.getSiteId() + target.getAbsolutePath());
			
			String targetDir = env.getProperty("fileStoragePath") + File.separator 
					+ targetPage.getSiteId() + target.getDirectoryPath();
			
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
