package org.example.projectapp.repository.specification;

import org.example.projectapp.model.User;
import org.example.projectapp.service.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserSpecification implements Specification<User> {

    private final SearchCriteria criteria;

    public UserSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate
            (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {


        String key = criteria.getKey();
        String operation = criteria.getOperation();
        List<Object> values = criteria.getValues();
        Object value = values.get(0);
        if (operation.equalsIgnoreCase("more")) {
            return builder.greaterThanOrEqualTo(
                    root.get(key), value.toString());
        } else if (operation.equalsIgnoreCase("less")) {
            return builder.lessThanOrEqualTo(
                    root.get(key), value.toString());
        } else if (operation.equalsIgnoreCase("like")) {
            if (root.get(key).getJavaType() == String.class) {
                return builder.like(
                        root.get(key), "%" + value + "%");
            } else {
                return builder.equal(root.get(key).as(String.class), value);
            }
        } else if (operation.equalsIgnoreCase("equal")) {
            return builder.equal(root.get(key).as(String.class), value);
        } else if (operation.equalsIgnoreCase("notEqual")) {
            return builder.notEqual(root.get(key).as(String.class), value);
        } else if (operation.equalsIgnoreCase("in")) {
            return root.get(key).as(String.class).in(values);
        }
        return null;
    }
}