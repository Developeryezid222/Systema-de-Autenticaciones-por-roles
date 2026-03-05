package pe.edu.cibertec.ciberpass.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public PasswordResetToken(String token, Usuario usuario) {
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusMinutes(30);
        this.usuario = usuario;
    }

    public PasswordResetToken(String token, LocalDateTime localDateTime, Usuario user) {
    }

    public boolean isExpiry() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
    public boolean isValited() {
        return !isExpiry();
    }
}
