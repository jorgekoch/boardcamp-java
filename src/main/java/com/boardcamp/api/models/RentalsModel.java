package com.boardcamp.api.models;

import java.time.LocalDate;

import com.boardcamp.api.dtos.RentalsDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rentals-boardcamp")
public class RentalsModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private CustomersModel customer;

    @ManyToOne
    @JoinColumn(name = "gameId", nullable = false)
    private GamesModel game;

    @Column(nullable = false)
    private LocalDate rentDate;

    @Column(nullable = false)
    private Integer daysRented;

    @Column
    private LocalDate returnDate;

    @Column(nullable = false)
    private Integer originalPrice;

    @Column
    private Integer delayFee;

    public RentalsModel (RentalsDTO dto, GamesModel game, CustomersModel customer) {
        this.customer = customer;
        this.game = game;
        this.daysRented = dto.getDaysRented();
        this.rentDate = LocalDate.now();
        this.originalPrice = daysRented * game.getPricePerDay();
        this.delayFee = 0;
    }
}
