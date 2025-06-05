package br.com.fiap.climby.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;

@Data
public class UserRequest {
    @NotBlank(message = "{name.not.blank}")
    @Size(min = 3, max = 100, message = "{name.size}")
    private String name;

    @Email(message = "{email.valid}")
    @NotBlank(message = "{email.not.blank}")
    private String email;

    @NotBlank(message = "{country.not.blank}")
    private String country;

    @NotBlank(message = "{city.not.blank}")
    private String city;

    @NotBlank(message = "{password.not.blank}")
    private String password;

}
