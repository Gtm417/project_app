package org.example.projectapp.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.example.projectapp.mapper.dto.ElasticFilterDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class SearchFilterSerializer extends StdSerializer<ElasticFilterDto> {
    protected SearchFilterSerializer(Class<ElasticFilterDto> t) {
        super(t);
    }

    protected SearchFilterSerializer() {
        super(ElasticFilterDto.class);
    }

    @Override
    public void serialize(ElasticFilterDto elasticFilterDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("column", elasticFilterDto.getColumn());
        jsonGenerator.writeStringField("operation", elasticFilterDto.getOperation().toString());
        jsonGenerator.writeStringField("orPredicate", String.valueOf(elasticFilterDto.isOrPredicate()));
        Class<?> clazz = elasticFilterDto.getClazz();
        if (clazz != null) {
            jsonGenerator.writeStringField("clazz", clazz.getName());
        } else {
            jsonGenerator.writeObjectField("clazz", String.class);
        }

//        // Check if the class is LocalDateTime

        if (clazz != null && LocalDateTime.class.isAssignableFrom(clazz)) {
            List<Object> values = elasticFilterDto.getValues();

            jsonGenerator.writeFieldName("values");
            jsonGenerator.writeStartArray();

            for (Object value : values) {
                if (value instanceof String) {
                    LocalDateTime dateTime = LocalDateTime.parse((String) value);
                    long epoch = dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
                    jsonGenerator.writeNumber(epoch);
                } else {
                    // Handle other value types if needed
                    // For now, we'll just write the value as is
                    jsonGenerator.writeObject(value);
                }
            }

            jsonGenerator.writeEndArray();
        } else {
            // If it's not LocalDateTime, write the values as is
            jsonGenerator.writeArrayFieldStart("values");
            for (Object value : elasticFilterDto.getValues()) {
                jsonGenerator.writeObject(value);
            }
            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }
}

