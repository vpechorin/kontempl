package net.pechorina.kontempl.rest

import groovy.util.logging.Slf4j
import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord
import net.pechorina.kontempl.data.ResultPage
import net.pechorina.kontempl.service.DataFormRecordService
import net.pechorina.kontempl.service.DataFormService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = "/api/dataforms/{id}/records")
@Slf4j
class DataFormRecordResource {

    @Autowired
    DataFormRecordService dataFormRecordService

    @Autowired
    DataFormService dataFormService

    @Autowired
    Environment env

    @GetMapping
    ResponseEntity<ResultPage<DataFormRecord>> queryRecords(
            @PathVariable("id") Integer formId, Pageable pageable) {
        Page<DataFormRecord> p = dataFormRecordService.findByFormId(formId, pageable);
        ResultPage<DataFormRecord> result = new ResultPage<>(p);

        return new ResponseEntity<ResultPage<DataFormRecord>>(result, HttpStatus.OK);
    }

    @GetMapping("/{recordId}")
    DataFormRecord getById(@PathVariable("id") Integer formId, @PathVariable("recordId") Integer recordId) {
        return dataFormRecordService.get(recordId)
    }

    @PostMapping
    ResponseEntity<DataFormRecord> create(@PathVariable("id") Integer formId,
                                          @RequestBody DataFormRecord r, HttpServletRequest request) {

        DataForm f = dataFormService.get(formId)
        r.dataForm = f
        DataFormRecord savedEntity = dataFormRecordService.save(r)
        log.info("DATAFORM RECORD CREATED: " + savedEntity + " Src:" + request.getRemoteAddr())
        return new ResponseEntity<DataFormRecord>(savedEntity, HttpStatus.CREATED)

    }

    @PutMapping("/{recordId}")
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

    @DeleteMapping("/{recordId}")
    void remove(@PathVariable("id") Integer formId, @PathVariable("recordId") Integer recordId,
                HttpServletRequest request, HttpServletResponse response) {

        dataFormRecordService.remove(recordId);
        log.info("DATAFORM RECORD DELETED: " + recordId + " Src:" + request.getRemoteAddr());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
