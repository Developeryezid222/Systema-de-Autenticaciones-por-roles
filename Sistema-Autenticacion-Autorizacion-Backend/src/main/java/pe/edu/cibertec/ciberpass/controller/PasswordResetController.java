package pe.edu.cibertec.ciberpass.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.ciberpass.entity.PasswordResetToken;
import pe.edu.cibertec.ciberpass.entity.dto.ResetPasswordRequest;
import pe.edu.cibertec.ciberpass.repository.PasswordResetTokenRepository;
import pe.edu.cibertec.ciberpass.service.PasswordResetService;
import pe.edu.cibertec.ciberpass.util.AppSettings;

import java.util.Map;

@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
@Validated
public class PasswordResetController {

    private final PasswordResetService passwordResetService;



    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;


    }


    // PASO 1: Solicitar restablecimiento
    @PostMapping("/forgot")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestBody Map<String, String> body) {

        String email = body.get("correo");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje1", "El correo es requerido."));
        }

        passwordResetService.forgotPassword(email);

        return ResponseEntity.ok(Map.of(
                "mensaje2", "Si el correo está registrado, recibirás un enlace en breve."
        ));
    }

    // PASO 2: Validar token
    @GetMapping("/validar")
    public ResponseEntity<Map<String, Object>> validarToken(@RequestParam String token) {
        boolean valido = passwordResetService.validarToken(token);

        if (valido) {
            return ResponseEntity.ok(Map.of("valido", true));
        }
        return ResponseEntity.badRequest()
                .body(Map.of("valido", false, "mensaje1", "El enlace ha expirado o ya fue utilizado."));
    }

    // PASO 3: Cambiar contraseña
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@Validated  @RequestBody ResetPasswordRequest request) {

        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}