package pe.edu.cibertec.ciberpass.service;

import pe.edu.cibertec.ciberpass.entity.dto.ResetPasswordRequest;

public interface PasswordResetService {
    void forgotPassword(String email);
    void resetPassword(ResetPasswordRequest request);
}
