package org.example.projectapp.repository.specification;

import org.example.projectapp.model.User;
import org.example.projectapp.service.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public UserSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public UserSpecificationsBuilder with(String key, String operation, List<Object> values) {
        params.add(new SearchCriteria(key, operation, values));
        return this;
    }

    public UserSpecificationsBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public UserSpecificationsBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public Specification<User> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<User>> specs = params.stream()
                .map(UserSpecification::new)
                .collect(Collectors.toList());

        Specification<User> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            Specification<User> specification = specs.get(i);
            if (specification == null) {
                continue;
            }
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specification)
                    : Specification.where(result)
                    .and(specification);
        }
        return result;
    }
}
