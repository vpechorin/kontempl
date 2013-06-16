package net.pechorina.kontempl.view;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.data.FileMeta;
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.service.ImageFileService;
import net.pechorina.kontempl.service.PageElementTypeService;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.utils.ImageUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.io.Files;

@Controller
public class ImageController extends AbstractController {
	private static final Logger logger = Logger
			.getLogger(ImageController.class);

	@Autowired
	private PageService pageService;

	@Autowired
	private PageElementTypeService pageElementTypeService;

	@Autowired
	private ImageFileService imageFileService;

	@RequestMapping(value = "/image/{pageId}/upload", method = RequestMethod.POST)
	public @ResponseBody
	LinkedList<FileMeta> upload(@PathVariable("pageId") Integer pageId,
			MultipartHttpServletRequest request, HttpServletResponse response) {

		LinkedList<FileMeta> files = new LinkedList<FileMeta>();

		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {
			mpf = request.getFile(itr.next());
			System.out.println(mpf.getOriginalFilename() + " uploaded! "
					+ files.size());

			FileMeta fileMeta = new FileMeta();
			fileMeta.setFileName(mpf.getOriginalFilename().toLowerCase());
			fileMeta.setFileSize(mpf.getSize());
			fileMeta.setFileType(mpf.getContentType());

			try {
				fileMeta.setBytes(mpf.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			files.add(fileMeta);
		}

		if (files.size() > 0) {
			for (FileMeta fm : files) {
				ImageFile im = new ImageFile(fm);
				im.setPageId(pageId);
				String filePath = appConfig.getProperty("fileStoragePath")
						+ im.getAbsolutePath();
				String dirPath = appConfig.getProperty("fileStoragePath")
						+ im.getDirectoryPath();
				boolean success = false;
				try {
					File f = new File(filePath);
					File d = new File(dirPath);
					if (!d.exists()) {
						logger.debug("Image directory do not exists, create new: "
								+ dirPath);
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
					int sortIndex = imageFileService.getNextImageSortIndex(pageId);
					im.setSortIndex(sortIndex);
					imageFileService.adjustMainImageStatusForNewImage(im);
					ImageFile i = imageFileService.save(im);
					logger.debug("Image " + filePath + " saved: " + i);
				}
			}
		}
		
		pageService.resetPageCache();

		return files;
	}

	@RequestMapping(value = "/images/{imageId}/delete")
	public String delete(@PathVariable("imageId") Integer imageId) {
		ImageFile im = imageFileService.getImageById(imageId);
		Integer pageId = im.getPageId();
		boolean success = imageFileService.deleteImage(im);
		
		pageService.resetPageCache();
		
		if (!success) logger.warn("Image delete failed.");
		return "redirect:/page/" + pageId + "/edit";
	}
	
	@RequestMapping(value = "/images/{imageId}/asyncdelete")
	public @ResponseBody String deleteAsync(@PathVariable("imageId") Integer imageId) {
		ImageFile im = imageFileService.getImageById(imageId);
		boolean success = imageFileService.deleteImage(im);
		
		pageService.resetPageCache();
		
		if (!success) return "FAIL";
		return "SUCCESS";
	}
	
	@RequestMapping(value = "/images/{imageId}/setMain")
	public String markAsMainImage(@PathVariable("imageId") Integer imageId) {
		ImageFile im = imageFileService.getImageById(imageId);
		Integer pageId = im.getPageId();
		imageFileService.markImageAsMainImage(im);
		
		pageService.resetPageCache();
		
		return "redirect:/page/" + pageId + "/edit";
	}
	
	@RequestMapping(value = "/images/{imageId}/setMainAsync")
	public @ResponseBody String markAsMainImageAsync(@PathVariable("imageId") Integer imageId) {
		ImageFile im = imageFileService.getImageById(imageId);
		imageFileService.markImageAsMainImage(im);
		
		pageService.resetPageCache();
		
		return "OK";
	}
	
	@RequestMapping(value = "/images/{imageId}/move")
	public @ResponseBody String moveImage(@PathVariable("imageId") Integer imageId, @RequestParam(value="dir", required=true) String direction) {
		ImageFile im = imageFileService.getImageById(imageId);
		imageFileService.moveImage(im, direction);
		return "OK";
	}
	
}
