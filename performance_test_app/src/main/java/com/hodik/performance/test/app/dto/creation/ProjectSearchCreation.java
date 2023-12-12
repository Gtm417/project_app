package com.hodik.performance.test.app.dto.creation;

import com.hodik.performance.test.app.dto.*;
import com.hodik.performance.test.app.model.creation.CreateRandomProjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ProjectSearchCreation {
    Random random = new Random();
    @Autowired
    private final CreateRandomProjects createRandomProjects;
    private String search;


    public ProjectSearchCreation(CreateRandomProjects createRandomProjects) {
        this.createRandomProjects = createRandomProjects;
    }

    public SearchDto createProjectSearchDto() {
        List<SearchFilterDto> filterDtoList = new ArrayList<>();
        search = createRandomProjects.getCategory();

        filterDtoList.add(createRandomStringFilterDto("category"));
        filterDtoList.add(createRandomStringFilterDto("description"));
//        filterDtoList.add(createRandomBooleanFilterDto("isPrivate"));
//        filterDtoList.add(createRandomBooleanFilterDto("isCommercial"));
//        filterDtoList.add(createRandomDateFilterDto("createdDate"));

        List<SearchSort> searchSorts = List.of(SearchSort.builder()
                .column("name")
                .ascending(true)
                .build());

        return SearchDto.builder()
                .search(createRandomProjects.getCategory())
                .filters(filterDtoList)
                .page(0)
                .size(100)
                .sorts(searchSorts)
                .build();
    }


    private SearchFilterDto createRandomDateFilterDto(String name) {
        return SearchFilterDto.builder()
                .name(name)
                .dataType(DataType.DATE_TIME)
                .operation(getRandomDateOperation())
                .values(List.of(createRandomProjects.generateRandomDate()))
                .orPredicate(true)
                .build();
    }

    private SearchOperation getRandomDateOperation() {
        String operations[] = {"LESS", "GREATER"};
        return SearchOperation.valueOf(operations[random.nextInt(operations.length)]);
    }


    private SearchFilterDto createRandomBooleanFilterDto(String name) {
        return SearchFilterDto.builder()
                .name(name)
                .dataType(DataType.STRING)
                .operation(SearchOperation.EQUAL)
                .values(List.of(random.nextBoolean()))
                .orPredicate(true)
                .build();
    }

    private SearchFilterDto createRandomStringFilterDto(String name) {
        return SearchFilterDto.builder()
                .name(name)
                .dataType(DataType.STRING)
                .operation(getRandomStringOperation())
                .values(getStringValues())
                .orPredicate(true)
                .build();

    }

    private List<Object> getStringValues() {
        String str = createRandomProjects.getDescription();
        String[] wordsArray = str.split("\\s+");
        return List.of(wordsArray[random.nextInt(wordsArray.length)]);
    }

    private SearchOperation getRandomStringOperation() {
        String operations[] = {"EQUAL", "NOT_EQUAL", "LIKE"};
        return SearchOperation.valueOf(operations[random.nextInt(operations.length)]);
    }
}
