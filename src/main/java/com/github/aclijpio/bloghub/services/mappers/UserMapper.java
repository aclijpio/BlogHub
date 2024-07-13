package com.github.aclijpio.bloghub.services.mappers;

import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.services.dtos.UserDto;
import com.github.aclijpio.bloghub.services.mappers.config.BaseMapper;
import com.github.aclijpio.bloghub.services.mappers.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface UserMapper extends BaseMapper<User, UserDto> {
}
