package net.pechorina.kontempl.view;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.data.DocFile;
import net.pechorina.kontempl.data.FileMeta;
import net.pechorina.kontempl.service.DocFileService;
import net.pechorina.kontempl.service.PageElementTypeService;
import net.pechorina.kontempl.service.PageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DocFileController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(DocFileController.class);

	@Autowired
	private PageService pageService;

	@Autowired
	private PageElementTypeService pageElementTypeService;

	@Autowired
	private DocFileService docFileService;

	@RequestMapping(value = "/files/{pageId}/upload", method = RequestMethod.POST)
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
				DocFile df = new DocFile(fm);
				df.setPageId(pageId);
				String filePath = appConfig.getProperty("fileStoragePath")
						+ df.getAbsolutePath();
				String dirPath = appConfig.getProperty("fileStoragePath")
						+ df.getDirectoryPath();
				boolean success = false;
				try {
					File f = new File(filePath);
					File d = new File(dirPath);
					if (!d.exists()) {
						logger.debug("Target directory do not exists, create new: "
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
					logger.debug("Image " + filePath + " saved: " + docFile);
				}
			}
		}
		
		pageService.resetPageCache();

		return files;
	}

	@RequestMapping(value = "/files/{fileId}/delete")
	public String delete(@PathVariable("fileId") Integer fileId) {
		DocFile df = docFileService.getDocById(fileId);
		Integer pageId = df.getPageId();
		boolean success = docFileService.deleteDoc(df);
		
		pageService.resetPageCache();
		
		if (!success) logger.warn("Image delete failed.");
		return "redirect:/page/" + pageId + "/edit";
	}
	
	@RequestMapping(value = "/files/{fileId}/asyncdelete")
	public @ResponseBody String deleteAsync(@PathVariable("fileId") Integer fileId) {
		DocFile df = docFileService.getDocById(fileId);
		boolean success = docFileService.deleteDoc(df);
		
		pageService.resetPageCache();
		
		if (!success) return "FAIL";
		return "SUCCESS";
	}
	
	@RequestMapping(value = "/files/{fileId}/move")
	public @ResponseBody String moveFile(@PathVariable("fileId") Integer fileId, 
			@RequestParam(value="dir", required=true) String direction) {
		DocFile f = docFileService.getDocById(fileId);
		docFileService.moveDoc(f, direction);
		return "OK";
	}
	
}
