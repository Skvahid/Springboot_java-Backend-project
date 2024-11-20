package com.ums.exceptions;

import com.ums.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@ControllerAdvice
public class ExceptionHandlerClass {
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity< ErrorDetails> resourceNotFoundException(
        ResourceNotFound exception,
        WebRequest webRequest

    ){
        ErrorDetails error = new ErrorDetails(exception.getMessage(),new Date(),webRequest.getDescription(false));

        return  new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> globalExceptionHandler(
            Exception exception,
            WebRequest webRequest

    ){
        ErrorDetails error = new ErrorDetails(exception.getMessage(),new Date(),webRequest.getDescription(false));

        return  new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
