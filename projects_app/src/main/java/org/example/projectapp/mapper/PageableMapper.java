package org.example.projectapp.mapper;


import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.controller.dto.SearchSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PageableMapper {
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    public Pageable getPageable(SearchDto searchDto) {
        int page = searchDto.getPage() == null || searchDto.getPage() < 0
                ? PAGE
                : searchDto.getPage();

        int size = searchDto.getSize() == null || searchDto.getSize() < 0
                ? SIZE
                : searchDto.getSize();
        List<Sort.Order> orders = mapToSortOrder(searchDto.getSorts());
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, size, sort);
    }

    private List<Sort.Order> mapToSortOrder(List<SearchSort> searchSorts) {
        return searchSorts.stream()
                .map(x -> new Sort.Order(getDirection(x), x.getColumn()))
                .collect(Collectors.toList());
    }

    private Sort.Direction getDirection(SearchSort x) {
        return x.getAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
}
