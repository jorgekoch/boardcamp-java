package com.boardcamp.api.models;

import com.boardcamp.api.dtos.CustomersDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers-boardcamp")
public class CustomersModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 11, min = 10)
    @Column(nullable = false, length = 11)
    private String phone;

    @Size(min = 11, max = 11)
    @Column(nullable = false, length = 11)
    private String cpf;

    public CustomersModel (CustomersDTO dto) {
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.cpf = dto.getCpf();
    }
}
