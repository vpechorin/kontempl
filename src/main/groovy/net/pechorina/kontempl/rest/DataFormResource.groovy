package net.pechorina.kontempl.rest

import groovy.util.logging.Slf4j
import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.service.DataFormService
import net.pechorina.kontempl.service.SiteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = "/api/dataforms")
@Slf4j
class DataFormResource {

    @Autowired
    DataFormService dataFormService

    @Autowired
    private SiteService siteService

    @Autowired
    Environment env

    @GetMapping
    List<DataForm> list(@RequestParam(value = "siteId", required = false) Integer siteId) {
        if (siteId) {
            return dataFormService.findBySiteId(siteId)
        }
        return dataFormService.findAll()
    }

    @GetMapping("/{id}")
    DataForm getById(@PathVariable("id") Integer id) {
        return dataFormService.get(id)
    }

    @PostMapping
    ResponseEntity<DataForm> create(@RequestBody DataForm f, HttpServletRequest request) {
        DataForm savedEntity = dataFormService.save(f)
        log.info("DATAFORM CREATED: " + savedEntity + " Src:" + request.getRemoteAddr())
        return new ResponseEntity<DataForm>(savedEntity, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    void save(@PathVariable("id") Integer id,
              @RequestBody DataForm f,
              HttpServletRequest request, HttpServletResponse response) {
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

    @DeleteMapping("/{id}")
    void remove(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        dataFormService.remove(id);
        log.info("DATAFORM DELETED: " + id + " Src:" + request.getRemoteAddr());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
