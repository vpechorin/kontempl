package net.pechorina.kontempl.service

import java.util.List;

import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord
import net.pechorina.kontempl.repos.DataFormRecordRepo
import net.pechorina.kontempl.repos.DataFormRepo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataFormRecordServiceImpl implements DataFormRecordService {
	
	@Autowired
	DataFormRecordRepo recRepo
	
	@Autowired
	DataFormRepo formRepo
	
	DataFormRecord get(Integer id) {
		return recRepo.findOne(id)
	}
	
	List<DataFormRecord> findAll() {
		return recRepo.findAll()
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

	@Override
	public List<DataFormRecord> findByFormName(String name) {
		DataForm f = formRepo.findByName(name)
		if (f) {
			return recRepo.findByFormIdOrderByPostedDesc(f.id)
		}
		return null
	}

	@Override
	public List<DataFormRecord> findByFormId(Integer formId) {
		return recRepo.findByFormIdOrderByPostedDesc(formId)
	}
}
