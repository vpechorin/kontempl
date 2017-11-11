package net.pechorina.kontempl.mappers;

import net.pechorina.kontempl.data.EmbedImage;
import net.pechorina.kontempl.data.api.EmbedImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateTimeMapper.class})
public interface EmbedImageMapper {
    EmbedImageMapper INSTANCE = Mappers.getMapper(EmbedImageMapper.class);
    EmbedImageDTO toDTO(EmbedImage embedImage);
}
