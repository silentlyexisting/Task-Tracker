package hexlet.code.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String internalServerErrorExceptionHandler(Exception exception) {
        return exception.getMessage();
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    public String userNotExceptionHandler(UsernameNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedExceptionHandler(AccessDeniedException exception) {
        return exception.getMessage();
    }
}
