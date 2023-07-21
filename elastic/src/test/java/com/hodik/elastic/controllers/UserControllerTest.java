package com.hodik.elastic.controllers;

import com.google.gson.Gson;
import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.UserDto;
import com.hodik.elastic.exceptions.EntityAlreadyExistsException;
import com.hodik.elastic.exceptions.EntityNotFoundException;
import com.hodik.elastic.mappers.UserMapper;
import com.hodik.elastic.model.Role;
import com.hodik.elastic.model.Skill;
import com.hodik.elastic.model.Status;
import com.hodik.elastic.model.User;
import com.hodik.elastic.services.EsUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ResourceUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static com.hodik.elastic.model.Expertise.NOVICE;
import static com.hodik.elastic.util.Operations.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private static final Skill SKILL = new Skill("skillName", NOVICE);
    public static final long ID = 1L;
    public static final String NAME = "Name";
    public static final String LAST_NAME = "LastName";
    public static final String NAME_GMAIL_COM = "name@gmail.com";
    public static final String PASSWORD = "password";
    public static final String CV = "cv";
    public static final String DESCRIPTION = "description";
    public static final Role ROLE_USER = Role.ROLE_USER;
    public static final Status STATUS = Status.NEW;
    public static final String FULL_STACK = "full stack";
    private final User expectedUser = getUserBuild();
    private final List<User> expectedUserList = List.of(expectedUser);
    private final UserDto expectedUserDto = getUserDtoBuild();
    private final List<UserDto> expectedUserDtoList = List.of(expectedUserDto);

    private final Gson gson = new Gson();
    private final SearchCriteriaDto searchCriteriaDto = gson.fromJson(ResourceUtil.readFileFromClasspath("search.criteria.user.success.json"), SearchCriteriaDto.class);
    @Mock
    private EsUserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldCreateUser() throws EntityAlreadyExistsException {
        //given
        when(userMapper.convertToUser(expectedUserDto)).thenReturn(expectedUser);
        //when
        ResponseEntity<HttpStatus> response = userController.createUser(expectedUserDto);
        //then
        verify(userService).createUser(expectedUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() throws EntityAlreadyExistsException {
        //given
        when(userMapper.convertToUser(expectedUserDto)).thenReturn(expectedUser);
        Mockito.doThrow(EntityAlreadyExistsException.class).when(userService).createUser(expectedUser);

        //when
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> userController.createUser(expectedUserDto));

        //then
        assertEquals(EntityAlreadyExistsException.class, exception.getClass());
    }

    @Test
    void shouldUpdateUser() {
        //given
        when(userMapper.convertToUser(expectedUserDto)).thenReturn(expectedUser);
        //when
        ResponseEntity<HttpStatus> response = userController.updateUser(expectedUser.getId(), expectedUserDto);
        //then
        verify(userMapper).convertToUser(expectedUserDto);
        verify(userService).update(expectedUser.getId(), expectedUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnUserDtoList() {
        //given
        when(userMapper.convertToUserDto(expectedUser)).thenReturn(expectedUserDto);
        when(userService.findAll()).thenReturn(expectedUserList);
        //when
        List<UserDto> userDtoList = userController.getUsers();
        //then
        verify(userService).findAll();
        verify(userMapper, atLeast(1)).convertToUserDto(expectedUser);
        assertEquals(expectedUserDtoList, userDtoList);
    }

    @Test
    void shouldReturnUserDto() {
        //given
        when(userMapper.convertToUserDto(expectedUser)).thenReturn(expectedUserDto);
        when(userService.findById(expectedUserDto.getId())).thenReturn(Optional.of(expectedUser));
        //when
        UserDto userDto = userController.getUser(expectedUserDto.getId());
        //then
        verify(userMapper).convertToUserDto(expectedUser);
        verify(userService).findById(expectedUser.getId());
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void getUserException() {
        //given

        when(userService.findById(expectedUserDto.getId())).thenThrow(EntityNotFoundException.class);
        //when
        assertThrows(EntityNotFoundException.class, () -> userController.getUser(expectedUserDto.getId()));
        //then

        verify(userService).findById(expectedUser.getId());

    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        //given

        when(userService.findById(expectedUserDto.getId())).thenThrow(EntityNotFoundException.class);
        //when
        assertThrows(EntityNotFoundException.class, () -> userController.getUser(expectedUserDto.getId()));
        //then

        verify(userService).findById(expectedUser.getId());

    }

    @Test
    void shouldDeleteUser() {
        //when
        ResponseEntity<HttpStatus> response = userController.deleteUser(expectedUserDto.getId());
        //then
        verify(userService).delete(expectedUser.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnUserDtoListByFilters() {
        //given
        when(userMapper.convertToUserDto(expectedUser)).thenReturn(expectedUserDto);
        when(userService.findAllWithFilters(searchCriteriaDto)).thenReturn(expectedUserList);
        //when
        List<UserDto> userDtoList = userController.searchByCriteria(searchCriteriaDto);
        //then
        verify(userMapper).convertToUserDto(expectedUser);
        verify(userService).findAllWithFilters(searchCriteriaDto);
        assertEquals(expectedUserDtoList, userDtoList);
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
                .status(STATUS)
                .type(FULL_STACK)
                .build();
    }

    private UserDto getUserDtoBuild() {
        return UserDto.builder()
                .id(ID)
                .firstName(NAME)
                .lastName(LAST_NAME)
                .email(NAME_GMAIL_COM)
                .password(PASSWORD)
                .cv(CV)
                .description(DESCRIPTION)
                .role(ROLE_USER)
                .skills(List.of(SKILL))
                .status(STATUS)
                .type(FULL_STACK)
                .build();
    }
}