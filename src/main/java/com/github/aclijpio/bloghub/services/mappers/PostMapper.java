package com.github.aclijpio.bloghub.services.mappers;

import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.services.dtos.PostDto;
import com.github.aclijpio.bloghub.services.mappers.config.BaseMapper;
import com.github.aclijpio.bloghub.services.mappers.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PostMapper extends BaseMapper<Post, PostDto> {
}
