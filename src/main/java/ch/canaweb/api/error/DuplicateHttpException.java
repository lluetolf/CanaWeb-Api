package ch.canaweb.api.error;

import java.util.Date;

public class DuplicateHttpException extends AbstracHttpException {
    public DuplicateHttpException(Date timestamp, String message, String details) {
        super(timestamp, message, details);
    }

    public DuplicateHttpException(String message, String details) {
        super(message, details);
    }
}
