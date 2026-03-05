package pe.edu.cibertec.ciberpass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.ciberpass.entity.PasswordResetToken;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.usuario.idUsuario = :idUsuario")
    void eliminarTokensDeUsuario(@Param("idUsuario") int idUsuario);
}
