package riwi.com.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import riwi.com.springsecurityjwt.models.RoleEntity;
import riwi.com.springsecurityjwt.models.UserEntity;
import riwi.com.springsecurityjwt.repositories.UserRepository;
import riwi.com.springsecurityjwt.utils.ERole;

import java.util.Set;

@SpringBootApplication
public class SpringSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtApplication.class, args);
    }


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Bean
    CommandLineRunner init(){
        return args -> {
            UserEntity userEntity = UserEntity.builder()
                    .email("suarezpadillajonathan@gmail.com")
                    .username("Jonathan")
                    .password(passwordEncoder.encode("1234"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.valueOf(ERole.ADMIN.name()))
                            .build()))
                    .build();

            UserEntity userEntity2 = UserEntity.builder()
                    .email("any@gmail.com")
                    .username("any")
                    .password(passwordEncoder.encode("1234"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.valueOf(ERole.USER.name()))
                            .build()))
                    .build();

            UserEntity userEntity3 = UserEntity.builder()
                    .email("adrea@gmail.com")
                    .username("andrea")
                    .password(passwordEncoder.encode("1234"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.valueOf(ERole.INVITED.name()))
                            .build()))
                    .build();

            userRepository.save(userEntity);
            userRepository.save(userEntity2);
            userRepository.save(userEntity3);
        };
    }
}
