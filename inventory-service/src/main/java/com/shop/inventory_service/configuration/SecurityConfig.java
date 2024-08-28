package com.shop.inventory_service.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@EnableJpaAuditing
public class SecurityConfig {
    private final static String[] publicUrl = {"/internal/**"};
    private final CustomJwtDecoder customJwtDecoder;
    public SecurityConfig(CustomJwtDecoder customJwtDecoder){
        this.customJwtDecoder = customJwtDecoder;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomJwtDecoder customJwtDecoder, JwtAuthenticationConverter getJwtAuthenticationConverter) throws Exception {
        httpSecurity.authorizeHttpRequests(request->
                request.requestMatchers(publicUrl).permitAll()
                        .anyRequest()
                        .authenticated());
        httpSecurity.oauth2ResourceServer(oauth ->
                oauth.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntrypoint())
                ).csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
