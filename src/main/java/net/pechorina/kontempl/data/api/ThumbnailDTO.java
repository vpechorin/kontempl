package net.pechorina.kontempl.data.api;

import java.io.File;
import java.util.Objects;

public class ThumbnailDTO {
    private Integer id;
    private Integer pageId;
    private String name;
    private Integer width;
    private Integer height;
    private Integer imageFileId;

    public String getAbsolutePath() {
        return getDirectoryPath() + File.separator + this.name;
    }

    public String getDirectoryPath() {
        return File.separator + pageId + File.separator + "thumbs";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(Integer imageFileId) {
        this.imageFileId = imageFileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThumbnailDTO that = (ThumbnailDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(pageId, that.pageId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(width, that.width) &&
                Objects.equals(height, that.height) &&
                Objects.equals(imageFileId, that.imageFileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageId, name, width, height, imageFileId);
    }

    @Override
    public String toString() {
        return "ThumbnailDTO{" +
                "id=" + id +
                ", pageId=" + pageId +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", imageFileId=" + imageFileId +
                '}';
    }
}
