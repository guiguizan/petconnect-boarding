package br.com.petconnect.boarding.handler;

import br.com.petconnect.boarding.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testHandleTokenException() {
        TokenException ex = new TokenException("Token is invalid");

        ResponseEntity<Object> response = globalExceptionHandler.err(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ValidationExceptionDetails);

        ValidationExceptionDetails details = (ValidationExceptionDetails) response.getBody();
        assertEquals("Token is invalid", details.getDetails());
        assertEquals("Bad Request Exception, Invalid Field", details.getTitle());
    }

    @Test
    public void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Business logic error");

        ResponseEntity<Object> response = globalExceptionHandler.err(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ExceptionDetails);

        ExceptionDetails details = (ExceptionDetails) response.getBody();
        assertEquals("Business logic error", details.getDetails());
        assertEquals("Bad Request Exception, Invalid Field", details.getTitle());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        ResponseEntity<Object> response = globalExceptionHandler.err(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ExceptionDetails);

        ExceptionDetails details = (ExceptionDetails) response.getBody();
        assertEquals("Resource not found", details.getDetails());
        assertEquals("Bad Request Exception, Invalid Field", details.getTitle());
    }

    @Test
    public void testHandleAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        ResponseEntity<Object> response = globalExceptionHandler.handleAccessDeniedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ExceptionDetails);

        ExceptionDetails details = (ExceptionDetails) response.getBody();
        assertEquals("Access denied", details.getDetails());
        assertEquals("Unauthorized, Invalid Credentials", details.getTitle());
    }

    @Test
    public void testHandleAuthenticationException() {
        AuthenticationCredentialsNotFoundException ex = new AuthenticationCredentialsNotFoundException("Credentials not found");

        ResponseEntity<String> response = globalExceptionHandler.handleAuthenticationException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Autenticação necessária: Credentials not found", response.getBody());
    }

    @Test
    public void testHandleFirebaseUploadException() {
        FirebaseUploadException ex = new FirebaseUploadException("Upload failed");

        ResponseEntity<String> response = globalExceptionHandler.handleAuthenticationException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro Ao Fazer Upload: Upload failed", response.getBody());
    }
}
