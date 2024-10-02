package Guthub.Backend.Mappers;

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

    public UserDto toDto(UserEntity user)
    {
        return modelMapper.map(user, UserDto.class);
    }

    public UserEntity fromDto(UserDto userDto)
    {
        return modelMapper.map(userDto, UserEntity.class);
    }

    public List<UserDto> toDtoList(List<UserEntity> users)
    {
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
