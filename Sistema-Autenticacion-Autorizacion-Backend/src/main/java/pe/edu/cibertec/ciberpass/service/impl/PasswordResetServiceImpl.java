package pe.edu.cibertec.ciberpass.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.ciberpass.entity.PasswordResetToken;
import pe.edu.cibertec.ciberpass.entity.Usuario;
import pe.edu.cibertec.ciberpass.entity.dto.ResetPasswordRequest;
import pe.edu.cibertec.ciberpass.repository.PasswordResetTokenRepository;
import pe.edu.cibertec.ciberpass.repository.UsuarioRepository;
import pe.edu.cibertec.ciberpass.service.PasswordResetService;


import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void forgotPassword(String email) {
        //Usuario user = usuarioRepository.findByLogin(email);
        Usuario user = usuarioRepository.findByCorreo(email).orElse(null);
        if(user == null) {
            return;
        }

        tokenRepository.eliminarTokensDeUsuario(user.getIdUsuario());

        // Generar token aleatorio seguro
        String token = UUID.randomUUID().toString();

        // Guardar token en BD
        PasswordResetToken resetToken = new PasswordResetToken(token, user
        );

        tokenRepository.save(resetToken);



        try {
            emailService.sendPasswordResetEmail(user.getCorreo(), user.getNombres(), token);
        } catch (Exception e) {
            throw new RuntimeException("No se puede enviar el correo de restablecimiento", e);
        }

    }


    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Validar que las contraseñas coincidan
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        // Buscar token en BD
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido o no encontrado."));

        // Validar que no esté expirado
        if (!resetToken.isValited()) {
            throw new RuntimeException("El enlace ha expirado. Solicita uno nuevo.");
        }

        //Obtener usuario asociado al Tokend
        Usuario usuario = resetToken.getUsuario();

        String passworHash = passwordEncoder.encode(request.getNewPassword());
        usuario.setPassword(passworHash);
        usuarioRepository.save(usuario);

        log.info("Contraseña actualizada para usuario: {}", usuario.getLogin());
        log.debug("Has generado {}", passworHash);

        tokenRepository.delete(resetToken);

    }

    @Override
    public boolean validarToken(String token) {
        return tokenRepository.findByToken(token)
                .map(PasswordResetToken::isValited)
                .orElse(false);
    }
}
