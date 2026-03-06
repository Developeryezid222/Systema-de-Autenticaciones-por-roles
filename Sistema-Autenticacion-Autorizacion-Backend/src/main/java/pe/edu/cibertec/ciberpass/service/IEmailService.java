package pe.edu.cibertec.ciberpass.service;

public interface IEmailService {
    void sendPasswordResetEmail(String to, String token);
}
