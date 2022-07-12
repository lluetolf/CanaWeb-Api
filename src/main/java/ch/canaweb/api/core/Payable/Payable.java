package ch.canaweb.api.core.Payable;

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
@Document(collectionName = "Payable")
public class Payable implements Serializable {
    @DocumentId
    private int payableId;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    private Timestamp transactionDate;

    private double pricePerUnit;

    private double quantity;

    private int documentId;

    private int fieldId;

    private String category;

    private String subCategory;

    private String comment;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    private Timestamp lastUpdated;
}

