package com.github.jadepeng.pipeline.service.mapper;

import com.github.jadepeng.pipeline.domain.*;
import com.github.jadepeng.pipeline.service.dto.ProgramVersionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProgramVersion} and its DTO {@link ProgramVersionDTO}.
 */
@Mapper(componentModel = "spring", uses = { DockerImageMapper.class })
public interface ProgramVersionMapper extends EntityMapper<ProgramVersionDTO, ProgramVersion> {
    @Mapping(target = "dockerImage", source = "dockerImage", qualifiedByName = "id")
    ProgramVersionDTO toDto(ProgramVersion s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProgramVersionDTO toDtoId(ProgramVersion programVersion);
}
