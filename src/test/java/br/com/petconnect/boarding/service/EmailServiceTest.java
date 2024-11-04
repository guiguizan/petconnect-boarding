package br.com.petconnect.boarding.service;

import br.com.petconnect.boarding.service.user.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Value("${email_sender}")
    private String emailSender = "no-reply@example.com"; // Valor padrão para simulação

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendPasswordResetEmail() {
        // Dados simulados
        String to = "user@example.com";
        String resetLink = "http://example.com/reset-password?token=abc123";

        // Execução do método
        emailService.sendPasswordResetEmail(to, resetLink);

        // Verificações
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

        // Verificação detalhada do conteúdo da mensagem
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom(emailSender);
        expectedMessage.setTo(to);
        expectedMessage.setSubject("Redefinição de Senha");
        expectedMessage.setText("Clique no link abaixo para redefinir sua senha:\n" + resetLink);


    }
}