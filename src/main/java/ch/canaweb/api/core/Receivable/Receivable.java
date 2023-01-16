package ch.canaweb.api.core.Receivable;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collectionName = "Receivable")
public class Receivable implements Serializable, Cloneable {
    @DocumentId
    private String receivableId;

    private Phase preliquidation;

    private Phase liquidation;

    private Phase ajuste;
}


