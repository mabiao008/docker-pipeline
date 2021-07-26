package com.github.jadepeng.pipeline.service.mapper;

import com.github.jadepeng.pipeline.domain.*;
import com.github.jadepeng.pipeline.service.dto.DockerImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DockerImage} and its DTO {@link DockerImageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DockerImageMapper extends EntityMapper<DockerImageDTO, DockerImage> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DockerImageDTO toDtoId(DockerImage dockerImage);
}
