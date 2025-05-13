/*
package org.seabattlepp.logic;

import org.seabattlepp.gui.BoardPanel;
import org.seabattlepp.logic.ai.AILogic;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;

import javax.swing.*;
import java.util.List;

public class GameLogic {

    private final BoardPanel boardPanel;
    private final ShipButton[][] computerShipButtons;
    private Ship[][] computerShipsLocations;
    private Ship[][] playerShipsLocations;
    private boolean isPlayerTurn = true;

    private final AILogic aiLogic;
    private final PlayerLogic playerLogic;
    private final UIMarkingLogic uiMarkingLogic; // Додаємо UIMarkingLogic

    public GameLogic(BoardPanel boardPanel, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.boardPanel = boardPanel;
        this.computerShipButtons = computerShipButtons;
        this.computerShipsLocations = new Ship[11][11];
        this.playerShipsLocations = new Ship[11][11];
        this.aiLogic = new AILogic(this, boardPanel, playerShipButtons); // AILogic
        this.playerLogic = new PlayerLogic(this, boardPanel, computerShipButtons); // PlayerLogic
        this.uiMarkingLogic = new UIMarkingLogic(this, boardPanel, computerShipButtons, playerShipButtons); // UIMarkingLogic - ініціалізація
        this.isPlayerTurn = true; // початково встановлюємо хід гравця тут в конструкторі
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    // встановлення черги гравця використовуємо AILogic та PlayerLogic
    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public void startGame() {
        isPlayerTurn = true;
        boardPanel.setGameStarted(true);
        boardPanel.enableComputerButtons();
        boardPanel.disablePlayerButtons();
        aiLogic.resetAI(); // Використовуємо AILogic для скидання AI
        playerLogic.startPlayerTurn(); // Починаємо хід гравця через PlayerLogic
    }


    public void startComputerTurn() {
        aiLogic.startComputerTurn(); // початок ходу комп'ютера до AILogic
    }

    public void startPlayerTurn() {
        playerLogic.startPlayerTurn(); // початок ходу гравця до PlayerLogic
    }


    public void processShot(int row, int col) {
        playerLogic.processShot(row, col); // бробку пострілу гравця до PlayerLogic
    }

    // отримання корабля комп'ютера за координатами використовується PlayerLogic
    public Ship getShipAt(int row, int col) {
        return computerShipsLocations[row][col];
    }

    // отримання корабля гравця за координатами використовується AILogic
    public Ship getPlayerShipAt(int row, int col) {
        return playerShipsLocations[row][col];
    }


    public boolean markHit(int row, int col, Ship ship) {
        boolean sunk = false;
        if (computerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();

            if (sunk) {
                uiMarkingLogic.markSunkShip(ship); // Використовуємо UIMarkingLogic
            } else {
                uiMarkingLogic.markHitSymbol(computerShipButtons[row][col]); // Використовуємо UIMarkingLogic
            }
        }
        return sunk;
    }

    public boolean markHitPlayerBoard(int row, int col, Ship ship) {
        boolean sunk = false;
        if (boardPanel.playerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();

            if (sunk) {
                uiMarkingLogic.markSunkShipPlayerBoard(ship); // Використовуємо UIMarkingLogic
            } else {
                uiMarkingLogic.markHitSymbolPlayerBoard(boardPanel.playerShipButtons[row][col]); // Використовуємо UIMarkingLogic
            }
        }
        return sunk;
    }


    void markMiss(int row, int col) {
        if (computerShipButtons[row][col] != null) {
            uiMarkingLogic.markMiss(row, col); // Використовуємо UIMarkingLogic
        }
    }

    public void markMissPlayerBoard(int row, int col) {
        if (boardPanel.playerShipButtons[row][col] != null) {
            uiMarkingLogic.markMissPlayerBoard(row, col); // Використовуємо UIMarkingLogic
        }
    }


    public void resetComputerShipsLocations() {
        computerShipsLocations = new Ship[11][11];
    }

    public void resetPlayerShipsLocations() {
        playerShipsLocations = new Ship[11][11];
    }


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


    public void checkGameEnd() {
        boolean computerSunk = computerShipsLocationsIsEmpty();
        boolean playerSunk = playerShipsLocationsIsEmpty();

        if (computerSunk) {
            endGame(true);
        } else if (playerSunk) {
            endGame(false);
        }
    }

    private void endGame(boolean playerWon) {
        if (playerWon) {
            JOptionPane.showMessageDialog(boardPanel.MainFrame, "Ви перемогли!", "Кінець гри!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(boardPanel.MainFrame, "Комп'ютер переміг!", "Кінець гри!", JOptionPane.INFORMATION_MESSAGE);
        }
        boardPanel.setGameStarted(false);
        boardPanel.MainFrame.disableRandomButton();
        boardPanel.resetBoards();
        aiLogic.resetAI(); // AILogic для скидання AI
    }

    public void resetGame() {
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
        aiLogic.resetAI(); // AILogic для скидання AI
        isPlayerTurn = true; // після скидання гри гравець починає першим
    }

    private boolean isValidCell(int row, int col) {
        return row > 0 && row <= 10 && col > 0 && col <= 10;
    }
}*/
