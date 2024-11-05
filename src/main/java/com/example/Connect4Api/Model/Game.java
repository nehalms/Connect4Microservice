package com.example.Connect4Api.Model;

import lombok.Data;

@Data
public class Game {
    private String gameId;
    private String userIdX;
    private String userIdO;
    private Player player1;
    private Player player2;
    private GameStatus status;
    private TicToe turn;
    private int[][] board;
    private TicToe winner;
    private String[] winnerIdxs;
}
