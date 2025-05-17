package org.seabattlepp.logic;

import javax.swing.*;

import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.gui.MainFrame;

public class GameLogic {

    public MainFrame mainFrame;
    public boolean isGameStarted;
    public boolean isPlayerTurn;
    public boolean isGameEnded;

    public GameLogic(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.isPlayerTurn = true;
        this.isGameStarted = false;
        this.isGameEnded = false;
        mainFrame.boardManager.resetComputerShipsLocations();
        mainFrame.boardManager.resetPlayerShipsLocations();
    }

    public void resetBoards() {
        mainFrame.boardManager.clearLeftBoardShips();
        mainFrame.boardManager.clearRightBoardShips();
        isGameStarted = false;
        mainFrame.boardManager.isRandomButtonPressed = false;
        mainFrame.boardManager.resetComputerShipsLocations();
        mainFrame.boardManager.resetPlayerShipsLocations();
        mainFrame.aiLogic.resetBot();
        isPlayerTurn = true;
        mainFrame.boardManager.isRandomButtonPressed = false;
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                mainFrame.boardManager.playerTargetedArea[i][j] = 0;
                mainFrame.boardManager.computerTargetedArea[i][j] = 0;
            }
        }
        mainFrame.boardManager.disableComputerButtons();
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public void processPlayerShot(int row, int col) {
        if (!isPlayerTurn || isGameEnded) return;

        if (!mainFrame.boardManager.isRandomButtonPressed) {
            return;
        }

        if (mainFrame.boardManager.isPlayerShotAt(row, col)) {
            return;
        }

        if (row >= 1 && row <= 10 && col >= 1 && col <= 10) {
            mainFrame.boardManager.playerTargetedArea[row][col] = 1;
        }

        Ship ship = mainFrame.boardManager.computerShipsLocations[row][col];
        boolean hit = ship != null;
        boolean sunk = false;

        if (hit) {
            sunk = mainFrame.uiMarkingLogic.markHit(row, col, ship);
            checkGameEnd();
        } else {
            if (mainFrame.boardManager.computerShipButtons[row][col] != null) {
                mainFrame.uiMarkingLogic.markMiss(row, col);
            }
            setPlayerTurn(false);
            mainFrame.boardManager.disableComputerButtons();
            mainFrame.boardManager.enablePlayerButtons();
            if (isGameStarted && !isGameEnded) {
                this.startComputerTurn();
            }
        }

        ShipButton button = mainFrame.boardManager.computerShipButtons[row][col];
        if (button != null) {
            button.setEnabled(false);
        }

        // Деактивуємо кнопку "Рандом" після пострілу
        if (mainFrame.randomButton != null) {
            mainFrame.randomButton.setEnabled(false);
        }

        if (hit && !sunk && isGameStarted && !isGameEnded) {
            mainFrame.boardManager.enableComputerButtons();
        }
    }

    public void startPlayerTurn() {
        if (isGameEnded) return;
        setPlayerTurn(true);
        if (mainFrame.randomButton != null) {
            mainFrame.randomButton.setEnabled(true);
        }
        mainFrame.boardManager.isRandomButtonPressed = false;
    }

    public void startGame() {
        if (isGameEnded) {
            isGameEnded = false;
        }
        isPlayerTurn = true;
        setGameStarted(true);
        mainFrame.boardManager.disableComputerButtons();
        mainFrame.boardManager.disablePlayerButtons();
        mainFrame.aiLogic.resetBot();
        if (mainFrame.randomButton != null) {
            mainFrame.randomButton.setEnabled(true);
        }
        startPlayerTurn();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int row = i;
                int col = j;
                ShipButton button = mainFrame.boardManager.computerShipButtons[row][col];

                if (button != null) {
                    for (var l : button.getActionListeners()) {
                        button.removeActionListener(l);
                    }

                    button.addActionListener(e -> {
                        if (button.isEnabled()) {
                            processPlayerShot(row, col);
                        }
                    });
                }
            }
        }
    }

    public void setGameStarted(boolean started) {
        isGameStarted = started;
        if (started && !this.isPlayerTurn && !isGameEnded) {
            this.startComputerTurn();
        }
    }

    public void startComputerTurn() {
        if (isGameEnded) return;
        mainFrame.aiLogic.startComputerTurn();
    }

    public boolean checkGameEnd() {
        if (isGameEnded) return false;

        boolean playerSunk = true;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (mainFrame.boardManager.playerShipsLocations[i][j] != null && !mainFrame.boardManager.playerShipsLocations[i][j].isSunk()) {
                    playerSunk = false;
                    break;
                }
            }
            if (!playerSunk) break;
        }
        if (playerSunk) {
            endGame(false);
            return playerSunk;
        }

        boolean computerSunk = true;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (mainFrame.boardManager.computerShipsLocations[i][j] != null && !mainFrame.boardManager.computerShipsLocations[i][j].isSunk()) {
                    computerSunk = false;
                    break;
                }
            }
            if (!computerSunk) break;
        }
        if (computerSunk) {
            endGame(true);
        }
        return playerSunk;
    }

    private void endGame(boolean playerWon) {
        if (isGameEnded) return;
        isGameEnded = true;
        SwingUtilities.invokeLater(() -> {
            String message = playerWon ? "Ви перемогли!" : "Комп'ютер переміг!";
            JOptionPane.showMessageDialog(mainFrame, message, "Кінець гри!", JOptionPane.INFORMATION_MESSAGE);
            isGameStarted = false;
            if (mainFrame.randomButton != null) {
                mainFrame.randomButton.setEnabled(false);
            }
            if (mainFrame.startButton != null) {
                mainFrame.startButton.setEnabled(true);
            }
            mainFrame.aiLogic.stopTimer();
            resetBoards();
            mainFrame.aiLogic.resetBot();
            isGameEnded = false;
        });
    }


    public boolean isCellAvailableForShot(int row, int col) {
        ShipButton button = mainFrame.boardManager.playerShipButtons[row][col];
        if (button != null) {
            String text = button.getText();
            if ("•".equals(text)) {
                return false;
            }
            return button.getIcon() == null;
        }
        return false;
    }
}
