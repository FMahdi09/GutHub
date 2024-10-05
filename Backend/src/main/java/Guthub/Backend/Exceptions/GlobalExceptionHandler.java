package Guthub.Backend.Exceptions;

import Guthub.Backend.Dtos.ErrorDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleIntegrityViolation()
    {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElement()
    {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex)
    {
        List<String> errorList = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) ->
        {
            String fieldName;

            try
            {
                fieldName = ((FieldError) error).getField();
            }
            catch (ClassCastException e)
            {
                fieldName = error.getObjectName();
            }

            String message = error.getDefaultMessage();

            errorList.add(String.format("%s: %s", fieldName, message));
        });

        return new ResponseEntity<>(new ErrorDto(errorList), HttpStatus.BAD_REQUEST);
    }
}
