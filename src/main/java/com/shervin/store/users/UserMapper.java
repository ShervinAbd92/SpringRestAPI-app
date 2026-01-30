package com.shervin.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserRegisterDto dto);
    void update(UpdateUserRequestDto request, @MappingTarget User user);
}
