package ru.practicum.shareit.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.practicum.shareit.ShareItServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomDateTimeSerializer extends StdSerializer<LocalDateTime> {

    @SuppressWarnings("unused")
    public CustomDateTimeSerializer() {
        this(null);
    }

    public CustomDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        jsonGenerator.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime.truncatedTo(ChronoUnit.SECONDS)));
        jsonGenerator.writeString(ShareItServer.FORMATTER.format(localDateTime));
    }
}
