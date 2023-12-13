package com.hodik.performance.test.app.dto.creation;

import com.hodik.performance.test.app.dto.SearchDto;
import com.hodik.performance.test.app.dto.SearchFilterDto;
import com.hodik.performance.test.app.dto.SearchSort;
import com.hodik.performance.test.app.model.creation.CreateRandomVacancies;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VacanciesSearchCreation {
    private final CreateRandomVacancies createRandomVacancies;


    public VacanciesSearchCreation(CreateRandomVacancies createRandomVacancies) {
        this.createRandomVacancies = createRandomVacancies;
    }

    public SearchDto createProjectSearchDto() {
        List<SearchFilterDto> filterDtoList = new ArrayList<>();
        String search = createRandomVacancies.getSearch();


        List<SearchSort> searchSorts = List.of(SearchSort.builder()
                .column("id")
                .ascending(true)
                .build());

        return SearchDto.builder()
                .search(search)
                .filters(filterDtoList)
                .page(0)
                .size(100)
                .sorts(searchSorts)
                .build();
    }


}