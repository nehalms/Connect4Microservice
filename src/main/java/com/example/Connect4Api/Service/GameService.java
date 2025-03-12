package com.example.Connect4Api.Service;

import com.example.Connect4Api.Exception.*;
import com.example.Connect4Api.Model.*;
import com.example.Connect4Api.Storage.GameStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@NoArgsConstructor
public class GameService {

    @Autowired
    private RestTemplate restTemplate;
    private static final int WINNING_COUNT = 4;

    @Autowired
    private Environment environment;

    public Game createGame(Player player) {
        Game game = new Game();
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setUserIdX(player.getUserId());
        game.setStatus(GameStatus.NEW);
        game.setBoard(new int[7][7]);
        game.setWinnerIdxs(new String[4]);
        game.setTurn(TicToe.X);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game joinGame(Player player, String gameId) throws GameNotFoundException, GameStartedException, GameCompletedException, DuplicatePlayerException {
        if(!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new GameNotFoundException("No game found with the given ID");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if(game.getStatus() == GameStatus.IN_PROGRESS) {
            throw new GameStartedException("Game you are trying to join is already started");
        }
        if(game.getStatus() == GameStatus.FINISHED) {
            throw new GameCompletedException("Game is already Completed");
        }
        if(game.getPlayer1().getUserId().equals(player.getUserId())) {
            throw new DuplicatePlayerException("Already in the room");
        }
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setPlayer2(player);
        game.setUserIdO(player.getUserId());
        GameStorage.getInstance().setGame(game);

        return game;
    }

    public Game getGame(Player player, String gameId) throws GameNotFoundException, DuplicatePlayerException, GameCompletedException {
        if(!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new GameNotFoundException("No Game found with the given Id");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if(game.getStatus() == GameStatus.FINISHED) {
            throw new GameCompletedException("Game is already Completed");
        }
        if( (game.getPlayer1() != null && game.getPlayer1().equals(player)) || (game.getPlayer2() != null && game.getPlayer2().equals(player)) ) {
            return game;
        } else {
            throw new DuplicatePlayerException("Join the room");
        }
    }

    public Game playGame(GamePlay gamePlay) throws GameNotFoundException, GameCompletedException, WaitingException {
        if(!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new GameNotFoundException("No Game found with the given Id");
        }
        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if(game.getStatus() == GameStatus.NEW) {
            throw new WaitingException("Waiting for the other player to join");
        }
        if(game.getStatus() == GameStatus.FINISHED) {
            throw new GameCompletedException("Game is already Completed");
        }

        int board[][] = game.getBoard();
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().getValue();

        boolean winX = checkWinner(game, board, TicToe.X);
        boolean winO = checkWinner(game, board, TicToe.O);

        if(winX) {
            game.setWinner(TicToe.X);
        } else if(winO) {
            game.setWinner(TicToe.O);
        }

        boolean completed = true;
        for (int[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (ints[j] == 0) {
                    completed = false;
                    break;
                }
            }
        }
        if(completed && (!winX && !winO)) {
            game.setStatus(GameStatus.FINISHED);
            game.setWinner(TicToe.DRAW);
        }
        if(winX || winO) {
            game.setStatus(GameStatus.FINISHED);
        }
        game.setTurn(game.getTurn() == TicToe.X ? TicToe.O : TicToe.X);
        GameStorage.getInstance().setGame(game);

        return game;
    }

    @Async
    public void updateStats(Game game) {
        if(game.getStatus() == GameStatus.FINISHED) {
            int player1Stat = game.getWinner() == TicToe.DRAW || game.getWinner() == TicToe.O ? 0 : 1;
            int player2Stat = game.getWinner() == TicToe.DRAW || game.getWinner() == TicToe.X ? 0 : 1;
            String apiKey = environment.getProperty("APIKEY");
            String url = environment.getProperty("Backend_URL") + "/game/frnrsave/" + game.getUserIdX() + "/" + player1Stat + "/" + game.getUserIdO() + "/" + player2Stat + "/" + apiKey;
            System.out.println("Url: " + url);
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Response: " + response);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResponse apiResponse = objectMapper.readValue(response, ApiResponse.class);
                Player player1 = game.getPlayer1();
                Player player2 = game.getPlayer2();
                if(player1.getUserId().equalsIgnoreCase(apiResponse.getData().getPlayer1().getUserId())) {
                    player1.setGamesPlayed(apiResponse.getData().getPlayer1().getFrnRowStats().getPlayed());
                    player2.setGamesPlayed(apiResponse.getData().getPlayer2().getFrnRowStats().getPlayed());
                } else if(player1.getUserId().equalsIgnoreCase(apiResponse.getData().getPlayer2().getUserId())) {
                    player1.setGamesPlayed(apiResponse.getData().getPlayer2().getFrnRowStats().getPlayed());
                    player2.setGamesPlayed(apiResponse.getData().getPlayer1().getFrnRowStats().getPlayed());
                }
                game.setPlayer1(player1);
                game.setPlayer2(player2);
            } catch (Exception e) {
                System.err.println("Error parsing response: " + e.getMessage());
                e.printStackTrace();
            }
            if(GameStorage.getInstance().getGames().containsKey(game.getGameId())) {
                GameStorage.getInstance().setGame(game);
            }
        }
    }

    public Game resetBoard(String gameId) throws GameNotFoundException, GameStartedException {
        if(!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new GameNotFoundException("No Game found with the given Id");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if(game.getStatus() == GameStatus.IN_PROGRESS) {
            throw new GameStartedException("Game In progress");
        }
        String userIdX = game.getUserIdX();
        game.setUserIdX(game.getUserIdO());
        game.setUserIdO(userIdX);
        game.setBoard(new int[7][7]);
        game.setTurn(TicToe.X);
        game.setWinner(null);
        game.setWinnerIdxs(new String[4]);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);

        return game;
    }

    public boolean checkWinner(Game game, int board[][], TicToe ticToe) {
        int ROWS = board.length;
        int COLS = board[0].length;
        int WINNING_COUNT = 4;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] != ticToe.getValue()) continue;

                if (col + WINNING_COUNT - 1 < COLS &&
                        checkDirection(board, row, col, 0, 1, ticToe.getValue())) {
                    game.setWinnerIdxs(new String[]{row + ":" + col, (row) + ":" + (col+1), (row) + ":" + (col+2), (row) + ":" + (col+3)});
                    return true;
                }

                if (row + WINNING_COUNT - 1 < ROWS &&
                        checkDirection(board, row, col, 1, 0, ticToe.getValue())) {
                    game.setWinnerIdxs(new String[]{row + ":" + col, (row+1) + ":" + (col), (row+2) + ":" + (col), (row+3) + ":" + (col)});
                    return true;
                }

                if (row + WINNING_COUNT - 1 < ROWS && col + WINNING_COUNT - 1 < COLS &&
                        checkDirection(board, row, col, 1, 1, ticToe.getValue())) {
                    game.setWinnerIdxs(new String[]{row + ":" + col, (row+1) + ":" + (col+1), (row+2) + ":" + (col+2), (row+3) + ":" + (col+3)});
                    return true;
                }

                if (row + WINNING_COUNT - 1 < ROWS && col - WINNING_COUNT + 1 >= 0 &&
                        checkDirection(board, row, col, 1, -1, ticToe.getValue())) {
                    game.setWinnerIdxs(new String[]{row + ":" + col, (row+1) + ":" + (col-1), (row+2) + ":" + (col-2), (row+3) + ":" + (col-3)});
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int[][] board, int row, int col, int rowDelta, int colDelta, int player) {
        int WINNING_COUNT = 4;
        for (int i = 0; i < WINNING_COUNT; i++) {
            if (board[row + i * rowDelta][col + i * colDelta] != player) {
                return false;
            }
        }
        return true;
    }

    public void authenticateUser(String token) throws InvalidTokenException {
        String apiKey = environment.getProperty("APIKEY");
        String url = environment.getProperty("Backend_URL") + "/game/authenticateUser/" + token + "/" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Authenticated User : " + response);
        if(response.equalsIgnoreCase("false")){
            throw new InvalidTokenException("Authenticate using valid token");
        }
    }
}
