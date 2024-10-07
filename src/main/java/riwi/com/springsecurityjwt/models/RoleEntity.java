package riwi.com.springsecurityjwt.models;


import jakarta.persistence.*;
import lombok.*;
import riwi.com.springsecurityjwt.utils.ERole;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole name;
}
