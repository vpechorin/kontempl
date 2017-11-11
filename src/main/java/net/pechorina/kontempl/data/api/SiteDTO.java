package net.pechorina.kontempl.data.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SiteDTO {
    private Integer id;
    private String name;
    private String title;
    private String domain;
    private String homePage;
    @JsonIgnore
    private List<SitePropertyDTO> properties;

    @JsonProperty(value = "propertyMap")
    public Map<String, String> getPropertyMap() {
        if (properties == null) return Collections.emptyMap();
        return properties.stream()
                .collect(Collectors.toMap(e -> e.getName(), e -> e.getContent()));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public List<SitePropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<SitePropertyDTO> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteDTO siteDTO = (SiteDTO) o;
        return Objects.equals(id, siteDTO.id) &&
                Objects.equals(name, siteDTO.name) &&
                Objects.equals(title, siteDTO.title) &&
                Objects.equals(domain, siteDTO.domain) &&
                Objects.equals(homePage, siteDTO.homePage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, domain, homePage);
    }

    @Override
    public String toString() {
        return "SiteDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", domain='" + domain + '\'' +
                ", homePage='" + homePage + '\'' +
                ", properties=" + properties +
                '}';
    }
}
