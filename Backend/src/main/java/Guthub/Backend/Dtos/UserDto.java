package Guthub.Backend.Dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto
{
    private int id;

    private String username;

    private String password;

    private List<RoleDto> roles = new ArrayList<>();
}
