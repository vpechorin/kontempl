package net.pechorina.kontempl.view;

import java.util.ArrayList;
import java.util.List;

public class ResultSet<T> {
	private List<T> resultList;
	private int totalResults;
	
    public ResultSet() {
        this.resultList = new ArrayList<T>();
        this.totalResults = 0;
    }
    
    public ResultSet(List<T> resultList) {
        this.resultList = resultList;
        this.totalResults = resultList.size();
    }    
    
    public ResultSet(List<T> resultList, Integer totalResults) {
        this.resultList = resultList;
        this.totalResults = totalResults;
    }

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

}
