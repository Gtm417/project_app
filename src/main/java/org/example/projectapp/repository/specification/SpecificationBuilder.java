package org.example.projectapp.repository.specification;

import org.example.projectapp.service.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SpecificationBuilder {

    private final List<SearchCriteria> params;


    public SpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public SpecificationBuilder with(String key, String operation, List<Object> values) {
        params.add(new SearchCriteria(key, operation, values));
        return this;
    }

    public SpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public SpecificationBuilder with(List<SearchCriteria> searchCriteria) {
        params.addAll(searchCriteria);
        return this;
    }

    public <T> Specification<T> build(Function<SearchCriteria, Specification<T>> mappingToSpecification) {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>();

        params.forEach(p -> specs.add(mappingToSpecification.apply(p)));

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            Specification<T> specification = specs.get(i);
            if (specification == null) {
                continue;
            }
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result).or(specification)
                    : Specification.where(result).and(specification);
        }
        return result;
    }

}
