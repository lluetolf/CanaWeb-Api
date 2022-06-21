package ch.canaweb.api.core.Field;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gcp.data.firestore.Document;


import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collectionName = "Field")
public class Field {

    @DocumentId
    private String id;

    private String fieldId;

    private String name;

    private String owner;

    private double size;

    private double cultivatedArea;

    private LocalDate acquisitionDate;

    private String ingenioId;

    private LocalDate lastUpdated;

    private Date date;
}
