package com.github.jadepeng.pipeline.service.mapper;

import com.github.jadepeng.pipeline.domain.*;
import com.github.jadepeng.pipeline.service.dto.ProgramArgumentsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProgramArguments} and its DTO {@link ProgramArgumentsDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProgramVersionMapper.class })
public interface ProgramArgumentsMapper extends EntityMapper<ProgramArgumentsDTO, ProgramArguments> {
    @Mapping(target = "program", source = "program", qualifiedByName = "id")
    ProgramArgumentsDTO toDto(ProgramArguments s);
}
