package Guthub.Backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role
{
    @Id
    @GeneratedValue
    private int id;

    @Column(
            unique = true,
            nullable = false
    )
    private String name;

    public GrantedAuthority getAuthority()
    {
        return new SimpleGrantedAuthority("ROLE_" + name);
    }
}
