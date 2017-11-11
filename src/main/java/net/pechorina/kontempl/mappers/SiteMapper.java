package net.pechorina.kontempl.mappers;

import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.SiteProperty;
import net.pechorina.kontempl.data.api.SiteDTO;
import net.pechorina.kontempl.data.api.SitePropertyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateTimeMapper.class})
public interface SiteMapper {

    SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

    SiteDTO toDTO(Site site);

    @Mapping(target = "siteId", source = "site.id")
    SitePropertyDTO toSitePropertyDTO(SiteProperty siteProperty);
}
