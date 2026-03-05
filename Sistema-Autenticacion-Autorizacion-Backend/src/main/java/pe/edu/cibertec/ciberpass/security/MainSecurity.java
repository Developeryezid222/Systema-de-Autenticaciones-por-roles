package pe.edu.cibertec.ciberpass.security;

import jakarta.servlet.FilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pe.edu.cibertec.ciberpass.util.AppSettings;

@Configuration
public class MainSecurity {

	@Autowired
	private final UserDetailsService userDetailsService;


	@Autowired
	private final JwtEntryPoint jwtEntryPoint;


    public MainSecurity(UserDetailsService userDetailsService, JwtEntryPoint jwtEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtEntryPoint = jwtEntryPoint;
    }


    @Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
		return authConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    http.csrf(AbstractHttpConfigurer::disable)
	        .cors(cors -> cors.configurationSource(request -> {
	            var corsConfig = new org.springframework.web.cors.CorsConfiguration();
	            corsConfig.setAllowedOrigins(java.util.List.of(AppSettings.URL_CROSS_ORIGIN));
	            corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	            corsConfig.setAllowedHeaders(java.util.List.of("*"));
	            corsConfig.setAllowCredentials(true);
	            return corsConfig;
	        }))
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/uploads/**").permitAll()
	            .requestMatchers("/url/auth/**").permitAll() // solo login/register
	            
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .exceptionHandling(exp -> exp.authenticationEntryPoint(jwtEntryPoint));

	    http.authenticationProvider(authenticationProvider());
	    http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}


}
