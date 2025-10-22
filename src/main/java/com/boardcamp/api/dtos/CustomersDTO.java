package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomersDTO {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(min = 10, max = 11)
    private String phone;

    @NotBlank
    @Size(min = 11, max = 11)
    private String cpf;
}
