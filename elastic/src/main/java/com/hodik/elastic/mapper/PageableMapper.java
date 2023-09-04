package com.hodik.elastic.mapper;

import com.hodik.elastic.dto.SearchCriteriaDto;
import com.hodik.elastic.dto.SearchSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PageableMapper {

    private static final int PAGE = 0;
    private static final int SIZE = 10;

    public Pageable getPageable(SearchCriteriaDto searchDto) {
        int page = getPage(searchDto);
        int size = size(searchDto);
        List<Sort.Order> orders = mapToSortOrder(searchDto.getSorts());
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }

    private Integer size(SearchCriteriaDto searchDto) {
        int size = searchDto.getSize();
        return size <= 0
                ? SIZE
                : size;
    }

    private Integer getPage(SearchCriteriaDto searchDto) {
        int page = searchDto.getPage();
        return page < 0
                ? PAGE
                : page;
    }

    private List<Sort.Order> mapToSortOrder(List<SearchSort> searchSorts) {
        searchSorts = Objects.requireNonNullElse(searchSorts, Collections.emptyList());
        return searchSorts.stream()
                .map(x -> new Sort.Order(getDirection(x), x.getColumn()))
                .collect(Collectors.toList());
    }

    private Sort.Direction getDirection(SearchSort x) {
        return x.getAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
}
