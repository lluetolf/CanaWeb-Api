package ch.canaweb.api.error;

public class EntityDoesNotExistHttpException extends AbstracHttpException {

    public EntityDoesNotExistHttpException(String message, String details) {
        super(message, details);
    }
}
