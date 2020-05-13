package com.mycode.conferencesregistration.api.exception;

import com.mycode.conferencesregistration.exception.ConferenceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yurii Kovtun
 */
@ControllerAdvice
public class ExceptionMapping {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConferenceNotFoundException.class)
    String conferenceNotFoundHandler(ConferenceNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().
                forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().stream().
                forEach(error ->
                        errors.put(
                                error.getPropertyPath().toString().substring(error.getPropertyPath().toString().lastIndexOf(".")+1),
                                error.getMessage()));

        return errors;
    }
}
