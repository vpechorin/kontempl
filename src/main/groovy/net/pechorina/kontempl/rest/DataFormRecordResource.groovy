package net.pechorina.kontempl.rest

import java.util.List

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import groovy.util.logging.Slf4j
import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord
import net.pechorina.kontempl.service.DataFormRecordService
import net.pechorina.kontempl.service.DataFormService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/api/dataformrecords")
@Slf4j
class DataFormRecordResource {
	
	@Autowired
	DataFormRecordService dataFormRecordService
	
	@Autowired
	Environment env
	
	@RequestMapping(method=RequestMethod.GET)
	List<DataFormRecord> list(@RequestParam(value="formId", required=false) String formId, @RequestParam(value="formName", required=false) String formName) {
		if (formId) {
			return dataFormRecordService.findByFormId(formId)
		}
		if (formId) {
			return dataFormRecordService.findByFormName(formName)
		}
		return dataFormRecordService.findAll()
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	DataFormRecord getById(@PathVariable("id") String id) {
		return dataFormRecordService.get(id)
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<DataFormRecord> create(@RequestBody DataFormRecord r, HttpServletRequest request) {
		DataFormRecord savedEntity = dataFormRecordService.save(r)
		log.info("DATAFORM RECORD CREATED: " + savedEntity + " Src:" + request.getRemoteAddr())
		return new ResponseEntity<DataFormRecord>(savedEntity, HttpStatus.CREATED)
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	void save(@PathVariable("id") String id, @RequestBody DataFormRecord r, HttpServletRequest request, HttpServletResponse response) {
		DataFormRecord entity = dataFormRecordService.get(id);
		
		// merge data
		entity.with {
			data = r.data
		}
		
		DataFormRecord savedEntity = dataFormRecordService.save(entity)
		log.info("DATAFORM RECORD UPDATED: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		dataFormRecordService.remove(id);
		log.info("DATAFORM RECORD DELETED: " + id + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
