package com.github.jadepeng.pipeline.service.mapper;

import com.github.jadepeng.pipeline.domain.*;
import com.github.jadepeng.pipeline.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {}
