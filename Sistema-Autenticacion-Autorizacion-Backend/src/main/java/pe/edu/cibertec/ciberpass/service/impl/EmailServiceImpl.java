package pe.edu.cibertec.ciberpass.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.ciberpass.service.IEmailService;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;



    @Override
    public void sendPasswordResetEmail(String to, String nombres, String token) {
        String resetLink = "http://localhost:4200/forgot-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remitente);
        message.setTo(to);
        message.setSubject("Restablece tu contraseña");
        message.setText("""
            Hola %s,
            
            Recibimos una solicitud para restablecer tu contraseña.
            Haz clic en el siguiente enlace para crear una nueva:
            
            %s
            
            Este enlace expirará en 30 minutos.
            
            Si no solicitaste esto, ignora este mensaje.
            
            Saludos,
            Equipo Soporte IT
            """
                .formatted(nombres, resetLink)); // ✅ nombres y resetLink
        mailSender.send(message);


    }
}
