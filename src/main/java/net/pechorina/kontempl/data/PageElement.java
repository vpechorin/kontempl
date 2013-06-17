package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "pageelement")
public class PageElement implements Serializable, Cloneable {
	private static final Logger logger = Logger.getLogger(PageElement.class);
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		
	private Integer id;

	@NotNull
	@Index(name="pageIdIndex")
    private Integer pageId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
	
	@Index(name="updatedByIDX")
	private Integer updatedBy;
	
	@NotNull
	@Index(name="nameIndex")
    private String name;
    private String customName;    
    
    @Lob
    private String body;

    public PageElement() {
        pageId = 0;
        name = "";
        customName = "";        
        body = "";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
    
    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }
    
    public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageElement [id=");
		builder.append(id);
		builder.append(", pageId=");
		builder.append(pageId);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone(); 
	}
	
	public PageElement copy() {
		PageElement newPageEl = null;
		try {
			newPageEl = (PageElement) this.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("Exception: " + e);
		}
		if (newPageEl != null) {
			newPageEl.setId(null);
		}
		return newPageEl;
	}

}