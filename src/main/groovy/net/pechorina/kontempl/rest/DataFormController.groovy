package net.pechorina.kontempl.rest

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord
import net.pechorina.kontempl.data.DataFormView
import net.pechorina.kontempl.data.Page
import net.pechorina.kontempl.data.Site
import net.pechorina.kontempl.service.DataFormRecordService
import net.pechorina.kontempl.service.DataFormService
import net.pechorina.kontempl.service.MailService
import net.pechorina.kontempl.service.SiteService
import net.sf.uadetector.ReadableUserAgent
import net.sf.uadetector.UserAgentStringParser
import net.sf.uadetector.service.UADetectorServiceFactory

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController
import net.sf.uadetector.UserAgentType

@RestController
@Slf4j
class DataFormController {
	
	static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	DataFormService dataFormService
	
	@Autowired
	DataFormRecordService dataFormRecordService
	
	@Autowired
	SiteService siteService
	
	@Autowired
	MailService mailService
	
	@Autowired
	Environment env
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}/forms/{formName}")
	public ResponseEntity<DataFormView> getFormByName(@PathVariable("siteName") String siteName,
			@PathVariable("formName") String formName) {
		Site s =  siteService.findByNameCached(siteName)
		DataForm f = dataFormService.getByNameAndSiteId(formName, s.id)

		DataFormView dfv = new DataFormView(name: f.name, title: f.title, formFields: f.formFields)

		return new ResponseEntity<DataFormView>(dfv, HttpStatus.OK)
	}

	@RequestMapping(method = RequestMethod.POST, value="/api/browse/sites/{siteName}/forms/{formName}/records")
	public void postFormData(@RequestBody Map rec, 
			@PathVariable("siteName") String siteName,
			@PathVariable("formName") String formName, 
			HttpServletRequest request, HttpServletResponse response) {
		Site s =  siteService.findByNameCached(siteName)
		DataForm f = dataFormService.getByNameAndSiteId(formName, s.id)
		
		def body = "\n";
		if (rec) {
			rec.each { key, value -> body += "$key: $value\n" }
		}
		
		String ua = request.getHeader("User-Agent")
		String ip = request.remoteAddr
		UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser()
		ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));
		log.debug(agent.toString())
		if (agent.getType() == UserAgentType.ROBOT) {
			log.warn("Rebot submit detected" + agent.toString());
		}
		String timestamp = new DateTime().toString(fmt);
		body += """=====
IP Address: $ip
User-Agent: $ua
"""
		
		if (f.persist) {
			def json = new JsonBuilder()
			String jsonStr = json(rec)
			DataFormRecord r = new DataFormRecord(dataForm: f, data: jsonStr, ip: ip, ua: ua, posted: new DateTime())
			dataFormRecordService.save(r)
		}
		
		String subj = s.domain + " - " + f.name;
		
		if (f.emails) {
			f.emails.tokenize(',').each { email ->
				mailService.sendSimpleEmail(env.getProperty("mailFrom"), 
					env.getProperty("mailFrom"), 
					email.stripIndent().stripMargin(), 
					subj, 
					body)
			}
		}
		
		log.debug("Data received: " + body)
		response.status = HttpServletResponse.SC_OK;
	}
}
