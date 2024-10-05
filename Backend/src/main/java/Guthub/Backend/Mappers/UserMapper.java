package Guthub.Backend.Mappers;

import Guthub.Backend.Dtos.DetailedUserDto;
import Guthub.Backend.Dtos.RegisterDto;
import Guthub.Backend.Dtos.UserDto;
import Guthub.Backend.Models.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper
{
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public UserDto toUserDto(UserEntity user)
    {
        return modelMapper.map(user, UserDto.class);
    }

    public DetailedUserDto toDetailedUserDto(UserEntity user)
    {
        return modelMapper.map(user, DetailedUserDto.class);
    }

    public UserEntity fromDetailedUserDto(DetailedUserDto detailedUserDto)
    {
        return modelMapper.map(detailedUserDto, UserEntity.class);
    }

    public UserEntity fromRegisterDto(RegisterDto registerDto)
    {
        return modelMapper.map(registerDto, UserEntity.class);
    }

    public List<UserDto> toUserDtoList(List<UserEntity> users)
    {
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }
}
