package org.example.projectapp.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.data.elasticsearch.core.ResourceUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SearchFilterSerializerTest {
    Gson gson = new Gson();
    private static final ElasticOperation operation = ElasticOperation.EQUAL;
    private static final Class<?> clazz = LocalDateTime.class;
    private static final List<Object> valuesHumanDateTime = List.of("2023-08-27T12:34");
    private static final List<Object> valuesTimeStamp = List.of(1693128840);
    private static final String column = "createDate";
    private static final boolean orPredicate = false;
    private final String expectedJson = ResourceUtil.readFileFromClasspath("expected.filter.project.dto.local.date.time.json");
    private final ElasticFilterDto filterDtoClassNull =
            gson.fromJson(ResourceUtil.readFileFromClasspath("filter.project.dto.class.null.json"), ElasticFilterDto.class);
    private final String expectedJsonClassNull =
            ResourceUtil.readFileFromClasspath("expected.filter.project.dto.class.null.json");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleModule module = new SimpleModule();

    @Test
    void testSerializerHumanDateTime() throws IOException, JSONException {
        //given
        ElasticFilterDto dto = new ElasticFilterDto(column, operation, valuesHumanDateTime, clazz, orPredicate);
        //when
        module.addSerializer(ElasticFilterDto.class, new SearchFilterSerializer());
        objectMapper.registerModule(module);
        //then
        String actualJson = objectMapper.writeValueAsString(dto);

        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    void testSerializerTimeStamp() throws IOException, JSONException {
        //given
        ElasticFilterDto dto = new ElasticFilterDto(column, operation, valuesTimeStamp, clazz, orPredicate);
        //when
        module.addSerializer(ElasticFilterDto.class, new SearchFilterSerializer());
        objectMapper.registerModule(module);
        //then
        String actualJson = objectMapper.writeValueAsString(dto);

        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    void shouldAddClasString() throws IOException, JSONException {

        //given
        ElasticFilterDto dto = filterDtoClassNull;
        //when
        module.addSerializer(ElasticFilterDto.class, new SearchFilterSerializer());
        objectMapper.registerModule(module);
        //then
        String actualJson = objectMapper.writeValueAsString(dto);

        JSONAssert.assertEquals(expectedJsonClassNull, actualJson, JSONCompareMode.STRICT);
    }
}
