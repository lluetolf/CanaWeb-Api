package ch.canaweb.api.core.Payable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTotal {
    private double total;

    private int month;

    private int year;
}
