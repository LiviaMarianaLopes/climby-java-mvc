package br.com.fiap.climby.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ShelterRequest {

    @NotBlank(message = "{name.not.blank}")
    @Size(min = 3, max = 100, message = "{name.size}")
    private String name;

    @NotBlank(message = "{email.not.blank}")
    @Email(message = "{email.valid}")
    private String email;

    @NotNull(message = "{phone.not.null}")
    private Long phone;

    @NotBlank(message = "{country.not.blank}")
    private String country;

    @NotBlank(message = "{city.not.blank}")
    private String city;

    @NotBlank(message = "{address.not.blank}")
    private String adress;

    @NotNull(message = "{addressNumber.not.null}")
    private int adressNumber;

    @NotBlank(message = "{cep.not.blank}")
    @Pattern(regexp = "\\d{8}", message = "{cep.valid}")
    private String zip;

    @NotBlank(message = "{district.not.blank}")
    private String district;

    private boolean isFull;
}
