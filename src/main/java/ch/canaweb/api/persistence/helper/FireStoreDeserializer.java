package ch.canaweb.api.persistence.helper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.cloud.Timestamp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FireStoreDeserializer extends StdDeserializer<Timestamp> {
    protected FireStoreDeserializer() {
        this(null);
    }

    protected FireStoreDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);

        LocalDateTime dt = null;
        try {
            dt = LocalDateTime.parse(node.asText(),  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (DateTimeParseException e) {
            // if there are 0 seconds, the pattern is HH:mm and not HH:mm:ss, this is a workaround to account for that case.
            dt = LocalDateTime.parse(node.asText(),  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        }
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(dt);
        return Timestamp.of(ts);
    }
}
