package com.hodik.elastic.services;

import com.google.gson.Gson;
import com.hodik.elastic.dto.SearchCriteriaDto;
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
import org.springframework.data.elasticsearch.core.ResourceUtil;

import java.util.List;
import java.util.Optional;

import static com.hodik.elastic.model.Expertise.NOVICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EsUserServiceTest {
    private static final Skill SKILL = new Skill("skillName", NOVICE);
    public static final long ID = 1L;
    public static final String NAME = "Name";
    public static final String LAST_NAME = "LastName";
    public static final String NAME_GMAIL_COM = "name@gmail.com";
    public static final String PASSWORD = "password";
    public static final String CV = "cv";
    public static final String DESCRIPTION = "description";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String FULL_STACK = "full stack";
    public static final int PAGE = 0;
    public static final int SIZE = 2;
    private final User expectedUser = getUserBuild();

    private final List<User> expectedUserList = List.of(expectedUser);
    private final SearchSort searchSort = new SearchSort("Name", true);
    private final List<SearchSort> searchSortList = List.of(searchSort);

    private final Gson gson= new Gson();

    private final SearchCriteriaDto searchCriteriaDtoSuccess = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.user.success.json"), SearchCriteriaDto.class);
    private final SearchCriteriaDto searchCriteriaDtoWrong = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.user.wrong.column.json"), SearchCriteriaDto.class);
    private final SearchCriteriaDto searchCriteriaDtoFiltersNull = new SearchCriteriaDto(null, PAGE, SIZE, searchSortList);
    private final SearchCriteriaDto searchCriteriaDtoFiltersEmpty = new SearchCriteriaDto(List.of(), PAGE, SIZE, searchSortList);
    private final PageRequest expectedPage = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, NAME));
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
    void shouldCreateUser() throws EntityAlreadyExistsException {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        userService.createUser(expectedUser);
        //then
        verify(userRepository).save(expectedUser);
    }

    @Test
    void shouldTrowExceptionWhenUserAlreadyExists() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () ->
            userService.createUser(expectedUser));

        String message = exception.getMessage();
        //then
        assertEquals("User already exists id= 1", message);
    }

    @Test
    void shouldUpdateUser() {
        //when
        userService.update(expectedUser.getId(), expectedUser);
        //then
        verify(userRepository).save(expectedUser);

    }

    @Test
    void shouldDeleteUser() {
        userService.delete(ID);
        verify(userRepository).deleteById(expectedUser.getId());
    }

    @Test
    void shouldReturnAllUsers () {
        //given
        when(userRepository.findAll()).thenReturn(expectedUserList);
        //when
        List<User> users = userService.findAll();
        //then
        assertEquals(expectedUserList, users);
    }

    @Test
    void shouldReturnAllUsersByPageable() {
        //given
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(expectedUserList, expectedPage, 1));
        //when
        List<User> users = userService.findAll(expectedPage);
        //then
        assertEquals(expectedUserList, users);
    }

    @Test
    void shouldReturnUsersByFilters() {
        //given
        when(userSearchRepository.findAllWithFilters(searchCriteriaDtoSuccess)).thenReturn(expectedUserList);
        //when
        List<User> users = userService.findAllWithFilters(searchCriteriaDtoSuccess);
        //then
        verify(userSearchRepository).findAllWithFilters(searchCriteriaDtoSuccess);
        assertEquals(expectedUserList, users);
    }
    @Test
    void shouldTrowExceptionWhenWrongColumn() {

        //when
      assertThrows(IllegalArgumentException.class, ()->userService.findAllWithFilters(searchCriteriaDtoWrong));
        //then
        verify(userSearchRepository, never()).findAllWithFilters(searchCriteriaDtoWrong);

    }

    @Test
    void shouldReturnUsersWithNullFilters() {
        //given

        when(userRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedUserList, expectedPage, 1));
        when(pageableMapper.getPageable(searchCriteriaDtoFiltersNull)).thenCallRealMethod();
        //when
        List<User> users = userService.findAllWithFilters(searchCriteriaDtoFiltersNull);
        //then
        verify(pageableMapper).getPageable(searchCriteriaDtoFiltersNull);
        verify(userRepository).findAll(pageCaptor.capture());
        Pageable value = pageCaptor.getValue();
        assertEquals(expectedPage, value);
        assertEquals(expectedUserList, users);
    }

    @Test
    void shouldReturnUsersWithEmptyFilters() {
        //given

        when(userRepository.findAll(expectedPage)).thenReturn(new PageImpl<>(expectedUserList, expectedPage, 1));
        when(pageableMapper.getPageable(searchCriteriaDtoFiltersEmpty)).thenCallRealMethod();
        //when
        List<User> users = userService.findAllWithFilters(searchCriteriaDtoFiltersEmpty);
        //then
        verify(pageableMapper).getPageable(searchCriteriaDtoFiltersEmpty);

        verify(userRepository).findAll(pageCaptor.capture());
        Pageable value = pageCaptor.getValue();
        assertEquals(expectedPage, value);
        assertEquals(expectedUserList, users);
    }

    @Test
    void shouldReturnUserById() {
        //given
        when(userRepository.findById(ID)).thenReturn(Optional.of(expectedUser));
        //when
        Optional<User> user = userService.findById(expectedUser.getId());
        //then
        verify(userRepository).findById(expectedUser.getId());
        assertEquals(Optional.of(expectedUser), user);
    }
    private static User getUserBuild() {
        return User.builder()
                .id(ID)
                .firstName(NAME)
                .lastName(LAST_NAME)
                .email(NAME_GMAIL_COM)
                .password(PASSWORD)
                .cv(CV)
                .description(DESCRIPTION)
                .role(ROLE_USER)
                .skills(List.of(SKILL))
                .status(EMPLOYEE)
                .type(FULL_STACK)
                .build();
    }
}