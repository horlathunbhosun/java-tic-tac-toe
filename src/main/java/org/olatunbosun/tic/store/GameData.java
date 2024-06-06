package org.olatunbosun.tic.store;

/**
 * @author olulodeolatunbosun
 * @created 05/06/2024/06/2024 - 11:59
 */
public class GameData {
    private int playerXWins;
    private int playerOWins;

    private int draws;

    public GameData() {
        this.playerXWins = 0;
        this.playerOWins = 0;
    }

    public int getPlayerXWins() {
        return playerXWins;
    }

    public void setPlayerXWins(int playerXWins) {
        this.playerXWins = playerXWins;
    }

    public int getPlayerOWins() {
        return playerOWins;
    }

    public void setPlayerOWins(int playerOWins) {
        this.playerOWins = playerOWins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

}