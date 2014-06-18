package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageNode implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private List<PageNode> nodes;

	public PageNode() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public PageNode(Page p) {
		super();
		this.id = p.getId();
		this.title = p.getTitle();
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
		if (this.nodes == null) this.nodes = new ArrayList<PageNode>();
		this.nodes.add(n);
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
