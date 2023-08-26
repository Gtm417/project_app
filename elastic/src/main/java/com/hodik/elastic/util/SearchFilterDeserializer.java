package com.hodik.elastic.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hodik.elastic.dto.FilterDto;
import com.hodik.elastic.dto.Operation;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class SearchFilterDeserializer extends StdDeserializer<FilterDto> {

    protected SearchFilterDeserializer() {
        super(FilterDto.class);
    }

    public SearchFilterDeserializer(StdDeserializer<FilterDto> src) {
        super(src);
    }


    @Override
    public FilterDto deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        String column = node.get("column").asText();
        Operation operations = Operation.valueOf(node.get("operation").asText());
        Class<?> valueType = getValueType(node);
        List<Object> values = new ArrayList<>();

        JsonNode valuesNode = node.get("values");
        for (JsonNode valueNode : valuesNode) {
            if (valueType.isInstance(LocalDateTime.now())) {
                long timeStamp = valueNode.asLong();
                values.add(LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.systemDefault()));
                continue;
            }
            values.add(valueNode.asText());
        }

        return new FilterDto(column, operations, values, valueType);
    }

    private static Class<?> getValueType(JsonNode node) {
        JsonNode clazzNode = node.get("clazz");
        if (clazzNode == null || StringUtils.isBlank(clazzNode.asText())) {
            return String.class;
        }
        try {
            return Class.forName(clazzNode.asText());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot parse class type", e);
        }
    }
}
