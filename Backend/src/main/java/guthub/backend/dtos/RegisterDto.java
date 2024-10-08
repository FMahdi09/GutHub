package guthub.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto
{
    @Pattern(
            regexp = "^.{4,20}$",
            message = "username must be between 4 and 20 characters"
    )
    @NotBlank
    private String username;

    @Pattern(
            regexp = "^\\S{8,20}$",
            message = "password must be between 8 and 20 characters"
    )
    @NotBlank
    private String password;

    @Pattern(
            regexp = "^\\S+@\\S+\\.\\S+$",
            message = "invalid email address"
    )
    @NotBlank
    private String email;
}
