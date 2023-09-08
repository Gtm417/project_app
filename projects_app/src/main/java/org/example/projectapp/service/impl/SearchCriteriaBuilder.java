package org.example.projectapp.service.impl;

import org.example.projectapp.controller.UsersController;
import org.example.projectapp.controller.dto.FilterDto;
import org.example.projectapp.model.Project;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.specification.GenericSpecification;
import org.example.projectapp.repository.specification.ProjectSpecs;
import org.example.projectapp.repository.specification.SpecificationBuilder;
import org.example.projectapp.repository.specification.UserSpecification;
import org.example.projectapp.service.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchCriteriaBuilder<T> {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final String SORT = "ASC";


    public Specification<User> buildUserSearchSpecification(List<FilterDto> filters) {
        SpecificationBuilder specificationsBuilder = new SpecificationBuilder();
        List<SearchCriteria> searchCriteriaFromFilters = getSearchCriteria(filters);
        return specificationsBuilder.with(searchCriteriaFromFilters).build(UserSpecification::new);
    }

    public <T> Specification<T> buildSearchSpecification(List<FilterDto> filters) {
        SpecificationBuilder specificationsBuilder = new SpecificationBuilder();
        List<SearchCriteria> searchCriteriaFromFilters = getSearchCriteria(filters);
        return specificationsBuilder.with(searchCriteriaFromFilters).build(GenericSpecification<T>::new);
    }

    public Specification<Project> buildSearchSpecificationWithPrivateProject(List<FilterDto> filters) {
        Specification<Project> genericSpec = buildSearchSpecification(filters);
        Specification.where(genericSpec).and(ProjectSpecs.getProjectPrivateSpec());
        return genericSpec.and(ProjectSpecs.getProjectPrivateSpec());
    }

    private List<SearchCriteria> getSearchCriteria(List<FilterDto> filters) {
        List<SearchCriteria> searchCriteriaFromFilters = mapFiltersToSearchCriteria(filters);
        logger.info("[SEARCH] Build specification with Search Criteria: {}", searchCriteriaFromFilters);
        return searchCriteriaFromFilters;
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
