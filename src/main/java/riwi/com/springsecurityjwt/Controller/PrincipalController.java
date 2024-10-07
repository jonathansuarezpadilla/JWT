package riwi.com.springsecurityjwt.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import riwi.com.springsecurityjwt.Controller.request.createUserDto;
import riwi.com.springsecurityjwt.models.RoleEntity;
import riwi.com.springsecurityjwt.models.UserEntity;
import riwi.com.springsecurityjwt.repositories.UserRepository;
import riwi.com.springsecurityjwt.utils.ERole;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
public class PrincipalController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    public String hello(){
        return "Hello world Not Secured";

    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello world Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody createUserDto createUserDto){


        Set<RoleEntity> roles= createUserDto.getRole()
                .stream().map(role->RoleEntity
                        .builder()
                        .name(ERole.valueOf(role))
                        .build()).collect(Collectors.toSet());

        System.out.println(createUserDto.toString());

        UserEntity userEntity= UserEntity.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword())) //encriptacion de la contraseña
                .email(createUserDto.getEmail())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
        userRepository.deleteById(Long.parseLong(id));
        return "se ha borrado el usuario con id ".concat(id);
    }



}
