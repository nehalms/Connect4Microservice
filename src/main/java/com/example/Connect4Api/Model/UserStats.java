package com.example.Connect4Api.Model;

import lombok.Data;

@Data
public class UserStats {

    private String gameId;
    private TicToe type;
    private Player player;
}
