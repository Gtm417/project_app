package com.hodik.elastic.mapper;


import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.Operation;
import com.hodik.elastic.dto.SearchFilterDto;
import com.hodik.elastic.dto.SearchOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FilterDtoMapper {

    public final ModelMapper modelMapper;

    public FilterDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public FilterDto convertToFilterDto(SearchFilterDto filterDto) {
        return FilterDto.builder()
                .column(filterDto.getName())
                .clazz(getClazz(filterDto))
                .values(filterDto.getValues())
                .operation(mapperOperation(filterDto.getOperation()))
                .orPredicate(filterDto.isOrPredicate())
                .build();
    }

    private Class<?> getClazz(SearchFilterDto filterDto) {
        return filterDto.getDataType() == null ?
                String.class
                : filterDto.getDataType().getClazz();
    }


    private Operation mapperOperation(SearchOperation operation) {
        return modelMapper.map(operation, Operation.class);
    }
}

