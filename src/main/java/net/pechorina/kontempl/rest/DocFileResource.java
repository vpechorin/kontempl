package net.pechorina.kontempl.rest;

import com.google.common.io.Files;
import net.pechorina.kontempl.data.DocFile;
import net.pechorina.kontempl.data.FileMeta;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.service.DocFileService;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.SiteService;
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
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/pages/{pageId}/files")
public class DocFileResource {
	static final Logger logger = LoggerFactory.getLogger(DocFileResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private DocFileService docFileService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<DocFile> list(@PathVariable("pageId") Integer pageId) {
        return docFileService.listDocsForPageOrdered(pageId);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public DocFile getById(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id) {
		return docFileService.getDocById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/moveup")
	public void moveFileUp(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, HttpServletResponse response) {
		DocFile d = docFileService.getDocById(id);
		docFileService.moveDoc(d, "up");
		pageService.resetPageCache();
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/movedown")
	public void moveFileDown(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, HttpServletResponse response) {
		DocFile d = docFileService.getDocById(id);
		docFileService.moveDoc(d, "down");
		pageService.resetPageCache();
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	LinkedList<FileMeta> upload(@PathVariable("pageId") Integer pageId,
			MultipartHttpServletRequest request, HttpServletResponse response) {
		
		Page page = pageService.getPage(pageId);
		
		LinkedList<FileMeta> files = new LinkedList<>();

		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		while (itr.hasNext()) {
			mpf = request.getFile(itr.next());
			System.out.println(mpf.getOriginalFilename() + " uploaded! "
					+ files.size());

			FileMeta fileMeta = new FileMeta();
			fileMeta.setFileName(StringUtils.prettifyFilename( mpf.getOriginalFilename().toLowerCase() ) );
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
				DocFile df = new DocFile();
				df.setPropertiesFromFileMeta(fm);
				df.setPageId(pageId);
				
				String filePath = env.getProperty("fileStoragePath") + File.separator 
						+ page.getSiteId() + df.getAbsolutePath();
				
				String dirPath = env.getProperty("fileStoragePath") + File.separator 
						+ page.getSiteId() + df.getDirectoryPath();
				
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
					success = true;
				} catch (IOException e) {
					logger.error("Can't write doc to file: " + e);
					success = false;
				}
				if (success) {
					int sortIndex = docFileService.getNextDocSortIndex(pageId);
					df.setSortIndex(sortIndex);
					DocFile docFile = docFileService.save(df);
					pageService.resetPageCache();
					logger.debug("Document " + filePath + " saved: " + docFile);
				}
			}
		}

		return files;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void save(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, 
			@RequestBody DocFile df, HttpServletRequest request, HttpServletResponse response) {

		DocFile existingEntity = docFileService.getDocById(id);
		
		// merge data
		existingEntity.setTitle(df.getTitle());
		existingEntity.setName(df.getName());
		existingEntity.setSortIndex(df.getSortIndex());
		
		DocFile savedEntity = docFileService.save(existingEntity);
		pageService.resetPageCache();
		logger.info("DOCFILE UPDATE/SAVE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id, 
			HttpServletRequest request, HttpServletResponse response) {
		DocFile existingEntity = docFileService.getDocById(id);
		docFileService.deleteDoc(existingEntity);
		pageService.resetPageCache();
		logger.info("FILE DELETE: " + existingEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
