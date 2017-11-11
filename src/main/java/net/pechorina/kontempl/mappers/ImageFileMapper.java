package net.pechorina.kontempl.mappers;

import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.api.ImageFileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateTimeMapper.class, ThumbnailMapper.class})
public interface ImageFileMapper {
    ImageFileMapper INSTANCE = Mappers.getMapper(ImageFileMapper.class);
    ImageFileDTO toDTO(ImageFile imageFile);
}
