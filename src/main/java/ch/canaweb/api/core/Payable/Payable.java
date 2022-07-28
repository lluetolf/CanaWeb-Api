package ch.canaweb.api.core.Payable;

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
@Document(collectionName = "Payable")
public class Payable implements Serializable, Cloneable {
    @DocumentId
    private String payableId;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    @NotNull(message = "TransactionDate cannot be null.")
    private Timestamp transactionDate;

    @NotNull(message = "PPU cannot be null.")
    private double pricePerUnit;

    @NotNull(message = "Quantity cannot be null.")
    private double quantity;

    private int documentId;

    private List<String> fieldNames;

    @NotNull(message = "Category cannot be null.")
    private String category;

    private String subCategory;

    private String comment;

    @JsonSerialize(using = FireStoreSerializer.class)
    @JsonDeserialize(using = FireStoreDeserializer.class)
    private Timestamp lastUpdated;

    public Payable clone() throws CloneNotSupportedException {
        return (Payable) super.clone();
    }
}

