package pe.edu.cibertec.ciberpass.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
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

    public PasswordResetToken(String token, LocalDateTime expiryDate, Usuario usuario) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.usuario = usuario;
    }

    public boolean isExpiry() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
    public boolean isValited() {
        return !isExpiry();
    }
}
