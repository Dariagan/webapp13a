package es.codeurjc.backend.security;

import es.codeurjc.backend.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    // TODO define all endpoints
    private final String[] publicEndpoints = {
        "/api/auth/**",
        "/api/ex/public-str",
        "/api/**",//FIXME
        "/h2-console/**"
    };
    
    private final String[] userEndpoints = {
        "/api/ex/user-str",
        "/api/ex/name"
    };
    
    private final String[] adminEndpoints = {
        "/api/ex/admin-str"
    };
    
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf()
            .disable()
            .authorizeHttpRequests()
            .antMatchers(publicEndpoints)
            .permitAll()
            .antMatchers(userEndpoints)
            .hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
            .antMatchers(adminEndpoints)
            .hasAuthority(Role.ADMIN.toString())
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
