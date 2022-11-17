package ch.canaweb.api.core.Receivable;

import ch.canaweb.api.core.Payable.Payable;
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

    private Phase preliquidiation;

    private Phase liquidiation;

    private Phase ajuste;
}


