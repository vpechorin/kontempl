package net.pechorina.kontempl.mappers;

import org.joda.time.DateTime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Mapper
public interface DateTimeMapper {
    DateTimeMapper INSTANCE = Mappers.getMapper(DateTimeMapper.class);

    default Long toMillis(ZonedDateTime dt) {
        return dt == null ? null : dt.toInstant().toEpochMilli();
    }

    default Long toMillis(DateTime dt) {
        return dt == null ? null : dt.getMillis();
    }

    default ZonedDateTime toZonedDateTime(Long millis) {
        return millis == null ? null : ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
    }

    default DateTime toDateTime(Long millis) {
        return millis == null ? null : new DateTime(millis.longValue());
    }
}
