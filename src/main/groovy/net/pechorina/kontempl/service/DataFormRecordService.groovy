package net.pechorina.kontempl.service

import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.DataFormRecord

interface DataFormRecordService {
	DataFormRecord get(Integer id)
	
	List<DataFormRecord> findByFormName(String name)
	List<DataFormRecord> findByFormId(Integer formId)
	
	List<DataFormRecord> findAll()
	
	DataFormRecord save(DataFormRecord r)
	
	void remove(DataFormRecord f)
	void remove(Integer id)
}
