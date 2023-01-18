package ch.canaweb.api.core.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngenioId {
    private String ingenioId;

    private double size;

    public IngenioId clone() throws CloneNotSupportedException {
        return (IngenioId) super.clone();
    }
}
