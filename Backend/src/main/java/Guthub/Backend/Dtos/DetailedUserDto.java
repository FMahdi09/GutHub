package Guthub.Backend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedUserDto
{
    private int id;

    private String username;

    private String password;

    private String email;

    private List<RoleDto> roles = new ArrayList<>();
}
