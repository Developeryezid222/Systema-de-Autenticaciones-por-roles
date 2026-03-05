package pe.edu.cibertec.ciberpass.entity.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String token;
    String newPassword;
    String confirmPassword;
}
