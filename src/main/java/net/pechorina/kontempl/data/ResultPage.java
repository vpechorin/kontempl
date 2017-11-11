package net.pechorina.kontempl.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

public class ResultPage<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int number;
	private int size;
	private int totalPages;
	private int numberOfElements;
	private long totalElements;
	private boolean previous;
	private boolean first;
	private boolean next;
	private boolean last;
	private List<T> content;

	public ResultPage() {
		super();
	}

	public ResultPage(Page<T> p) {
		this.number = p.getNumber();
		this.size = p.getSize();
		this.totalPages = p.getTotalPages();
		this.numberOfElements = p.getNumberOfElements();
		this.totalElements = p.getTotalElements();
		this.previous = p.hasPrevious();
		this.next = p.hasNext();
		this.first = p.isFirst();
		this.last = p.isLast();
		this.content = p.getContent();
	}
	
	public boolean processPage( Consumer<List<T>> consumer ) {
		boolean hasMore = false;
		if (this.content != null) {
			consumer.accept(this.content);
			if (this.next) hasMore = true;
		}
		return hasMore;
	}
	
	public boolean processEachItemInPage( Consumer<T> consumer ) {
		boolean hasMore = false;
		if (this.content != null) {
			this.content.forEach(consumer);
			if (this.next) hasMore = true;
		}
		return hasMore;
	}

	@JsonIgnore
	public boolean hasPrevious() {
		return this.previous;
	}

	@JsonIgnore
	public boolean hasNext() {
		return this.next;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public boolean isPrevious() {
		return previous;
	}

	public void setPrevious(boolean previous) {
		this.previous = previous;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
