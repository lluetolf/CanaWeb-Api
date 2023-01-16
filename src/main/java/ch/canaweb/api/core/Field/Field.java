package ch.canaweb.api.core.Field;

import ch.canaweb.api.persistence.helper.FireStoreDeserializer;
import ch.canaweb.api.persistence.helper.FireStoreSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collectionName = "Field")
public class Field implements Serializable {
    @DocumentId
    private String id;

    @NotNull(message = "Name cannot be null.")
    private String name;

    @NotNull(message = "Owner cannot be null.")
    private String owner;

    @NotNull(message = "Size cannot be null.")
    private double size;

    @NotNull(message = "CultivatedArea cannot be null.")
    private double cultivatedArea;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    @NotNull(message = "AcquisitionDate cannot be null.")
    private Timestamp acquisitionDate;

    private List<String> ingenioId;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    private Timestamp lastUpdated;

}
