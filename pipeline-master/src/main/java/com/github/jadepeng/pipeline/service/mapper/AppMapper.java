package com.github.jadepeng.pipeline.service.mapper;

import com.github.jadepeng.pipeline.domain.*;
import com.github.jadepeng.pipeline.service.dto.AppDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link App} and its DTO {@link AppDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppMapper extends EntityMapper<AppDTO, App> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppDTO toDtoId(App app);
}
