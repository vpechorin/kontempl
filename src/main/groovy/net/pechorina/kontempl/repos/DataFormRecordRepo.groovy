package net.pechorina.kontempl.repos

import net.pechorina.kontempl.data.DataFormRecord
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface DataFormRecordRepo extends JpaRepository<DataFormRecord, Integer> {

    List<DataFormRecord> findByFormIdOrderByPostedDesc(Integer formId)

    List<DataFormRecord> findByFormIdOrderByPostedAsc(Integer formId)

    Page<DataFormRecord> findByFormId(Integer formId, Pageable pageable)
}
