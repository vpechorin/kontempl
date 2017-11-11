package net.pechorina.kontempl.mappers;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageProperty;
import net.pechorina.kontempl.data.api.PageDTO;
import net.pechorina.kontempl.data.api.PagePropertyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateTimeMapper.class, ImageFileMapper.class, DocFileMapper.class, EmbedImageMapper.class, SiteMapper.class})
public interface PageMapper {
    PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);

    @Mappings({
            @Mapping(target = "siteId", source = "site.id")
    })
    PageDTO toPageDTO(Page page);

    @Mappings({
        @Mapping(target = "pageId", source = "page.id")
    })
    PagePropertyDTO toPagePropertyDTO(PageProperty pageProperty);

}
