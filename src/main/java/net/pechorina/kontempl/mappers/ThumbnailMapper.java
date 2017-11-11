package net.pechorina.kontempl.mappers;

import net.pechorina.kontempl.data.Thumbnail;
import net.pechorina.kontempl.data.api.ThumbnailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateTimeMapper.class})
public interface ThumbnailMapper {
    ThumbnailMapper INSTANCE = Mappers.getMapper(ThumbnailMapper.class);

    ThumbnailDTO toDTO(Thumbnail v);
}
