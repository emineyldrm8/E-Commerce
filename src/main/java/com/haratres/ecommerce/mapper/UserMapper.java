package com.haratres.ecommerce.mapper;
import com.haratres.ecommerce.dto.UserLoginDto;
import com.haratres.ecommerce.dto.UserRegisterDto;
import com.haratres.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserRegisterDto toRegisterDTO(User user);
    UserLoginDto toLoginDTO(User user);
    User toEntity(UserRegisterDto userRegisterDTO);
    User toEntity(UserLoginDto userLoginDTO);
}