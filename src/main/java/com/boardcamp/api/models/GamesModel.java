package com.boardcamp.api.models;

import com.boardcamp.api.dtos.GamesDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "games-boardcamp")
public class GamesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer stockTotal;

    @Column(nullable = false)
    private Integer pricePerDay;

    public GamesModel (GamesDTO dto) {
        this.name = dto.getName();
        this.image = dto.getImage();
        this.stockTotal = dto.getStockTotal();
        this.pricePerDay = dto.getPricePerDay();
    }
}
