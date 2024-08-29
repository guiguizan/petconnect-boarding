package br.com.petconnect.boarding.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);  // Passa a mensagem para o construtor da superclasse (RuntimeException)
    }

    // Construtor que aceita uma mensagem e uma causa
    public BusinessException(String message, Throwable cause) {
        super(message, cause);  // Passa a mensagem e a causa para o construtor da superclasse
    }

    // Construtor que aceita uma causa
    public BusinessException(Throwable cause) {
        super(cause);  // Passa a causa para o construtor da superclasse
    }
}
