package org.example.projectapp.mapper;

import org.example.projectapp.mapper.dto.UserElasticDto;
import org.example.projectapp.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserElasticDto convertToUserElasticDto(User user) {
        return modelMapper.map(user, UserElasticDto.class);
    }

}
