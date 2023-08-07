package org.example.projectapp.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CustomLocalDateTimeDeSerializer extends StdDeserializer<LocalDateTime> {
    public CustomLocalDateTimeDeSerializer() {
        super(LocalDateTime.class);
    }

    public CustomLocalDateTimeDeSerializer(StdDeserializer<LocalDateTime> src) {
        super(src);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        long timeStamp = jsonParser.getLongValue();
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneId.systemDefault());
    }
}
