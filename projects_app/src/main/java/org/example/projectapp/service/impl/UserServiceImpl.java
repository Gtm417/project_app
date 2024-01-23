package org.example.projectapp.service.impl;

import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.controller.dto.SearchUserDto;
import org.example.projectapp.controller.dto.SkillDto;
import org.example.projectapp.mapper.PageableMapper;
import org.example.projectapp.mapper.SearchElasticCriteriaDtoMapper;
import org.example.projectapp.mapper.UserMapper;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.example.projectapp.mapper.dto.UserElasticDto;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.UserRepository;
import org.example.projectapp.restclient.ElasticUsersServiceClient;
import org.example.projectapp.service.UserService;
import org.example.projectapp.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final PageableMapper pageableMapper;
    private final SearchElasticCriteriaDtoMapper elasticCriteriaDtoMapper;
    private final ElasticUsersServiceClient elasticUsersServiceClient;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository repository, SearchCriteriaBuilder<User> searchCriteriaBuilder,
                           PageableMapper pageableMapper,
                           @Qualifier("searchUserElasticDtoMapper") SearchElasticCriteriaDtoMapper elasticCriteriaDtoMapper, ElasticUsersServiceClient elasticUsersServiceClient, UserMapper userMapper) {
        this.repository = repository;
        this.searchCriteriaBuilder = searchCriteriaBuilder;
        this.pageableMapper = pageableMapper;
        this.elasticCriteriaDtoMapper = elasticCriteriaDtoMapper;
        this.elasticUsersServiceClient = elasticUsersServiceClient;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserDto> findUsers(SearchDto searchDto) {
        Specification<User> spec = searchCriteriaBuilder.buildUserSearchSpecification(searchDto.getFilters());
        Pageable pageable = pageableMapper.getPageable(searchDto);
        Page<User> users = repository.findAll(spec, pageable);
        return mapToUserDto(users);
    }

    @Override
    public List<User> findAllUsers() {
        return repository.findAll();
    }

    @Override
    public List<User> findUsersByListId(List<Long> listID) {
        return repository.findAllById(listID);
    }

    @Override
    public List<UserElasticDto> findUsersInElastic(SearchUserDto searchDto) {
        SearchElasticCriteriaDto searchElasticCriteriaDto =
                elasticCriteriaDtoMapper.convertToSearchElasticCriteriaDto(searchDto);
        return elasticUsersServiceClient.searchUsers(searchElasticCriteriaDto);
    }

    @Override
    public void saveCV(long id, byte[] cv) {
        User user = repository.findById(id).orElseThrow();
        user.setCv(cv);
        repository.save(user);
        User savedUser = repository.save(user);
        elasticUsersServiceClient.updateUser(id, userMapper.convertToUserElasticDto(user));
    }

    @Override
    public UserDto findUserById(long id) {
        return buildUserDto(repository.findById(id).orElseThrow());
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
                .cv(user.getCv())
                .type(user.getType())
                .build();

        userDto.setSkills(user.getSkills().stream()
                .map(se -> new SkillDto(se.getSkill().getName(), se.getExpertise()))
                .collect(Collectors.toList()));
        return userDto;
    }

}
