package br.com.petconnect.boarding.handler;

import br.com.petconnect.boarding.exception.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Invalid Fields")
                        .details("Check fields errors")
                        .developMessage(ex.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Object> err(TokenException tokenException) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .details(tokenException.getMessage())
                        .title("Bad Request Exception, Invalid Field")
                        .developMessage("Erro Interno")
                        .timestamp(LocalDateTime.now())
                        .developMessage("Check Data")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build(), HttpStatus.UNAUTHORIZED
        );
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> err(BusinessException businessException) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .details(businessException.getMessage())
                        .title("Bad Request Exception, Invalid Field")
                        .developMessage("Erro Interno")
                        .timestamp(LocalDateTime.now())
                        .developMessage("Check Data")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build(), HttpStatus.BAD_REQUEST
        );

    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> err(ResourceNotFoundException resourceNotFoundException) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .details(resourceNotFoundException.getMessage())
                        .title("Bad Request Exception, Invalid Field")
                        .developMessage("Erro Interno")
                        .timestamp(LocalDateTime.now())
                        .developMessage("Check Data")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build(), HttpStatus.NOT_FOUND
        );

    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Acesso negado: " + ex.getMessage());
    }
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationCredentialsNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Autenticação necessária: " + ex.getMessage());
    }



}
