package net.pechorina.kontempl.rest;

import com.google.common.io.Files;
import net.pechorina.kontempl.data.EmbedImage;
import net.pechorina.kontempl.data.FileMeta;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.exceptions.BadImageException;
import net.pechorina.kontempl.service.ImageFileService;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.SiteService;
import net.pechorina.kontempl.utils.FileUtils;
import net.pechorina.kontempl.utils.ImageUtils;
import net.pechorina.kontempl.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/pages/{pageId}/embed")
public class EmbedImagesResource {
	static final Logger logger = LoggerFactory.getLogger(EmbedImagesResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private ImageFileService imgService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<EmbedImage> list(@PathVariable("pageId") Integer pageId) {
		Page p = pageService.getPage(pageId);
        return p.getEmbedImages().stream().collect(Collectors.toList());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public EmbedImage getById(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id) {
		Page p = pageService.getPage(pageId);
        return p.getEmbedImages().stream().filter(em -> em.getId() == id.intValue()).findFirst().orElse(null);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> upload(@PathVariable("pageId") Integer pageId,
			MultipartHttpServletRequest request, HttpServletResponse response) {
		
		Page page = pageService.getPage(pageId);
		
		String requestId = UUID.randomUUID().toString();
		
		LinkedList<FileMeta> files = new LinkedList<>();

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
				EmbedImage emb = new EmbedImage();
				emb.setName(fm.getFileName());
				emb.setContentType(fm.getFileType());
				emb.setFileSize(fm.getFileSize());
				emb.setPage(page);
				
				String filePath = env.getProperty("fileStoragePath") + File.separator + page.getSiteId() + emb.getAbsolutePath();
				logger.debug(requestId + " filePath: " + filePath);
				
				String dirPath = env.getProperty("fileStoragePath") + File.separator + page.getSiteId() + emb.getDirectoryPath();
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
					
					// check if TIFF was uploaded
					String ext = ImageUtils.getImageExtension(f);
					if (ext != null) {
						if (ext.equalsIgnoreCase("tif") || (ext.equalsIgnoreCase("tiff"))) {
							// convert to jpeg
							String newImageName = ImageUtils.convertImageToJpeg(f, ext, dirPath);
							FileUtils.deleteFileByName(filePath);
							f = new File(dirPath + File.separator + newImageName);
							emb.setName(newImageName);
							emb.setContentType("image/jpeg");
							emb.setFileSize(f.length());
						}
					}
					
					Dimension dim = ImageUtils.getImageDimension(f);
					if (dim != null) {
						emb.setWidth(dim.width);
						emb.setHeight(dim.height);
					}
					success = true;
				} catch (IOException e) {
					logger.error("Can't write image to file: " + e);
					success = false;
				} catch (BadImageException e) {
					logger.error("Bad image: " + e);
					success = false;
				}
				if (success) {
					page.addEmbedImage(emb);
					pageService.savePage(page);
					logger.debug(requestId + " Embedded image " + filePath + " saved");
				}
			}
		}

		return files;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, 
			HttpServletRequest request, HttpServletResponse response) {
		Page page = pageService.getPage(pageId);
		EmbedImage emb = page.getEmbedImages().stream().filter(em -> em.getId() == id.intValue()).findFirst().orElse(null);
		boolean fileRemoved = false;
		if (emb != null) {
			String filename = env.getProperty("fileStoragePath") + File.separator + page.getSiteId() + emb.getAbsolutePath();	
			fileRemoved = FileUtils.deleteFileByName( filename );
		}
		
		if (fileRemoved) {
			page.getEmbedImages().removeIf(im -> im.getId() == id.intValue());
			pageService.savePage(page);
		}
		
		logger.info("EMBED IMAGE DELETED: " + id + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
