package org.example.projectapp.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.projectapp.controller.UsersController;
import org.example.projectapp.controller.dto.FilterDto;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.controller.dto.SkillDto;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.UserRepository;
import org.example.projectapp.repository.specification.UserSpecificationsBuilder;
import org.example.projectapp.service.SearchCriteria;
import org.example.projectapp.service.UserService;
import org.example.projectapp.service.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final String SORT = "ASC";
    private final UserRepository repository;
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<UserDto> findUsers(SearchDto searchDto) {
        Specification<User> spec = buildSearchSpecification(searchDto.getFilters());
        int page = searchDto.getPage() == null || searchDto.getPage() < 0
                ? PAGE
                : searchDto.getPage();
        int size = searchDto.getSize() == null || searchDto.getSize() <= 0
                ? SIZE
                : searchDto.getSize();
        buildSort(searchDto.getSort());
        Page<User> users = repository.findAll(spec, PageRequest.of(page, size, buildSort(searchDto.getSort())));
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

    private Sort buildSort(String sort) {
        String direction = StringUtils.isBlank(sort) ? SORT : sort;
        Sort.Direction dir = direction.equalsIgnoreCase("ASC") ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, "firstName", "lastName");
    }

    private Specification<User> buildSearchSpecification(List<FilterDto> filters) {
        UserSpecificationsBuilder specificationsBuilder = new UserSpecificationsBuilder();
        List<SearchCriteria> searchCriteriaFromFilters = mapFiltersToSearchCriteria(filters);
        logger.info("[SEARCH] Search Criteria from filters: {}", searchCriteriaFromFilters);
        return specificationsBuilder.with(searchCriteriaFromFilters).build();
    }

    private List<SearchCriteria> mapFiltersToSearchCriteria(List<FilterDto> filters) {
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        for (FilterDto filter : filters) {
            if (filter == null || filter.getValues().isEmpty()) {
                logger.info("[SEARCH][SKIP] Filter is null or empty: {}", filter);
                continue;
            }
            SearchCriteria criteriaToAdd = SearchCriteria.builder()
                    .key(filter.getName())
                    .operation(filter.getOperation().getName())
                    .values(filter.getValues())
                    .build();

            searchCriteria.add(criteriaToAdd);
        }
        return searchCriteria;
    }

}
