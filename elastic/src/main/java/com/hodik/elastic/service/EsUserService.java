package com.hodik.elastic.service;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.exception.EntityAlreadyExistsException;
import com.hodik.elastic.mapper.PageableMapper;
import com.hodik.elastic.model.User;
import com.hodik.elastic.repository.UserRepository;
import com.hodik.elastic.repository.UserSearchRepository;
import com.hodik.elastic.util.SearchColumnUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class EsUserService {
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;
    private final PageableMapper pageableMapper;

    @Autowired
    public EsUserService(UserRepository userRepository, UserSearchRepository userSearchRepository, PageableMapper pageableMapper) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.pageableMapper = pageableMapper;
    }

    public void createUser(User user) throws EntityAlreadyExistsException {
        long id = user.getId();
        if (userRepository.findById(id).isPresent()) {
            throw new EntityAlreadyExistsException("User already exists id= " + id);
        }
        userRepository.save(user);
        log.info("[ELASTIC] User is saved to ES successful id = " + id);
    }

    public void update(long id, User user) {
        user.setId(id);
        userRepository.save(user);
        log.info("[ELASTIC] User is updated in ES successful id = " + id);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
        log.info("[ELASTIC] User is deleted from ES successful id = " + id);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;

    }

    public List<User> findAll(Pageable pageable) {
        List<User> users = new ArrayList<>();
        userRepository.findAll(pageable).forEach(users::add);
        return users;

    }


    public List<User> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        //validation column name
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            Pageable pageable = pageableMapper.getPageable(searchCriteriaDto);
            return findAll(pageable);
        }
        searchCriteriaDto.getFilters().forEach(x -> SearchColumnUser.getByNameIgnoringCase(x.getColumn()));
        return userSearchRepository.findAllWithFilters(searchCriteriaDto);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public void createUserList(List<User> users) {
        userRepository.saveAll(users);
        log.info("[ELASTIC] List of users is saved successful");
    }
}

