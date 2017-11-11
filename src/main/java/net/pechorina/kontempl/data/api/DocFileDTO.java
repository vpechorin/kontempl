package net.pechorina.kontempl.data.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.pechorina.kontempl.utils.UnitUtils;

import java.io.File;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocFileDTO {
    private Integer id;
    private Integer pageId;
    private String name;
    private String title;
    private String contentType;
    private Integer sortIndex;
    private Integer width;
    private Integer height;
    private Long fileSize;

    public String getHFileSize() {
        return UnitUtils.bytesToHuman(fileSize);
    }

    public String getAbsolutePath() {
        return File.separator + pageId + File.separator + "docs" + File.separator + this.name;
    }

    public String getDirectoryPath() {
        return File.separator + pageId + File.separator + "docs";
    }

    public Integer getId() {
        return id;
    }

    public DocFileDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPageId() {
        return pageId;
    }

    public DocFileDTO setPageId(Integer pageId) {
        this.pageId = pageId;
        return this;
    }

    public String getName() {
        return name;
    }

    public DocFileDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DocFileDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public DocFileDTO setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public DocFileDTO setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public DocFileDTO setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public DocFileDTO setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public DocFileDTO setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocFileDTO that = (DocFileDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(pageId, that.pageId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(title, that.title) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(sortIndex, that.sortIndex) &&
                Objects.equals(width, that.width) &&
                Objects.equals(height, that.height) &&
                Objects.equals(fileSize, that.fileSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageId, name, title, contentType, sortIndex, width, height, fileSize);
    }

    @Override
    public String toString() {
        return "DocFileDTO{" +
                "id=" + id +
                ", pageId=" + pageId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", contentType='" + contentType + '\'' +
                ", sortIndex=" + sortIndex +
                ", width=" + width +
                ", height=" + height +
                ", fileSize=" + fileSize +
                '}';
    }
}
