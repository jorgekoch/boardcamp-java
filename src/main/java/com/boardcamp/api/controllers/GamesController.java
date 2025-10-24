package com.boardcamp.api.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.services.GamesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/games")
public class GamesController {

    final GamesService gamesService;
    GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping
    public ResponseEntity<Object> getGames() {
        return ResponseEntity.status(HttpStatus.OK).body(gamesService.getGames());
    }
    
    @PostMapping
    public ResponseEntity<Object> postGames(@RequestBody @Valid GamesDTO body) {
        GamesModel item = gamesService.postGames(body);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

}
