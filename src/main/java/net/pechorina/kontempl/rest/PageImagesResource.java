package net.pechorina.kontempl.rest;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.data.FileMeta;
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.service.ImageFileService;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.SiteService;
import net.pechorina.kontempl.utils.ImageUtils;
import net.pechorina.kontempl.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.io.Files;

@RestController
@RequestMapping(value = "/api/pages/{pageId}/images")
public class PageImagesResource {
	static final Logger logger = LoggerFactory.getLogger(PageImagesResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private ImageFileService imgService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<ImageFile> list(@PathVariable("pageId") Integer pageId) {
		List<ImageFile> list = imgService.listImagesForPageOrdered(pageId); 
		return list;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ImageFile getById(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id) {
		return imgService.getImageById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/setmain")
	public void setAsMainImage(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, HttpServletResponse response) {
		ImageFile im = imgService.getImageById(id);
		imgService.markImageAsMainImage(im);
		
		pageService.resetPageCache();
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/moveup")
	public void moveImageUp(@PathVariable("pageId") Integer pageId,@PathVariable("id") Integer id, HttpServletResponse response) {
		ImageFile im = imgService.getImageById(id);
		imgService.moveImage(im, "up");
		
		pageService.resetPageCache();
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/movedown")
	public void moveImageDown(@PathVariable("pageId") Integer pageId,@PathVariable("id") Integer id, HttpServletResponse response) {
		ImageFile im = imgService.getImageById(id);
		imgService.moveImage(im, "down");
		
		pageService.resetPageCache();
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	LinkedList<FileMeta> upload(@PathVariable("pageId") Integer pageId,
			MultipartHttpServletRequest request, HttpServletResponse response) {
		
		String requestId = UUID.randomUUID().toString();
		
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();

		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {
			mpf = request.getFile(itr.next());
			logger.debug( mpf.getOriginalFilename() + " uploaded! " + files.size());

			FileMeta fileMeta = new FileMeta();
			fileMeta.setFileName(StringUtils.prettifyFilename( mpf.getOriginalFilename().toLowerCase() ) );
			fileMeta.setFileSize(mpf.getSize());
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes( mpf.getBytes() );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			files.add(fileMeta);
			logger.debug(requestId + " file uploaded: " + fileMeta);
		}
		
		logger.debug(requestId + " files uploaded: " + files.size());

		if (files.size() > 0) {
			for (FileMeta fm : files) {
				logger.debug(requestId + " Processing: " + fm.getFileName() + " -- " + fm.getFileType());
				ImageFile im = new ImageFile(fm);
				im.setPageId(pageId);
				String filePath = env.getProperty("fileStoragePath") + im.getAbsolutePath();
				logger.debug(requestId + " filePath: " + filePath);
				String dirPath = env.getProperty("fileStoragePath") + im.getDirectoryPath();
				logger.debug(requestId + " dirPath: " + dirPath);
				
				boolean success = false;
				try {
					File f = new File(filePath);
					File d = new File(dirPath);
					if (!d.exists()) {
						logger.debug("Image directory do not exists, create new: " + dirPath);
						Files.createParentDirs(f);
					}
					Files.write(fm.getBytes(), f);
					Dimension dim = ImageUtils.getImageDimension(f);
					if (dim != null) {
						im.setWidth(dim.width);
						im.setHeight(dim.height);
					}
					success = true;
				} catch (IOException e) {
					logger.error("Can't write image to file: " + e);
					success = false;
				}
				if (success) {
					ImageFile i = imgService.saveNew(im);
					logger.debug(requestId + " Image " + filePath + " saved: " + i);
				}
			}
		}
		
		pageService.resetPageCache();

		return files;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void save(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, 
			@RequestBody ImageFile im, HttpServletRequest request, HttpServletResponse response) {
		ImageFile existingEntity = imgService.getImageById(id);
		
		// merge data
		existingEntity.setName(im.getName());
		existingEntity.setMainImage(im.isMainImage());
		existingEntity.setSortIndex(im.getSortIndex());
		
		ImageFile savedEntity = imgService.save(existingEntity);
		
		pageService.resetPageCache();
		
		logger.info("IMAGE UPDATE/SAVE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, 
			HttpServletRequest request, HttpServletResponse response) {
		ImageFile im = imgService.getImageById(id);
		imgService.deleteImage(im);
		
		pageService.resetPageCache();
		
		logger.info("IMAGE DELETE: " + im + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
