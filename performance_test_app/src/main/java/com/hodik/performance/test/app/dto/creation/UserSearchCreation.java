package com.hodik.performance.test.app.dto.creation;

import com.hodik.performance.test.app.dto.*;
import com.hodik.performance.test.app.model.creation.CreateRandomUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class UserSearchCreation {
    Random random = new Random();
    @Autowired
    private final CreateRandomUsers createRandomUsers;

    public UserSearchCreation(CreateRandomUsers createRandomUsers) {
        this.createRandomUsers = createRandomUsers;
    }

    public SearchDto createProjectSearchDto() {
        List<SearchFilterDto> filterDtoList = new ArrayList<>();
        String search = getSearch();

        filterDtoList.add(createRandomStringFilterDto("firstName", createRandomUsers.getFirstName()));
        filterDtoList.add(createRandomStringFilterDto("lastName", createRandomUsers.getLastName()));


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

    private String getSearch() {
        String str = createRandomUsers.getDescription();
        String[] wordsArray = str.split("\\s+");
        return wordsArray[random.nextInt(wordsArray.length)];
    }

    private SearchFilterDto createRandomStringFilterDto(String name, String value) {
        return SearchFilterDto.builder()
                .name(name)
                .dataType(DataType.STRING)
                .operation(getRandomStringOperation())
                .values(List.of(value))
                .orPredicate(true)
                .build();

    }

    private SearchOperation getRandomStringOperation() {
        String[] operations = {"EQUAL", "NOT_EQUAL", "LIKE"};
        return SearchOperation.valueOf(operations[random.nextInt(operations.length)]);
    }
}
