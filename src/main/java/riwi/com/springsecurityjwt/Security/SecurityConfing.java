package riwi.com.springsecurityjwt.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import riwi.com.springsecurityjwt.Security.JWT.JWTUtils;
import riwi.com.springsecurityjwt.Security.filters.JwtAuthenticationFilter;
import riwi.com.springsecurityjwt.Security.filters.JwtAuthorizationFilter;
import riwi.com.springsecurityjwt.service.UserDetailsServiceImpl;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // habiltamos las anotaciones de spring security para los controladores
public class SecurityConfing {

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthorizationFilter authorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);

        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(authorize->{
                    authorize.requestMatchers("/hello").permitAll();
                    //authorize.requestMatchers("/accessAdmin").hasAnyRole("ADMIN","USER");
                    authorize.anyRequest().authenticated();
                })
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(jwtAuthenticationFilter) //primer filtro
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class) // segundo filtro
                .build();
    }


//    @Bean
//    UserDetailsService userDetailsService() {
//
//        // crear un usuario en memoria
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//
//        manager.createUser(User
//                .withUsername("Jonathan")
//                .password("1234")
//                .roles()
//                .build());
//
//        return manager;
//    }


    // genera el hasheo de la contraseña
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();   //NoOpPasswordEncoder.getInstance();
    }

    // encarga de la administracion de la authenticacion de los usuarios
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);


        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return  authenticationManagerBuilder.build();

    }




}
