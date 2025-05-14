package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.logic.GameLogic;
import org.seabattlepp.ships.Ship;

import javax.swing.*;

public class AILogic {

    private AIPlayer aiPlayer;
    private final GameLogic gameLogic;
    private final ShipButton[][] playerShipButtons;

    public AILogic(GameLogic gameLogic, ShipButton[][] playerShipButtons) {
        this.gameLogic = gameLogic;
        this.playerShipButtons = playerShipButtons;
        this.aiPlayer = new AIPlayer();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerShipButtons[i][j] == null) {
                    System.err.println("Warning: playerShipButtons[" + i + "][" + j + "] is null");
                }
            }
        }
    }

    public void resetAI() {
        aiPlayer.resetAI();
    }

    public void startComputerTurn() {
        if (!gameLogic.isGameStarted()) {
            System.out.println("Game not started, skipping computer turn");
            return;
        }
        gameLogic.setPlayerTurn(false);
        gameLogic.disableComputerButtons();
        gameLogic.disablePlayerButtons();

        int[] shotCoordinates = aiPlayer.makeTurn(playerShipButtons);
        System.out.println("Computer shot: " + (shotCoordinates != null ? java.util.Arrays.toString(shotCoordinates) : "null"));

        if (shotCoordinates == null) {
            System.out.println("No shot coordinates, returning turn to player");
            gameLogic.startPlayerTurn();
            return;
        }

        Timer timer = new Timer(700, e -> {
            boolean hit = processComputerShot(shotCoordinates[0], shotCoordinates[1]);
            System.out.println("Computer shot result: hit=" + hit);
            if (!hit) {
                gameLogic.setPlayerTurn(true);
                gameLogic.enableComputerButtons();
            } else if (gameLogic.isGameStarted() && !gameLogic.isPlayerTurn()) {
                startComputerTurn(); // Повторний хід, якщо влучив
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public boolean processComputerShot(int row, int col) {
        if (!isValidCell(row, col)) {
            System.err.println("Invalid shot coordinates: row=" + row + ", col=" + col);
            return false;
        }

        ShipButton button = playerShipButtons[row][col];
        if (button == null) {
            System.err.println("Button is null at row=" + row + ", col=" + col);
            return false;
        }

        Ship ship = gameLogic.getPlayerShipAt(row, col);
        boolean hit = ship != null;
        boolean sunk = false;

        if (hit) {
            sunk = gameLogic.markHitPlayerBoard(row, col, ship);
        } else {
            gameLogic.markMissPlayerBoard(row, col);
        }

        aiPlayer.processShotResult(new int[]{row, col}, hit, sunk);

        if (sunk) {
            gameLogic.checkGameEnd();
        }
        return hit;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }
}