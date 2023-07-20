package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.dto.SearchSort;
import com.hodik.elastic.exceptions.EntityAlreadyExistsException;
import com.hodik.elastic.mappers.PageableMapper;
import com.hodik.elastic.model.Skill;
import com.hodik.elastic.model.User;
import com.hodik.elastic.repositories.UserRepository;
import com.hodik.elastic.repositories.UserSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.hodik.elastic.model.Expertise.NOVICE;
import static com.hodik.elastic.util.Operations.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EsUserServiceTest {
    private final Skill skill = new Skill("skillName", NOVICE);
    private final User USER = User.builder()
            .id(1L)
            .firstName("Name")
            .lastName("LastName")
            .email("name@gmail.com")
            .password("password")
            .cv("cv")
            .description("description")
            .role("ROLE_USER")
            .skills(List.of(skill))
            .status("EMPLOYEE")
            .type("full stack")
            .build();
    private final List<User> USERS = List.of(USER);
    private final SearchSort SEARCH_SORT = new SearchSort("Name", true);
    private final List<SearchSort> SEARCH_SORT_LIST = List.of(SEARCH_SORT);
    private final SearchFilter SEARCH_FILTER = new SearchFilter("firstName", LIKE, List.of("Name"));
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO = new SearchCriteriaDto(List.of(SEARCH_FILTER), 0, 2, SEARCH_SORT_LIST);
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO_FILTERS_NULL = new SearchCriteriaDto(null, 0, 2, SEARCH_SORT_LIST);
    private final SearchCriteriaDto SEARCH_CRITERIA_DTO_FILTERS_EMPTY = new SearchCriteriaDto(List.of(), 0, 2, SEARCH_SORT_LIST);
    private PageRequest EXPECTED_PAGE = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "Name"));
    @Mock
    private PageableMapper pageableMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserSearchRepository userSearchRepository;
    @InjectMocks
    private EsUserService userService;

    @Captor
    private ArgumentCaptor<Pageable> pageCaptor;


    @Test
    void createUserSuccess() throws EntityAlreadyExistsException {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        userService.createUser(USER);
        //then
        verify(userRepository).save(USER);
    }

    @Test
    void createUserThrowException() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER));
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () ->
            userService.createUser(USER));

        String message = exception.getMessage();
        //then
        assertEquals("User already exists id= 1", message);
    }

    @Test
    void update() {
        //when
        userService.update(USER.getId(), USER);
        //then
        verify(userRepository).save(USER);

    }

    @Test
    void delete() {
        userService.delete(USER.getId());
        verify(userRepository).deleteById(USER.getId());
    }

    @Test
    void findAll() {
        //given
        when(userRepository.findAll()).thenReturn(USERS);
        //when
        List<User> users = userService.findAll();
        //then
        assertEquals(USERS, users);
    }

    @Test
    void findAllPageable() {
        //given
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(USERS, EXPECTED_PAGE, 1));
        //when
        List<User> users = userService.findAll(EXPECTED_PAGE);
        //then
        assertEquals(USERS, users);
    }

    @Test
    void findAllWithFiltersSuccess() {
        //given
        when(userSearchRepository.findAllWithFilters(SEARCH_CRITERIA_DTO)).thenReturn(USERS);
        //when
        List<User> users = userService.findAllWithFilters(SEARCH_CRITERIA_DTO);
        //then
        verify(userSearchRepository).findAllWithFilters(SEARCH_CRITERIA_DTO);
        assertEquals(USERS, users);
    }

    @Test
    void findAllWithFiltersWithNullFilters() {
        //given

        when(userRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(USERS, EXPECTED_PAGE, 1));
        when(pageableMapper.getPageable(SEARCH_CRITERIA_DTO_FILTERS_NULL)).thenCallRealMethod();
        //when
        List<User> users = userService.findAllWithFilters(SEARCH_CRITERIA_DTO_FILTERS_NULL);
        //then
        verify(pageableMapper).getPageable(SEARCH_CRITERIA_DTO_FILTERS_NULL);
        verify(userRepository).findAll(pageCaptor.capture());
        Pageable value = pageCaptor.getValue();
        assertEquals(EXPECTED_PAGE, value);
        assertEquals(USERS, users);
    }

    @Test
    void findAllWithFiltersWithEmptyFilters() {
        //given

        when(userRepository.findAll(EXPECTED_PAGE)).thenReturn(new PageImpl<>(USERS, EXPECTED_PAGE, 1));
        when(pageableMapper.getPageable(SEARCH_CRITERIA_DTO_FILTERS_EMPTY)).thenCallRealMethod();
        //when
        List<User> users = userService.findAllWithFilters(SEARCH_CRITERIA_DTO_FILTERS_EMPTY);
        //then
        verify(pageableMapper).getPageable(SEARCH_CRITERIA_DTO_FILTERS_EMPTY);

        verify(userRepository).findAll(pageCaptor.capture());
        Pageable value = pageCaptor.getValue();
        assertEquals(EXPECTED_PAGE, value);
        assertEquals(USERS, users);
    }

    @Test
    void findById() {
        //given
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));
        //when
        Optional<User> user = userService.findById(USER.getId());
        //then
        verify(userRepository).findById(USER.getId());
        assertEquals(Optional.of(USER), user);
    }
}