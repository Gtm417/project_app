package com.hodik.elastic.mappers;

import com.hodik.elastic.dto.SearchSort;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SortOrdersMapper {
    private final List<SearchSort> searchSorts;


    public SortOrdersMapper(List<SearchSort> searchSorts) {
        this.searchSorts = searchSorts;
    }
    public List<Sort.Order> mapToSortOrder (List<SearchSort> searchSorts){
        List<Sort.Order> orders= new ArrayList<>();
        Sort.Direction direction;
        for (SearchSort s: searchSorts){

            if (s.getAscending()) {
                direction = Sort.Direction.ASC;
            } else {
                direction = Sort.Direction.DESC;
            }
            String column = s.getColumn().toString();
            Sort.Order order= new Order(direction,column);
            orders.add(order);
        }
        return orders;
    }
}
