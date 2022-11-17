package ch.canaweb.api.core.Receivable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Phase {
    private String ingenioId;

    private double pricePerUnit;

    private double tons;

    private double total;

    public Phase clone() throws CloneNotSupportedException {
        return (Phase) super.clone();
    }
}
