package com.hodik.elastic.mappers;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PageableMapper {

    public Pageable getPageable(SearchCriteriaDto searchCriteriaDto) {
        int page = searchCriteriaDto.getPage();
        int size = searchCriteriaDto.getSize();
        List<Sort.Order> orders = mapToSortOrder(searchCriteriaDto.getSorts());
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }

    private List<Sort.Order> mapToSortOrder(List<SearchSort> searchSorts) {
        return searchSorts.stream()
                .map(x -> new Order(getDirection(x), x.getColumn()))
                .collect(Collectors.toList());
    }

    private Sort.Direction getDirection(SearchSort x) {
        return x.getAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
}
