package org.olatunbosun.tic.algo;

import org.olatunbosun.tic.Board;

/**
 * Implements the Minimax algorithm for the Tic Tac Toe game.
 * Evaluates and finds the best possible move for the given player.
 *
 * @author olulodeolatunbosun
 * @created 05/06/2024 - 11:26
 */
public class MinMax {

    private static final int SIZE = 3;
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private Board gameBoard;

    public MinMax(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Finds the best possible move for the given player using the Minimax algorithm.
     *
     * @param player The player ('X' or 'O') for whom the best move is to be found.
     * @return An array of two integers representing the row and column of the best move.
     */
    public int[] findBestMove(char player) {
        int bestVal = (player == PLAYER_X) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard.getBoard()[i][j] == ' ') {
                    gameBoard.makeMove(i, j, player);
                    int moveVal = minimax(0, player == PLAYER_X ? false : true);
                    gameBoard.getBoard()[i][j] = ' ';

                    if (player == PLAYER_X && moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    } else if (player == PLAYER_O && moveVal < bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Implements the Minimax algorithm.
     *
     * @param depth The current depth of the recursion.
     * @param isMax True if the current move is by the maximizer (PLAYER_X), false otherwise.
     * @return The evaluated score of the current board configuration.
     */
    private int minimax(int depth, boolean isMax) {
        int score = evaluate();

        if (score == 10 || score == -10 || !gameBoard.isMovesLeft()) {
            return score;
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (gameBoard.getBoard()[i][j] == ' ') {
                        gameBoard.makeMove(i, j, PLAYER_X);
                        best = Math.max(best, minimax(depth + 1, false));
                        gameBoard.getBoard()[i][j] = ' ';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (gameBoard.getBoard()[i][j] == ' ') {
                        gameBoard.makeMove(i, j, PLAYER_O);
                        best = Math.min(best, minimax(depth + 1, true));
                        gameBoard.getBoard()[i][j] = ' ';
                    }
                }
            }
            return best;
        }
    }

    /**
     * Evaluates the current board configuration and returns a score.
     *
     * @return 10 if PLAYER_X wins, -10 if PLAYER_O wins, 0 for a draw or incomplete game.
     */
    private int evaluate() {
        char[][] board = gameBoard.getBoard();

        // Check rows for victory
        for (int row = 0; row < SIZE; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == PLAYER_X) {
                    return 10;
                } else if (board[row][0] == PLAYER_O) {
                    return -10;
                }
            }
        }

        // Check columns for victory
        for (int col = 0; col < SIZE; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == PLAYER_X) {
                    return 10;
                } else if (board[0][col] == PLAYER_O) {
                    return -10;
                }
            }
        }

        // Check diagonals for victory
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == PLAYER_X) {
                return 10;
            } else if (board[0][0] == PLAYER_O) {
                return -10;
            }
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == PLAYER_X) {
                return 10;
            } else if (board[0][2] == PLAYER_O) {
                return -10;
            }
        }

        return 0;
    }
}
