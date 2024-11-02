package br.com.petconnect.boarding.exception;

public class FirebaseUploadException  extends RuntimeException{
    public FirebaseUploadException(String message) {
        super(message);  // Passa a mensagem para o construtor da superclasse (RuntimeException)
    }

    // Construtor que aceita uma mensagem e uma causa
    public FirebaseUploadException(String message, Throwable cause) {
        super(message, cause);  // Passa a mensagem e a causa para o construtor da superclasse
    }

    // Construtor que aceita uma causa
    public FirebaseUploadException(Throwable cause) {
        super(cause);  // Passa a causa para o construtor da superclasse
    }
}

