package ch.canaweb.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // I can return a response entity or for instance just a plain string
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> exceptions(Throwable e) {
        this.logger.error("Hit massive failure.");
        return  Map.of(
                "date", (new Date()).toString(),
                "message", e.getMessage(),
                "details", ""
        );
    }

    @ExceptionHandler(DuplicateHttpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> duplicateHttpException(AbstracHttpException e) {
        return e.getMessageBody();
    }

    @ExceptionHandler(BaseHttpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> baseHttpException(AbstracHttpException e) {
        return e.getMessageBody();
    }

    @ExceptionHandler(EntityDoesNotExistHttpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> edneHttpException(AbstracHttpException e) {
        return e.getMessageBody();
    }
}