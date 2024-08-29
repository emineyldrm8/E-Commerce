package com.haratres.ecommerce.mapper;
import com.haratres.ecommerce.dto.UserLoginDto;
import com.haratres.ecommerce.dto.UserRegisterDto;
import com.haratres.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source ="role.roleName", target = "role")
    UserRegisterDto toRegisterDTO(User user);
    UserLoginDto toLoginDTO(User user);
    @Mapping(source ="role", target = "role.roleName")
    User toEntity(UserRegisterDto userRegisterDTO);
    User toEntity(UserLoginDto userLoginDTO);
}