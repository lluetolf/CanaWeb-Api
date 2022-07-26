package ch.canaweb.api.error;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstracHttpException extends RuntimeException {
    private Date timestamp = null;
    private String message = "";
    private String details = "";

    public AbstracHttpException(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public AbstracHttpException(String message, String details) {
        super();
        this.timestamp = new Date();
        this.message = message;
        this.details = details;
    }

    public AbstracHttpException() {
        super();
        this.timestamp = new Date();
        this.message = "N/A";
        this.details = "N/A";
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public Map<String, String> getMessageBody() {
        return new HashMap<>(
                Map.of("date", this.timestamp == null ? "UNKNOWN" : this.timestamp.toString(),
                        "message", this.message,
                        "details", this.details,
                        "type", this.getClass().getSimpleName()));
    }
}
