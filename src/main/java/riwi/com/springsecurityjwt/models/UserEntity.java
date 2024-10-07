package riwi.com.springsecurityjwt.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

@Builder
@Table(name="user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Size(max=80)
    private String email;

    @NotBlank
    @Size(max=40)
    private String username;

    @NotBlank
    private String password;

    @ManyToMany(fetch= FetchType.EAGER,targetEntity = RoleEntity.class, cascade=CascadeType.PERSIST)
    @JoinTable(name="user_roles",joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<RoleEntity> roles;

}
