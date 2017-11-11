package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageNode implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String name;
	private boolean placeholder;
	private List<PageNode> nodes;
	private List<DocFile> files;
	private List<ImageFile> images;

	public PageNode() {
		super();
		this.nodes = new ArrayList<>();
		this.files = new ArrayList<>();
		this.images = new ArrayList<>();
	}
	
	public PageNode(Page p) {
		super();
		this.nodes = new ArrayList<>();
		this.files = new ArrayList<>();
		this.images = new ArrayList<>();
		
		this.id = p.getId();
		this.title = p.getTitle();
		this.name = p.getName();
		this.placeholder = p.getPlaceholder();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<PageNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<PageNode> nodes) {
		this.nodes = nodes;
	}
	
	public void addChild(PageNode n) {
		this.nodes.add(n);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(boolean placeholder) {
		this.placeholder = placeholder;
	}

	public List<DocFile> getFiles() {
		return files;
	}

	public void setFiles(List<DocFile> files) {
		this.files.clear();
		this.files.addAll( files );
	}

	public List<ImageFile> getImages() {
		return images;
	}

	public void setImages(List<ImageFile> images) {
		this.images.clear();
		this.images.addAll(images);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageNode [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", nodes=");
		if (nodes != null) {
			builder.append(nodes.size());
		} else {
			builder.append(0);
		}
		builder.append("]");
		return builder.toString();
	}

}
