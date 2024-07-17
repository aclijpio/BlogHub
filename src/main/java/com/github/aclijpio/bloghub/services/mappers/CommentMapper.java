package com.github.aclijpio.bloghub.services.mappers;

import com.github.aclijpio.bloghub.entities.Comment;
import com.github.aclijpio.bloghub.services.dtos.comment.CommentDto;
import com.github.aclijpio.bloghub.services.mappers.config.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper extends BaseMapper<Comment, CommentDto> {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
}
