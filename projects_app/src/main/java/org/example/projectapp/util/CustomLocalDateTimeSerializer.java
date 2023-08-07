package org.example.projectapp.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    public CustomLocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }


    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Long epoch = value.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        jsonGenerator.writeNumber(epoch.toString());
    }
}
