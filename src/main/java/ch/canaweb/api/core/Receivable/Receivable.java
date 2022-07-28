package ch.canaweb.api.core.Receivable;

import java.time.LocalDate;

public class Receivable {
    private int receivableId;
    private String zafra;
    private LocalDate transactionDate;
    private String documentId;
    private String fieldName;
    private LocalDate lastUpdated;

    public Receivable() {
    }

    public Receivable(int receivableId, String zafra, LocalDate transactionDate, String documentId, String fieldName, LocalDate lastUpdated) {
        this.receivableId = receivableId;
        this.zafra = zafra;
        this.transactionDate = transactionDate;
        this.documentId = documentId;
        this.fieldName = fieldName;
        this.lastUpdated = lastUpdated;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getReceivableId() {
        return receivableId;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setReceivableId(int receivableId) {
        this.receivableId = receivableId;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getZafra() {
        return zafra;
    }

    public void setZafra(String zafra) {
        this.zafra = zafra;
    }
}
