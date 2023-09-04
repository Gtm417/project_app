package org.example.projectapp.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import org.example.projectapp.TestUtils;
import org.example.projectapp.mapper.dto.ElasticFilterDto;
import org.example.projectapp.mapper.dto.ElasticOperation;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SearchFilterSerializerTest {
    private final Gson gson = new Gson();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final ElasticOperation OPERATION = ElasticOperation.EQUAL;
    private static final Class<?> CLASS = LocalDateTime.class;
    private static final List<Object> VALUES_HUMAN_DATA_TIME = List.of("2023-08-27T12:34");
    private static final List<Object> VALUES_TIME_STAMP = List.of(1693128840);
    private static final String COLUMN = "createDate";
    private static final boolean OR_PREDICATE_FALSE = false;
    private static final boolean OR_PREDICATE_TRUE = true;


    private final String expectedJson = TestUtils.readResource("expected.filter.project.dto.local.date.time.json");
    private final ElasticFilterDto filterDtoClassNull =
            gson.fromJson(TestUtils.readResource("filter.project.dto.class.null.json"), ElasticFilterDto.class);
    private final String expectedJsonClassNull =
            TestUtils.readResource("expected.filter.project.dto.class.null.json");
    private final String expectedProjectDtoNullDate =
            TestUtils.readResource("expected.filter.project.dto.data.null.json");
    private final String expectedProjectDtoEmptyDate =
            TestUtils.readResource("expected.filter.project.dto.data.empty.json");
    private final String expectedProjectDtoEmptyDateOrPredicateTrue =
            TestUtils.readResource(("expected.filter.project.dto.data.empty.or.predicate.true.json"));


    private final SimpleModule module = new SimpleModule();

    @Test
    void testSerializerHumanDateTime() throws IOException, JSONException {
        //given
        ElasticFilterDto dto = new ElasticFilterDto(COLUMN, OPERATION, VALUES_HUMAN_DATA_TIME, CLASS, OR_PREDICATE_FALSE);
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
        ElasticFilterDto dto = new ElasticFilterDto(COLUMN, OPERATION, VALUES_TIME_STAMP, CLASS, OR_PREDICATE_FALSE);
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

    @Test
    void testSerializerNullDate() throws IOException, JSONException {
        //given
        ElasticFilterDto dto = ElasticFilterDto.builder()
                .column(COLUMN)
                .operation(OPERATION)
                .values(null)
                .clazz(CLASS)
                .orPredicate(OR_PREDICATE_FALSE).build();
        //when
        module.addSerializer(ElasticFilterDto.class, new SearchFilterSerializer());
        objectMapper.registerModule(module);
        //then
        String actualJson = objectMapper.writeValueAsString(dto);

        JSONAssert.assertEquals(expectedProjectDtoNullDate, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    void testSerializerEmptyDate() throws IOException, JSONException {
        //given
        ElasticFilterDto dto = ElasticFilterDto.builder()
                .column(COLUMN)
                .operation(OPERATION)
                .values(Collections.emptyList())
                .clazz(CLASS)
                .orPredicate(OR_PREDICATE_FALSE).build();
        //when
        module.addSerializer(ElasticFilterDto.class, new SearchFilterSerializer());
        objectMapper.registerModule(module);
        //then
        String actualJson = objectMapper.writeValueAsString(dto);

        JSONAssert.assertEquals(expectedProjectDtoEmptyDate, actualJson, JSONCompareMode.STRICT);
    }

    @Test
    void testSerializerEmptyDateOrPredicateTrue() throws IOException, JSONException {
        //given
        ElasticFilterDto dto = ElasticFilterDto.builder()
                .column(COLUMN)
                .operation(OPERATION)
                .values(Collections.emptyList())
                .clazz(CLASS)
                .orPredicate(OR_PREDICATE_TRUE).build();
        //when
        module.addSerializer(ElasticFilterDto.class, new SearchFilterSerializer());
        objectMapper.registerModule(module);
        //then
        String actualJson = objectMapper.writeValueAsString(dto);

        JSONAssert.assertEquals(expectedProjectDtoEmptyDateOrPredicateTrue, actualJson, JSONCompareMode.STRICT);
    }

}
