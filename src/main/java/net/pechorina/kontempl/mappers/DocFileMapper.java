package net.pechorina.kontempl.mappers;

import net.pechorina.kontempl.data.DocFile;
import net.pechorina.kontempl.data.api.DocFileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateTimeMapper.class})
public interface DocFileMapper {
    DocFileMapper INSTANCE = Mappers.getMapper(DocFileMapper.class);
    DocFileDTO toDTO(DocFile docFile);
}
