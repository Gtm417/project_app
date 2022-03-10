package org.example.projectapp.repository.specification;

import org.example.projectapp.model.User;
import org.example.projectapp.service.SearchCriteria;

import javax.persistence.criteria.*;
import java.util.List;

public class UserSpecification extends GenericSpecification<User> {

    public UserSpecification(SearchCriteria criteria) {
        super(criteria);
    }

    @Override
    public Predicate toPredicate
            (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        SearchCriteria criteria = getCriteria();
        String key = criteria.getKey();
        List<Object> values = criteria.getValues();
        if (key.equalsIgnoreCase("skills")) {
            query.distinct(true);
            Join<Object, Object> join =
                    (Join<Object, Object>) root.fetch("skills").fetch("skill");

            return builder.or(values.stream()
                    .map(v -> builder.like(join.get("name"), "%" + v + "%"))
                    .toArray(Predicate[]::new)
            );
        }
        return super.toPredicate(root, query, builder);
    }
}