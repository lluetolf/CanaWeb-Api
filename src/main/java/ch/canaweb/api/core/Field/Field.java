package ch.canaweb.api.core.Field;

import ch.canaweb.api.persistence.helper.FireStoreDeserializer;
import ch.canaweb.api.persistence.helper.FireStoreSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gcp.data.firestore.Document;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collectionName = "Field")
public class Field implements Serializable {
    @DocumentId
    private String id;

    private String fieldId;

    private String name;

    private String owner;

    private double size;

    private double cultivatedArea;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    private Timestamp acquisitionDate;

    private String ingenioId;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    private Timestamp lastUpdated;

}
