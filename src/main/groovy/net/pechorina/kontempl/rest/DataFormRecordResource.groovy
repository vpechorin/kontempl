package net.pechorina.kontempl.rest

import java.util.List

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import groovy.util.logging.Slf4j
import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord
import net.pechorina.kontempl.data.ResultPage
import net.pechorina.kontempl.service.DataFormRecordService
import net.pechorina.kontempl.service.DataFormService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/api/dataforms/{id}/records")
@Slf4j
class DataFormRecordResource {
	
	@Inject
	DataFormRecordService dataFormRecordService
	
	@Inject
	DataFormService dataFormService
	
	@Inject
	Environment env
	
	@RequestMapping(method = RequestMethod.GET)
	public  ResponseEntity<ResultPage<DataFormRecord>> queryRecords(
			@PathVariable("id") Integer formId, Pageable pageable) {
		Page<DataFormRecord> p = dataFormRecordService.findByFormId(formId, pageable);
		ResultPage<DataFormRecord> result = new ResultPage<>(p);

		return new ResponseEntity<ResultPage<DataFormRecord>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{recordId}")
	DataFormRecord getById(@PathVariable("id") Integer formId, @PathVariable("recordId") Integer recordId) {
		return dataFormRecordService.get(recordId)
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<DataFormRecord> create(@PathVariable("id") Integer formId, 
		@RequestBody DataFormRecord r, HttpServletRequest request) {
		
		DataForm f = dataFormService.get(formId)
		r.dataForm = f
		DataFormRecord savedEntity = dataFormRecordService.save(r)
		log.info("DATAFORM RECORD CREATED: " + savedEntity + " Src:" + request.getRemoteAddr())
		return new ResponseEntity<DataFormRecord>(savedEntity, HttpStatus.CREATED)
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{recordId}")
	void save(@PathVariable("id") Integer formId, @PathVariable("id") Integer recordId, 
		@RequestBody DataFormRecord r, HttpServletRequest request, HttpServletResponse response) {
		
		DataFormRecord entity = dataFormRecordService.get(recordId);
		
		// merge data
		entity.with {
			data = r.data
		}
		
		DataFormRecord savedEntity = dataFormRecordService.save(entity)
		log.info("DATAFORM RECORD UPDATED: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
		
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{recordId}")
	public void remove(@PathVariable("id") Integer formId, @PathVariable("recordId") Integer recordId, 
		HttpServletRequest request, HttpServletResponse response) {
		
		dataFormRecordService.remove(recordId);
		log.info("DATAFORM RECORD DELETED: " + recordId + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
