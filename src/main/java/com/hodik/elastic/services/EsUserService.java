package com.hodik.elastic.services;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchFilter;
import com.hodik.elastic.exceptions.EntityAlreadyExitsException;
import com.hodik.elastic.model.User;
import com.hodik.elastic.repositories.UserRepository;
import com.hodik.elastic.repositories.UserSearchRepository;
import com.hodik.elastic.util.SearchColumnUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2

public class EsUserService {
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;

    @Autowired
    public EsUserService(UserRepository userRepository, UserSearchRepository userSearchRepository) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
    }

    public void createUser(User user) throws EntityAlreadyExitsException {
        long id = user.getId();
        if (userRepository.findById(id).isPresent()) {
            throw new EntityAlreadyExitsException("User already exits id= " + id);
        }
        userRepository.save(user);
        log.info("User is saved to ES successful id = " + id);
    }

    public void update(long id, User user) {
        user.setId(id);
        userRepository.save(user);
        log.info("User is updated in ES successful id = " + id);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
        log.info("User is deleted from ES successful id = " + id);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;

    }


    public List<User> findAllWithFilters(SearchCriteriaDto searchCriteriaDto) {
        //validation column name
        searchCriteriaDto.getFilters().stream().forEach(x -> SearchColumnUser.getByNameIgnoringCase(x.getColumn()));
        List<SearchFilter> filters = searchCriteriaDto.getFilters();
        if (filters == null) {
            return findAll();
        }
        return userSearchRepository.findAllWithFilters(searchCriteriaDto);
    }


    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}

