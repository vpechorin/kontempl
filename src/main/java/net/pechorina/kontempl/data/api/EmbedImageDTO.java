package net.pechorina.kontempl.data.api;

import net.pechorina.kontempl.utils.UnitUtils;

import java.io.File;
import java.util.Objects;

public class EmbedImageDTO {
    private Integer id;
    private Integer pageId;
    private String name;
    private String contentType;
    private Integer width;
    private Integer height;
    private Long fileSize;

    public String getHFileSize() {
        return UnitUtils.bytesToHuman(fileSize);
    }

    public String getAbsolutePath() {
        return File.separator + pageId + File.separator + "embed" + File.separator + this.name;
    }

    public String getDirectoryPath() {
        return File.separator + pageId + File.separator + "embed";
    }

    public Integer getId() {
        return id;
    }

    public EmbedImageDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPageId() {
        return pageId;
    }

    public EmbedImageDTO setPageId(Integer pageId) {
        this.pageId = pageId;
        return this;
    }

    public String getName() {
        return name;
    }

    public EmbedImageDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public EmbedImageDTO setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public EmbedImageDTO setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public EmbedImageDTO setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public EmbedImageDTO setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmbedImageDTO that = (EmbedImageDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(pageId, that.pageId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(width, that.width) &&
                Objects.equals(height, that.height) &&
                Objects.equals(fileSize, that.fileSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageId, name, contentType, width, height, fileSize);
    }

    @Override
    public String toString() {
        return "EmbedImageDTO{" +
                "id=" + id +
                ", pageId=" + pageId +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", fileSize=" + fileSize +
                '}';
    }
}
