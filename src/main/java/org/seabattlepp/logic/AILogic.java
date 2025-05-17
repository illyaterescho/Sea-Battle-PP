package org.seabattlepp.logic;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.ships.Ship;

import javax.swing.*;

public class AILogic {

    private final AIStrategy aiStrategy;
    private final GameLogic gameLogic;
    private final ShipButton[][] playerShipButtons;
    private Timer timer; // Поле для Timer

    public AILogic(GameLogic gameLogic, ShipButton[][] playerShipButtons) {
        this.gameLogic = gameLogic;
        this.playerShipButtons = playerShipButtons;
        this.aiStrategy = new AIStrategy();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerShipButtons[i][j] == null) {
                    System.err.println("Warning: playerShipButtons[" + i + "][" + j + "] is null");
                }
            }
        }
    }

    public void resetBot() {
        aiStrategy.reset();
        if (timer != null) {
            timer.stop();
        }
    }

    // точка входу в хід комп'ютера
    public void startComputerTurn() {
        if (!gameLogic.isGameStarted || gameLogic.isGameEnded) {
        }
        gameLogic.setPlayerTurn(false);
        gameLogic.boardManager.disableComputerButtons();
        gameLogic.boardManager.disablePlayerButtons();

        int[] shotCoordinates = getValidShotCoordinates();

        if (shotCoordinates == null) {
            gameLogic.startPlayerTurn();
            return;
        }

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, e -> {
            boolean hit = processComputerShot(shotCoordinates[0], shotCoordinates[1]);
            if (!hit && !gameLogic.isGameEnded) {
                gameLogic.setPlayerTurn(true);
                gameLogic.boardManager.enableComputerButtons();
            } else if (hit && gameLogic.isGameStarted && !gameLogic.isGameEnded && !gameLogic.isPlayerTurn) {
                startComputerTurn();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // обирає допустиму клітинку для пострілу
    private int[] getValidShotCoordinates() {
        int[] coordinates;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            coordinates = aiStrategy.chooseShot(playerShipButtons);
            attempts++;
            if (coordinates == null || attempts > MAX_ATTEMPTS) {
                return null;
            }

            // Перевіряємо, чи клітинка доступна через GameLogic
            boolean isAvailable = gameLogic.isCellAvailableForShot(coordinates[0], coordinates[1]);
            boolean isNotShot = !gameLogic.boardManager.isComputerShotAt(coordinates[0], coordinates[1]);
            String cellText = playerShipButtons[coordinates[0]][coordinates[1]] != null ?
                    playerShipButtons[coordinates[0]][coordinates[1]].getText() : "null";

            if (!isAvailable || !isNotShot || "•".equals(cellText)) {
                if ("•".equals(cellText)) {
                    System.out.println("Computer skipped shot at row=" + coordinates[0] + ", col=" + coordinates[1] +
                            " (symbol '•' found), choosing another cell at " + new java.util.Date());
                } else if (!isNotShot) {
                    System.out.println("Computer skipped shot at row=" + coordinates[0] + ", col=" + coordinates[1] +
                            " (cell already shot), choosing another cell at " + new java.util.Date());
                }
                coordinates = null; // Скидаємо координати для нової спроби
                continue;
            }

            // Перевіряємо валідність клітинки лише після всіх перевірок
            if (coordinates != null && !isValidCell(coordinates[0], coordinates[1])) {
                coordinates = null; // Скидаємо, якщо клітинка невалідна
            }
        } while (coordinates == null);

        return coordinates;
    }

    // виконує постріл комп'ютера в гравця
    public boolean processComputerShot(int row, int col) {
        if (!isValidCell(row, col)) {
            System.err.println("Invalid shot coordinates: row=" + row + ", col=" + col + " at " + new java.util.Date());
            return false;
        }

        if (gameLogic.isGameEnded) {
            if (timer != null) {
                timer.stop();
            }
            return false;
        }

        ShipButton button = playerShipButtons[row][col];
        if (button == null) {
            System.err.println("Button is null at row=" + row + ", col=" + col + " at " + new java.util.Date());
            return false;
        }

        // Додаткова перевірка перед пострілом
        if (!gameLogic.isCellAvailableForShot(row, col) || gameLogic.boardManager.isComputerShotAt(row, col)) {
            if (!gameLogic.isGameEnded) {
                startComputerTurn(); // Повторна спроба лише якщо гра не закінчена
            }
            return false;
        }

        Ship ship = gameLogic.boardManager.playerShipsLocations[row][col];
        boolean hit = ship != null;
        boolean sunk = false;

        if (hit) {
            sunk = gameLogic.markHitPlayerBoard(row, col, ship);
        } else {
            gameLogic.markMissPlayerBoard(row, col);
        }

        aiStrategy.processShotResult(new int[]{row, col}, hit, sunk);

        if (sunk && !gameLogic.isGameEnded) {
            gameLogic.checkGameEnd();
        }
        return hit;
    }

    // перевірка на коректність координат
    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }

    // Зупиняє роботу таймера
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}
