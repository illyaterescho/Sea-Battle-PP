package org.seabattlepp.logic;

import org.seabattlepp.logic.ai.AILogic;
import org.seabattlepp.ships.BoardController;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.gui.MainFrame;

import javax.swing.*;
import java.util.List;

public class GameLogic {

    private final BoardController boardController;
    private final MainFrame mainFrame;
    private final ShipButton[][] computerShipButtons;
    private final ShipButton[][] playerShipButtons;
    private Ship[][] computerShipsLocations;
    private Ship[][] playerShipsLocations;
    private boolean isPlayerTurn;

    private final AILogic aiLogic;
    private final PlayerLogic playerLogic;
    private final UIMarkingLogic uiMarkingLogic; // Додаємо UIMarkingLogic

    public GameLogic(MainFrame mainFrame, BoardController boardController, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.boardController = boardController;
        this.computerShipButtons = computerShipButtons;
        this.playerShipButtons = playerShipButtons;
        this.mainFrame = mainFrame;
        this.aiLogic = new AILogic(this, boardController, playerShipButtons);
        this.playerLogic = new PlayerLogic(this, boardController, computerShipButtons);
        this.uiMarkingLogic = new UIMarkingLogic(this, boardController, computerShipButtons, playerShipButtons);
        this.isPlayerTurn = true; // Гравець починає
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    // встановлення черги гравця використовуємо AILogic та PlayerLogic
    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    // ▶️ Старт гри
    public void startGame() {
        isPlayerTurn = true;
        boardController.setGameStarted(true);
        boardController.enableComputerButtons();
        boardController.disablePlayerButtons();
        aiLogic.resetAI(); // Використовуємо AILogic для скидання AI
        playerLogic.startPlayerTurn(); // Починаємо хід гравця через PlayerLogic
    }

    // ⏩ Хід комп'ютера
    public void startComputerTurn() {
        aiLogic.startComputerTurn(); // початок ходу комп'ютера до AILogic
    }

    // 🔁 Повернення ходу гравцю
    public void startPlayerTurn() {
        playerLogic.startPlayerTurn();
    }

    // 🔫 Обробка кліку гравця по комп’ютеру
    public void processShot(int row, int col) {
        playerLogic.processShot(row, col); // бробку пострілу гравця до PlayerLogic
    }

    // отримання корабля комп'ютера за координатами використовується PlayerLogic
    public Ship getShipAt(int row, int col) {
        return computerShipsLocations[row][col];
    }

    // Отримати корабель гравця за координатами
    public Ship getPlayerShipAt(int row, int col) {
        return playerShipsLocations[row][col];
    }

    // ✅ Обробка попадання гравця по кораблю комп’ютера
    public boolean markHit(int row, int col, Ship ship) {
        boolean sunk = false;
        if (computerShipButtons[row][col] != null) {
            ship.takeHit();           // зафіксували влучання
            sunk = ship.isSunk();     // чи потоплено

            if (sunk) {
                uiMarkingLogic.markSunkShip(ship); // показати потоплення
            } else {
                uiMarkingLogic.markHitSymbol(computerShipButtons[row][col]); // звичайне влучання
            }
        }
        return sunk;
    }

    // ✅ Обробка попадання ШІ по кораблю гравця
    public boolean markHitPlayerBoard(int row, int col, Ship ship) {
        boolean sunk = false;
        if (playerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();

            if (sunk) {
                uiMarkingLogic.markSunkShipPlayerBoard(ship); // Використовуємо UIMarkingLogic
            } else {
                uiMarkingLogic.markHitSymbolPlayerBoard(playerShipButtons[row][col]);            }
        }
        return sunk;
    }

    // ❌ Промах гравця
    void markMiss(int row, int col) {
        if (computerShipButtons[row][col] != null) {
            uiMarkingLogic.markMiss(row, col); // Використовуємо UIMarkingLogic
        }
    }

    // ❌ Промах комп’ютера
    public void markMissPlayerBoard(int row, int col) {
        if (playerShipButtons[row][col] != null) {
            uiMarkingLogic.markMissPlayerBoard(row, col); // Використовуємо UIMarkingLogic
        }
    }

    // 🔄 Скидання координат кораблів комп’ютера
    public void resetComputerShipsLocations() {
        computerShipsLocations = new Ship[11][11];
    }

    // 🔄 Скидання координат кораблів гравця
    public void resetPlayerShipsLocations() {
        playerShipsLocations = new Ship[11][11];
    }

    // ↩️ Завантажити розміщення кораблів комп’ютера
    public void setComputerShips(List<Ship> ships) {
        resetComputerShipsLocations();
        for (Ship ship : ships) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                computerShipsLocations[row][col] = ship;
            }
        }
    }

    // ↩️ Завантажити розміщення кораблів гравця
    public void setPlayerShips(List<Ship> ships) {
        resetPlayerShipsLocations();
        for (Ship ship : ships) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                playerShipsLocations[row][col] = ship;
            }
        }
    }


    // 📉 Чи всі кораблі комп’ютера потоплено?
    private boolean computerShipsLocationsIsEmpty() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (computerShipsLocations[i][j] != null && !computerShipsLocations[i][j].isSunk()) {
                    return false;
                }
            }
        }
        return true;
    }

    // 📉 Чи всі кораблі гравця потоплено?
    private boolean playerShipsLocationsIsEmpty() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerShipsLocations[i][j] != null && !playerShipsLocations[i][j].isSunk()) {
                    return false;
                }
            }
        }
        return true;
    }


    // 🧠 Перевірка завершення гри
    public void checkGameEnd() {
        boolean computerSunk = computerShipsLocationsIsEmpty();
        boolean playerSunk = playerShipsLocationsIsEmpty();

        if (computerSunk) {
            endGame(true);
        } else if (playerSunk) {
            endGame(false);
        }
    }

    // 🎯 Завершення гри: показ повідомлення, скидання стану
    private void endGame(boolean playerWon) {
        if (playerWon) {
            JOptionPane.showMessageDialog(mainFrame, "Ви перемогли!", "Кінець гри!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Комп'ютер переміг!", "Кінець гри!", JOptionPane.INFORMATION_MESSAGE);
        }
        boardController.setGameStarted(false);
        mainFrame.disableRandomButton(); // прямо через MainFrame
        boardController.resetBoards();
        aiLogic.resetAI();
    }

    // 🔄 Повне скидання гри
    public void resetGame() {
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
        aiLogic.resetAI(); // AILogic для скидання AI
        isPlayerTurn = true; // після скидання гри гравець починає першим
    }
}
