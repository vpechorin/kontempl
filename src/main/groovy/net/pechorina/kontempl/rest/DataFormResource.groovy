package net.pechorina.kontempl.rest

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import groovy.util.logging.Slf4j;
import net.pechorina.kontempl.data.DataForm;
import net.pechorina.kontempl.service.DataFormRecordService
import net.pechorina.kontempl.service.DataFormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/api/dataforms")
@Slf4j
class DataFormResource {
	
	@Autowired
	DataFormService dataFormService
	
	@Autowired
	Environment env
	
	@RequestMapping(method=RequestMethod.GET)
	List<DataForm> list() {
		return dataFormService.findAll()
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	DataForm getById(@PathVariable("id") Integer id) {
		return dataFormService.get(id)
	}
	
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<DataForm> create(@RequestBody DataForm f, HttpServletRequest request) {
		DataForm savedEntity = dataFormService.save(f)
		log.info("DATAFORM CREATED: " + savedEntity + " Src:" + request.getRemoteAddr())
		return new ResponseEntity<DataForm>(savedEntity, HttpStatus.CREATED)
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	void save(@PathVariable("id") Integer id, @RequestBody DataForm f, HttpServletRequest request, HttpServletResponse response) {
		DataForm entity = dataFormService.get(id);
		
		// merge data
		entity.with {
			emails = f.emails
			name = f.name
			title = f.title
			siteId = f.siteId
			persist = f.persist
			formFields = f.formFields
		}
		
		DataForm savedEntity = dataFormService.save(entity)
		log.info("DATAFORM UPDATED: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
		dataFormService.remove(id);
		log.info("DATAFORM DELETED: " + id + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
