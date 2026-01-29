package pe.edu.cibertec.ciberpass.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DecoderPassword {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		String password = "juana2025";
		String encodedPassword = "$2a$10$WTcHP9dP84V1bCLj16zIhuW1y24CShRKDI4vsJBYt83Go5uXcbdSe";
		
		boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
		System.out.println("Password : " + password + "   isPasswordMatch    : " + isPasswordMatch);
		
	}
}
