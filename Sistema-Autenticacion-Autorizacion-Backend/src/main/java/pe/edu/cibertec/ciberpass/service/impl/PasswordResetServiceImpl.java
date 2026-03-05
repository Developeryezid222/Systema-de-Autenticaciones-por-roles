package pe.edu.cibertec.ciberpass.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.ciberpass.entity.Usuario;
import pe.edu.cibertec.ciberpass.entity.dto.ResetPasswordRequest;
import pe.edu.cibertec.ciberpass.repository.PasswordResetTokenRepository;
import pe.edu.cibertec.ciberpass.repository.UsuarioRepository;
import pe.edu.cibertec.ciberpass.service.PasswordResetService;

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
        Usuario user = usuarioRepository.

    }

    @Override

    public void resetPassword(ResetPasswordRequest request) {

    }
}
