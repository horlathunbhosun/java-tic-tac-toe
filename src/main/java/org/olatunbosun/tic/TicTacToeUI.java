package org.olatunbosun.tic;

import org.olatunbosun.tic.algo.MinMax;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.olatunbosun.tic.store.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * GUI for Tic Tac Toe game with Minimax algorithm implementation.
 * Tracks and displays the number of wins for each player.
 *
 * @author olulodeolatunbosun
 * @created 05/06/2024 - 11:29
 */
public class TicTacToeUI extends JFrame {

    private Board gameBoard;

    private MinMax minimax;
    private JButton[][] buttons;
    private boolean playerTurn;
    private GameData gameData;
    private JLabel scoreLabel;

    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private static final String DATA_FILE = "src/main/resources/gameData.json";

    public TicTacToeUI() {
        gameBoard = new Board();
        minimax = new MinMax(gameBoard);
        buttons = new JButton[3][3];
        playerTurn = true;
        gameData = loadGameData();

        setTitle("Tic Tac Toe");
        setLayout(new BorderLayout());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeButtons();
        initializeScoreLabel();
        setVisible(true);
    }

    private void initializeButtons() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    private void initializeScoreLabel() {
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        updateScoreLabel();
        add(scoreLabel, BorderLayout.EAST);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("<html>Player X Wins: " + gameData.getPlayerXWins() +
                "<br>Player O Wins: " + gameData.getPlayerOWins() +
                "<br>Draws: " + gameData.getDraws() + "</html>");    }

    private void makeMove(int row, int col, char player) {
        gameBoard.makeMove(row, col, player);
        buttons[row][col].setText(String.valueOf(player));
        buttons[row][col].setEnabled(false);
    }

    private void checkGameStatus() {
        if (gameBoard.isWinner(PLAYER_X)) {
            gameData.setPlayerXWins(gameData.getPlayerXWins() + 1);
            showMessage("Player X wins!");
            saveGameData();
            updateScoreLabel();
            resetGame();
        } else if (gameBoard.isWinner(PLAYER_O)) {
            gameData.setPlayerOWins(gameData.getPlayerOWins() + 1);
            showMessage("Player O wins!");
            saveGameData();
            updateScoreLabel();
            resetGame();
        } else if (!gameBoard.isMovesLeft()) {
            gameData.setDraws(gameData.getDraws() + 1);
            showMessage("It's a draw from both!");
            saveGameData();
            updateScoreLabel();
            resetGame();
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void resetGame() {
        gameBoard = new Board();
        minimax = new MinMax(gameBoard);
        playerTurn = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private GameData loadGameData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                return mapper.readValue(file, GameData.class);
            } else {
                return new GameData();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new GameData();
        }
    }

    private void saveGameData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(DATA_FILE), gameData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerTurn) {
                makeMove(row, col, PLAYER_X);
                playerTurn = false;
                checkGameStatus();

                if (!gameBoard.isWinner(PLAYER_X) && gameBoard.isMovesLeft()) {
                    int[] bestMove = minimax.findBestMove(PLAYER_O);
                    makeMove(bestMove[0], bestMove[1], PLAYER_O);
                    playerTurn = true;
                    checkGameStatus();
                }
            }
        }
    }
}
