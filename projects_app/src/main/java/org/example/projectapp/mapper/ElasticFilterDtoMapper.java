package org.example.projectapp.mapper;


import org.example.projectapp.controller.dto.FilterDto;
import org.example.projectapp.controller.dto.SearchOperation;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ElasticFilterDtoMapper {

    public final ModelMapper modelMapper;

    public ElasticFilterDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ElasticFilterDto convertToProjectElasticFilterDto(FilterDto filterDto) {
        return ElasticFilterDto.builder()
                .column(filterDto.getName())
                .clazz(getClazz(filterDto))
                .values(filterDto.getValues())
                .operation(mapperOperation(filterDto.getOperation()))
                .orPredicate(filterDto.isOrPredicate())
                .build();
    }

    private Class<?> getClazz(FilterDto filterDto) {
        return filterDto.getDataType() == null ?
                String.class
                : filterDto.getDataType().getClazz();
    }

//    private FilterDto convertDataIfNeed(FilterDto filterDto) {
//        List<Object> values = filterDto.getValues();
//        if (getClazz(filterDto) == LocalDateTime.class) {
//
//            for (int i = 0; i < values.size(); i++) {
//                if (values.get(i).getClass().isInstance("String")) {
//                    LocalDateTime dateTime = LocalDateTime.parse(values.get(i).toString());
//                    Long epoch = dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
//                    values.set(i, epoch.toString());
//                }
//            }
//        }
//        filterDto.setValues(values);
//
//        return filterDto;
//    }

    private ElasticOperation mapperOperation(SearchOperation operation) {
        return modelMapper.map(operation, ElasticOperation.class);
    }
}

