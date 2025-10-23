package com.boardcamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.GamesIdConflictException;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GamesRepository;

@Service
public class GamesService {

    final GamesRepository gamesRepository;
    GamesService(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }
    
    public List<GamesModel> getGames() {
        return gamesRepository.findAll();
    }

    public Optional<GamesModel> postGames(GamesDTO body) {

        if (gamesRepository.existsByName(body.getName())) {
            throw new GamesIdConflictException("Game with this name already exists");
        }

        GamesModel item = new GamesModel(body);
        gamesRepository.save(item);
        return Optional.of(item);
    }
}
