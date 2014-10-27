package net.pechorina.kontempl.repos

import java.util.List;

import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.Thumbnail;
import net.pechorina.kontempl.data.User;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface DataFormRecordRepo extends JpaRepository<DataFormRecord, Integer> {
	
	List<DataFormRecord> findByFormIdOrderByPostedDesc(Integer formId)
	List<DataFormRecord> findByFormIdOrderByPostedAsc(Integer formId)
	
	Page<DataFormRecord> findByFormId(Integer formId, Pageable pageable)
}
