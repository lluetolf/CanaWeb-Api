package ch.canaweb.api.error;

import java.util.Date;

public class BaseHttpException extends AbstracHttpException {
    public BaseHttpException(Date timestamp, String message, String details) {
        super(timestamp, message, details);
    }

    public BaseHttpException(String message, String details) {
        super(message, details);
    }
}
