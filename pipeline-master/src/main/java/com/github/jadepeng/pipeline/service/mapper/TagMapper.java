package com.github.jadepeng.pipeline.service.mapper;

import com.github.jadepeng.pipeline.domain.*;
import com.github.jadepeng.pipeline.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppMapper.class, ProgramMapper.class })
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "app", source = "app", qualifiedByName = "id")
    @Mapping(target = "program", source = "program", qualifiedByName = "id")
    TagDTO toDto(Tag s);
}
