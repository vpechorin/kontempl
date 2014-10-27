package net.pechorina.kontempl.service

import java.util.List;

import javax.inject.Inject;

import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord
import net.pechorina.kontempl.repos.DataFormRecordRepo
import net.pechorina.kontempl.repos.DataFormRepo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class DataFormRecordService {
	
	@Inject
	DataFormRecordRepo recRepo
	
	@Inject
	DataFormRepo formRepo
	
	DataFormRecord get(Integer id) {
		return recRepo.findOne(id)
	}
	
	List<DataFormRecord> findAll() {
		return recRepo.findAll()
	}

	List<DataFormRecord> findAll(Sort sort) {
		return recRepo.findAll(sort)
	}
		
	DataFormRecord save(DataFormRecord f) {
		return recRepo.saveAndFlush(f)
	}
	
	void remove(DataFormRecord f) {
		recRepo.delete(f)
	}
	
	void remove(Integer id) {
		recRepo.delete(id)
	}

	public List<DataFormRecord> findByFormName(String name) {
		DataForm f = formRepo.findByName(name)
		if (f) {
			return recRepo.findByFormIdOrderByPostedDesc(f.id)
		}
		return null
	}
	
	public Page<DataFormRecord> findByFormId(Integer formId, Pageable pageable) {
		return recRepo.findByFormId(formId, pageable)
	}

}
