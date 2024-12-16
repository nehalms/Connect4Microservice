package com.example.Connect4Api.Model;

import lombok.Data;

import java.util.Objects;

@Data
public class Player {

    private String userId;
    private String name;
    private int gamesPlayed;

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, gamesPlayed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return gamesPlayed == player.gamesPlayed && Objects.equals(userId, player.userId) && Objects.equals(name, player.name);
    }
}
