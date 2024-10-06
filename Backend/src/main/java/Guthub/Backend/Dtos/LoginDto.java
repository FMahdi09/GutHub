package Guthub.Backend.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto
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
}
