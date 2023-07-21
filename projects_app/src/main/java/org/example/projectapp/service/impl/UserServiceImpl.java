package org.example.projectapp.service.impl;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.controller.dto.SkillDto;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.UserRepository;
import org.example.projectapp.service.UserService;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final String SORT = "ASC";
    private final UserRepository repository;
    private final SearchCriteriaBuilder<User> searchCriteriaBuilder;

    public UserServiceImpl(UserRepository repository, SearchCriteriaBuilder<User> searchCriteriaBuilder) {
        this.repository = repository;
        this.searchCriteriaBuilder = searchCriteriaBuilder;
    }

    @Override
    public Page<UserDto> findUsers(SearchDto searchDto) {
        Specification<User> spec = searchCriteriaBuilder.buildUserSearchSpecification(searchDto.getFilters());
        Page<User> users =
                repository.findAll(spec,
                        searchCriteriaBuilder.getPagination(searchDto, "firstName", "lastName"));
        return mapToUserDto(users);
    }

    private Page<UserDto> mapToUserDto(Page<User> users) {
        List<UserDto> collect = users.stream().map(this::buildUserDto).collect(Collectors.toList());
        return new PageImpl<>(collect);
    }

    private UserDto buildUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .status(user.getStatus())
                .picture(user.getPicture())
                .type(user.getType())
                .build();

        userDto.setSkills(user.getSkills().stream()
                .map(se -> new SkillDto(se.getSkill().getName(), se.getExpertise()))
                .collect(Collectors.toList()));
        return userDto;
    }

}
