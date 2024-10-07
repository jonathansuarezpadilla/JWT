package riwi.com.springsecurityjwt.Controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class createUserDto {

    @Email
    @NotBlank
    private String email;
    private String username;
    private String password;
    private Set<String> role  = new HashSet<>();;
}
