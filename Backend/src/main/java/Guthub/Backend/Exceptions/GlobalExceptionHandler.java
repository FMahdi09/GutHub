package Guthub.Backend.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleIntegrityViolation()
    {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
