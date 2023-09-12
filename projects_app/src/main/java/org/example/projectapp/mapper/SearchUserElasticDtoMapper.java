package org.example.projectapp.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.example.projectapp.controller.dto.SearchDto;
import org.example.projectapp.controller.dto.SearchUserDto;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.example.projectapp.mapper.dto.SearchElasticCriteriaDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchUserElasticDtoMapper implements SearchElasticCriteriaDtoMapper {
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String CV = "cv";
    public static final String SKILLS_SKILL_NAME = "skills.skillName";
    public static final String DESCRIPTION = "description";
    private final ElasticFilterDtoMapper filterDtoMapper;

    public SearchUserElasticDtoMapper(ElasticFilterDtoMapper filterDtoMapper) {
        this.filterDtoMapper = filterDtoMapper;
    }

    @Override
    public SearchElasticCriteriaDto convertToSearchElasticCriteriaDto(SearchDto searchDto) {
        List<ElasticFilterDto> filterDtoList = getElasticFilterDtos(searchDto, filterDtoMapper);
        addSkillsToFilterDtoList(searchDto, filterDtoList);
        addSearchToFilterDtoList(searchDto, filterDtoList);
        return getElasticCriteriaDto(searchDto, filterDtoList);
    }


    private void addSkillsToFilterDtoList(SearchDto searchDto, List<ElasticFilterDto> filterDtoList) {
        SearchUserDto searchUserDto = (SearchUserDto) searchDto;
        List<String> skills = searchUserDto.getSkills();
        boolean orPredicate = true;
        if (CollectionUtils.isNotEmpty(skills)) {
            for (String skillName : skills) {
                ElasticFilterDto skillFilter =
                        getFilter(skillName, SKILLS_SKILL_NAME, ElasticOperation.LIKE, orPredicate);
                filterDtoList.add(skillFilter);
            }
        }
    }


    private void addSearchToFilterDtoList(SearchDto searchDto, List<ElasticFilterDto> filterDtoList) {
        String search = searchDto.getSearch();
        if (search != null && !search.isBlank()) {
            boolean orPredicate = true;

            filterDtoList.add(getFilter(search, EMAIL, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, FIRST_NAME, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, LAST_NAME, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, CV, ElasticOperation.FULL_TEXT, orPredicate));
            filterDtoList.add(getFilter(search, DESCRIPTION, ElasticOperation.FULL_TEXT, orPredicate));
        }
    }
}


