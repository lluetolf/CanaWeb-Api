package ch.canaweb.api.core.Field;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.Timestamp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FireStoreSerializer extends JsonSerializer<Timestamp> {
    @Override
    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        LocalDateTime date = LocalDateTime.ofEpochSecond(value.getSeconds(), value.getNanos(), ZoneOffset.UTC);
        String w = date.toString();
        gen.writeString(w);
    }
}
