package net.pechorina.kontempl.service

import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.repos.DataFormRepo

import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class DataFormService {
	
	@Inject
	DataFormRepo dataFormRepo
	
	DataForm get(Integer id) {
		return dataFormRepo.findOne(id)
	}
	
	DataForm getByName(String name) {
		return dataFormRepo.findByName(name)
	}
	
	List<DataForm> findAll() {
		return dataFormRepo.findAll()
	}
	
	List<DataForm> findBySiteId(int siteId) {
		return dataFormRepo.findBySiteId(siteId)
	}
	
	DataForm save(DataForm f) {
		return dataFormRepo.saveAndFlush(f)
	}
	
	void remove(DataForm f) {
		dataFormRepo.delete(f)
	}
	
	void remove(Integer id) {
		dataFormRepo.delete(id)
	}

	DataForm getByNameAndSiteId(String name, int siteId) {
		return dataFormRepo.findByNameAndSiteId(name, siteId)
	}

}
