package com.github.aclijpio.bloghub.services.mappers;

import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.services.dtos.post.PostDto;
import com.github.aclijpio.bloghub.services.mappers.config.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CommentMapper.class)
public interface PostMapper extends BaseMapper<Post, PostDto> {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

}
