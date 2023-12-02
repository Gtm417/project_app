package com.hodik.elastic.mapper;

import com.hodik.elastic.dto.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("searchUserDtoMapper")
public class SearchUserDtoMapper implements SearchCriteriaDtoMapper {
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String CV = "cv";
    public static final String SKILLS_SKILL_NAME = "skills.skillName";
    public static final String DESCRIPTION = "description";
    private final FilterDtoMapper filterDtoMapper;

    public SearchUserDtoMapper(FilterDtoMapper filterDtoMapper) {
        this.filterDtoMapper = filterDtoMapper;
    }

    @Override
    public SearchCriteriaDto convertToSearchCriteriaDto(SearchDto searchDto) {
        List<FilterDto> filterDtoList = getElasticFilterDtos(searchDto, filterDtoMapper);
        addSkillsToFilterDtoList(searchDto, filterDtoList);
        addSearchToFilterDtoList(searchDto, filterDtoList);
        return getElasticCriteriaDto(searchDto, filterDtoList);
    }


    private void addSkillsToFilterDtoList(SearchDto searchDto, List<FilterDto> filterDtoList) {
        SearchUserDto searchUserDto = (SearchUserDto) searchDto;
        List<String> skills = searchUserDto.getSkills();
        boolean orPredicate = true;
        if (CollectionUtils.isNotEmpty(skills)) {
            for (String skillName : skills) {
                FilterDto skillFilter =
                        getFilter(skillName, SKILLS_SKILL_NAME, Operation.LIKE, orPredicate);
                filterDtoList.add(skillFilter);
            }
        }
    }


    private void addSearchToFilterDtoList(SearchDto searchDto, List<FilterDto> filterDtoList) {
        String search = searchDto.getSearch();
        if (search != null && !search.isBlank()) {
            boolean orPredicate = true;

            filterDtoList.add(getFilter(search, EMAIL, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, FIRST_NAME, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, LAST_NAME, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, CV, Operation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, DESCRIPTION, Operation.FULL_TEXT, orPredicate));
        }
    }


}


