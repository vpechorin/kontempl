package net.pechorina.kontempl.data.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.pechorina.kontempl.utils.UnitUtils;

import java.io.File;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageFileDTO {
    private Integer id;
    private Integer pageId;
    private String name;
    private String contentType;
    private Boolean mainImage;
    private Integer sortIndex;
    private Integer width;
    private Integer height;
    private Integer fileSize;
    private ThumbnailDTO thumb;

    public String getHFileSize() {
        return UnitUtils.bytesToHuman(fileSize);
    }

    public String getAbsolutePath() {
        return File.separator + pageId + File.separator + "images" + File.separator + this.name;
    }

    public String getDirectoryPath() {
        return File.separator + pageId + File.separator + "images";
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getMainImage() {
        return mainImage;
    }

    public void setMainImage(Boolean mainImage) {
        this.mainImage = mainImage;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
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

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public ThumbnailDTO getThumb() {
        return thumb;
    }

    public void setThumb(ThumbnailDTO thumb) {
        this.thumb = thumb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageFileDTO that = (ImageFileDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(pageId, that.pageId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(mainImage, that.mainImage) &&
                Objects.equals(sortIndex, that.sortIndex) &&
                Objects.equals(width, that.width) &&
                Objects.equals(height, that.height) &&
                Objects.equals(fileSize, that.fileSize) &&
                Objects.equals(thumb, that.thumb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageId, name, contentType, mainImage, sortIndex, width, height, fileSize, thumb);
    }

    @Override
    public String toString() {
        return "ImageFileDTO{" +
                "id=" + id +
                ", pageId=" + pageId +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", mainImage=" + mainImage +
                ", sortIndex=" + sortIndex +
                ", width=" + width +
                ", height=" + height +
                ", fileSize=" + fileSize +
                ", thumb=" + thumb +
                '}';
    }
}
