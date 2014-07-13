package net.pechorina.kontempl.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.pechorina.kontempl.data.DocFile;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.repos.DocFileRepo;
import net.pechorina.kontempl.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Files;

@Service
public class DocFileService {
	static final Logger logger = LoggerFactory.getLogger(DocFileService.class);

	@Autowired
	private DocFileRepo docFileRepo;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PageService pageService;
	
	@Transactional
	public List<DocFile> listDocsForPage(Integer pageId) {
		return docFileRepo.findByPageId(pageId);
	}
	
	@Transactional
	public List<DocFile> listDocsForPageOrdered(Integer pageId) {
		return docFileRepo.getPageDocsOrdered(pageId);
	}
	
	@Transactional
	public DocFile save(DocFile im) {
		DocFile docFile = docFileRepo.saveAndFlush(im);

		return docFile;
	}
	
	@Transactional
	public void removeAllDocsForPage(Page page) {
		List<DocFile> docs = docFileRepo.findByPageId(page.getId());
		for(DocFile d: docs) {
			deleteDoc(d);
		}
		String pageDocsDir = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + File.separator + page.getId() + File.separator + "docs";
		File dir = new File(pageDocsDir);
		FileUtils.deleteDirectory(dir);
	}
	
	@Transactional
	public DocFile getDocById(Integer id) {
		return docFileRepo.findOne(id);
	}
	
	@Transactional
	public boolean deleteDoc(DocFile d) {
		Page page = pageService.getPage(d.getPageId());
		String filename = env.getProperty("fileStoragePath") + File.separator 
				+ page.getSiteId() + d.getAbsolutePath();
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
			docFileRepo.delete(d);
			logger.debug("docFile DB record removed");
		}
		else {
			logger.debug("Will not remove DocFile DB record as can't delete the file from the disk");
		}
		
		logger.debug("deleteImage success=" + success);
		
		return success;
	}
	
	@Transactional
	public int getMaxDocIndex(Integer pageId) {
		Integer idx = docFileRepo.getMaxSortIndex(pageId);
		if (idx == null) {
			idx = 0;
		}
		logger.debug("Max index: " + idx + " for page id: " + pageId);
		return idx;
	}
	
	@Transactional
	public int getNextDocSortIndex(Integer pageId) {
		Integer idx = docFileRepo.getMaxSortIndex(pageId);
		if (idx == null) {
			idx = 0;
		}
		logger.debug("Max index: " + idx + " for page id: " + pageId);
		idx++;
		logger.debug("Nextindex: " + idx + " for page id: " + pageId);
		return idx;
	}

	@Transactional
	public long countDocsForPage(Integer pageId) {
		Long n = docFileRepo.countDocsForPage(pageId);
		if (n == null) {
			n = 0l;
		}

		logger.debug("Total images: " + n + " for page id: " + pageId);
		return n;
	}

	
	@Transactional
	public void moveDoc(DocFile df, String direction) {
		logger.debug("moveDoc: " + df.getId() + "/" + df.getName() + " " + direction);
		Integer pageId = df.getPageId();
		List<DocFile> files = docFileRepo.getPageDocsOrdered(pageId);
		int idx = files.indexOf(df);
		int newIdx = 0;
		if (direction.equalsIgnoreCase("UP")) {
			// moving element up the list
			if (idx > 0) {
				newIdx = idx -1;
			}
		}
		else {
			// moving element down the list
			if (idx < (files.size() - 1)) {
				newIdx = idx + 1;
			}
			else {
				newIdx = idx;
			}
		}
		logger.debug("Old index: " + idx + " New index: " + newIdx + " Total elements: " + files.size());
		
		DocFile element = files.remove(idx);
		files.add(newIdx, element);
		
		// renumber all elements
		int i = 1;
		for(DocFile docFile: files) {
			docFile.setSortIndex(i);
			docFileRepo.save(docFile);
			i++;
		}
	}
	
	@Transactional
	public DocFile copyFileToPage(DocFile src,  Page srcPage, Page targetPage) {
		DocFile target = src.copy();
		target.setPageId(targetPage.getId());
		DocFile savedFile = null;
		logger.debug("Copy " + src.getName() + " file from page #" + srcPage.getId() + " to page #" + targetPage.getId());
		try {
			File srcFile = new File(env.getProperty("fileStoragePath") + File.separator 
					+ srcPage.getSiteId() + src.getAbsolutePath());
			
			File targetFile = new File(env.getProperty("fileStoragePath") + File.separator 
					+ srcPage.getSiteId() + target.getAbsolutePath());
			
			String targetDir = env.getProperty("fileStoragePath") + File.separator 
					+ srcPage.getSiteId() + target.getDirectoryPath();
			
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
