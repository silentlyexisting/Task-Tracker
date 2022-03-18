package hexlet.code.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(String entityName) {
        super("Cannot find " + entityName + " with such id.");
    }
}
