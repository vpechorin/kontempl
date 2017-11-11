package net.pechorina.kontempl.data.api;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PageDTO {
    private Integer id;
    private Integer siteId;
    private Integer parentId;
    private Integer sortindex;
    private Boolean publicPage;
    private Boolean autoName;
    private Boolean hideTitle;
    private Boolean placeholder;
    private Boolean richText;
    private Boolean includeForm;
    private String name;
    private Long created;
    private Long updated;
    private String title;
    private String htmlTitle;
    private String description;
    private String tags;
    private String body;
    private Integer formId;
    private List<PagePropertyDTO> properties;
    private Set<EmbedImageDTO> embedImages;
    private ImageFileDTO mainImage;
    private List<ImageFileDTO> images;
    private List<DocFileDTO> docs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSortindex() {
        return sortindex;
    }

    public void setSortindex(Integer sortindex) {
        this.sortindex = sortindex;
    }

    public Boolean getPublicPage() {
        return publicPage;
    }

    public void setPublicPage(Boolean publicPage) {
        this.publicPage = publicPage;
    }

    public Boolean getAutoName() {
        return autoName;
    }

    public void setAutoName(Boolean autoName) {
        this.autoName = autoName;
    }

    public Boolean getHideTitle() {
        return hideTitle;
    }

    public void setHideTitle(Boolean hideTitle) {
        this.hideTitle = hideTitle;
    }

    public Boolean getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(Boolean placeholder) {
        this.placeholder = placeholder;
    }

    public Boolean getRichText() {
        return richText;
    }

    public void setRichText(Boolean richText) {
        this.richText = richText;
    }

    public Boolean getIncludeForm() {
        return includeForm;
    }

    public void setIncludeForm(Boolean includeForm) {
        this.includeForm = includeForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public List<PagePropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<PagePropertyDTO> properties) {
        this.properties = properties;
    }

    public Set<EmbedImageDTO> getEmbedImages() {
        return embedImages;
    }

    public void setEmbedImages(Set<EmbedImageDTO> embedImages) {
        this.embedImages = embedImages;
    }

    public ImageFileDTO getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageFileDTO mainImage) {
        this.mainImage = mainImage;
    }

    public List<ImageFileDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageFileDTO> images) {
        this.images = images;
    }

    public List<DocFileDTO> getDocs() {
        return docs;
    }

    public void setDocs(List<DocFileDTO> docs) {
        this.docs = docs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDTO pageDTO = (PageDTO) o;
        return Objects.equals(id, pageDTO.id) &&
                Objects.equals(siteId, pageDTO.siteId) &&
                Objects.equals(parentId, pageDTO.parentId) &&
                Objects.equals(name, pageDTO.name) &&
                Objects.equals(title, pageDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteId, parentId, name, title);
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "id=" + id +
                ", siteId=" + siteId +
                ", parentId=" + parentId +
                ", sortindex=" + sortindex +
                ", publicPage=" + publicPage +
                ", autoName=" + autoName +
                ", hideTitle=" + hideTitle +
                ", placeholder=" + placeholder +
                ", richText=" + richText +
                ", includeForm=" + includeForm +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", title='" + title + '\'' +
                ", htmlTitle='" + htmlTitle + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
