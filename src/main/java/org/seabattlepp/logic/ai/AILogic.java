package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.logic.GameLogic;
import org.seabattlepp.ships.Ship;

import javax.swing.*;

public class AILogic {

    private AIPlayer aiPlayer;
    private final GameLogic gameLogic;
    private final ShipButton[][] playerShipButtons;
    private Timer timer; // Поле для Timer

    public AILogic(GameLogic gameLogic, ShipButton[][] playerShipButtons) {
        this.gameLogic = gameLogic;
        this.playerShipButtons = playerShipButtons;
        this.aiPlayer = new AIPlayer(gameLogic); // Передаємо GameLogic до AIPlayer
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
        if (timer != null) {
            timer.stop(); // Зупиняємо таймер при скиданні AI
            System.out.println("Timer stopped in resetAI at " + new java.util.Date());
        }
    }

    public void startComputerTurn() {
        if (!gameLogic.isGameStarted() || gameLogic.isGameEnded()) {
            System.out.println("Game not started or ended, skipping computer turn at " + new java.util.Date());
            return;
        }
        gameLogic.setPlayerTurn(false);
        gameLogic.disableComputerButtons();
        gameLogic.disablePlayerButtons();

        int[] shotCoordinates = getValidShotCoordinates();
        System.out.println("Computer shot: " + (shotCoordinates != null ? java.util.Arrays.toString(shotCoordinates) : "null") + " at " + new java.util.Date());

        if (shotCoordinates == null) {
            System.out.println("No shot coordinates, returning turn to player at " + new java.util.Date());
            gameLogic.startPlayerTurn();
            return;
        }

        // Зупиняємо попередній таймер, якщо він існує
        if (timer != null) {
            timer.stop();
            System.out.println("Previous timer stopped in startComputerTurn at " + new java.util.Date());
        }

        timer = new Timer(1000, e -> {
            boolean hit = processComputerShot(shotCoordinates[0], shotCoordinates[1]);
            System.out.println("Computer shot result: hit=" + hit + " at " + new java.util.Date());
            if (!hit && !gameLogic.isGameEnded()) {
                gameLogic.setPlayerTurn(true);
                gameLogic.enableComputerButtons();
            } else if (hit && gameLogic.isGameStarted() && !gameLogic.isGameEnded() && !gameLogic.isPlayerTurn()) {
                startComputerTurn(); // Продовжуємо хід лише якщо гра триває
            }
        });
        timer.setRepeats(false);
        timer.start();
        System.out.println("Timer started for shot at " + new java.util.Date());
    }

    private int[] getValidShotCoordinates() {
        int[] coordinates = null;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            coordinates = aiPlayer.makeTurn(playerShipButtons);
            attempts++;
            if (coordinates == null || attempts > MAX_ATTEMPTS) {
                System.out.println("Max attempts reached or no coordinates available, returning null at " + new java.util.Date());
                return null;
            }

            // Перевіряємо, чи клітинка доступна через GameLogic
            boolean isAvailable = gameLogic.isCellAvailableForShot(coordinates[0], coordinates[1]);
            boolean isNotShot = !gameLogic.isComputerShotAt(coordinates[0], coordinates[1]);
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
                continue;
            }
        } while (coordinates == null);

        return coordinates;
    }

    public boolean processComputerShot(int row, int col) {
        if (!isValidCell(row, col)) {
            System.err.println("Invalid shot coordinates: row=" + row + ", col=" + col + " at " + new java.util.Date());
            return false;
        }

        if (gameLogic.isGameEnded()) {
            System.out.println("Game already ended, skipping shot at row=" + row + ", col=" + col + " at " + new java.util.Date());
            if (timer != null) {
                timer.stop();
                System.out.println("Timer stopped in processComputerShot due to game end at " + new java.util.Date());
            }
            return false;
        }

        ShipButton button = playerShipButtons[row][col];
        if (button == null) {
            System.err.println("Button is null at row=" + row + ", col=" + col + " at " + new java.util.Date());
            return false;
        }

        // Додаткова перевірка перед пострілом
        if (!gameLogic.isCellAvailableForShot(row, col) || gameLogic.isComputerShotAt(row, col)) {
            System.out.println("Computer tried to shoot at already used cell in processComputerShot: row=" + row + ", col=" + col + ". Retrying... at " + new java.util.Date());
            if (!gameLogic.isGameEnded()) {
                startComputerTurn(); // Повторна спроба лише якщо гра не закінчена
            }
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

        if (sunk && !gameLogic.isGameEnded()) {
            System.out.println("Checking game end after sunk ship at row=" + row + ", col=" + col + " at " + new java.util.Date());
            gameLogic.checkGameEnd();
        }
        return hit;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }

    // Оновлюємо метод для зупинки AI
    public void stopAI() {
        System.out.println("AI stopped at " + new java.util.Date());
        if (timer != null) {
            timer.stop();
            System.out.println("Timer stopped in stopAI at " + new java.util.Date());
        }
    }
}