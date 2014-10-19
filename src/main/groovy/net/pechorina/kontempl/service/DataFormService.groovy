package net.pechorina.kontempl.service

import net.pechorina.kontempl.data.DataForm

interface DataFormService {
	DataForm get(Integer id)
	DataForm getByName(String name)
	DataForm getByNameAndSiteId(String name, int siteId)
	
	List<DataForm> findAll()
	
	DataForm save(DataForm f)
	
	void remove(DataForm f)
	void remove(Integer id)
}
