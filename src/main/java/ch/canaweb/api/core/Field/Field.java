package ch.canaweb.api.core.Field;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Field {
    public String fieldId;
    public String name;
    public String owner;
    public double size;
    public double cultivatedArea;
    public LocalDate acquisitionDate;
    public String ingenioId;
    public LocalDate lastUpdated;
    public Date date;

    public Field() {
        this.date = new Date();
    }

    public Field(String fieldId, String name, String owner, double size, double cultivatedArea, LocalDate acquisitionDate, String ingenioId, LocalDate lastUpdated) {
        this.fieldId = fieldId;
        this.name = name;
        this.owner = owner;
        this.size = size;
        this.cultivatedArea = cultivatedArea;
        this.acquisitionDate = acquisitionDate;
        this.ingenioId = ingenioId;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return fieldId == field.fieldId && Double.compare(field.size, size) == 0 && Double.compare(field.cultivatedArea, cultivatedArea) == 0 && name.equals(field.name) && owner.equals(field.owner) && acquisitionDate.equals(field.acquisitionDate) && ingenioId.equals(field.ingenioId) && lastUpdated.equals(field.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId, name, owner, size, cultivatedArea, acquisitionDate, ingenioId, lastUpdated);
    }
}
