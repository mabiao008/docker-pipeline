package com.github.jadepeng.pipeline.service.mapper;

import com.github.jadepeng.pipeline.domain.*;
import com.github.jadepeng.pipeline.service.dto.ProgramDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Program} and its DTO {@link ProgramDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProgramVersionMapper.class })
public interface ProgramMapper extends EntityMapper<ProgramDTO, Program> {
    @Mapping(target = "onlineVersion", source = "onlineVersion", qualifiedByName = "id")
    ProgramDTO toDto(Program s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProgramDTO toDtoId(Program program);
}
