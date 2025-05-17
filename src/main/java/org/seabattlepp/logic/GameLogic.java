package org.seabattlepp.logic;

import javax.swing.*;

import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.gui.MainFrame;

public class GameLogic {

    private final MainFrame mainFrame;
    public BoardManager boardManager;
    public boolean isGameStarted;
    public boolean isPlayerTurn;
    public AILogic aiLogic;
    public UIMarkingLogic uiMarkingLogic;
    public boolean isGameEnded;

    public GameLogic(MainFrame mainFrame, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.mainFrame = mainFrame;
        this.boardManager = new BoardManager(computerShipButtons, playerShipButtons);
        this.aiLogic = new AILogic(this, boardManager.playerShipButtons);
        this.uiMarkingLogic = new UIMarkingLogic(this);
        this.isPlayerTurn = true;
        this.isGameStarted = false;
        this.isGameEnded = false;
        boardManager.resetComputerShipsLocations();
        boardManager.resetPlayerShipsLocations();
    }

    public void resetBoards() {
        boardManager.clearLeftBoardShips();
        boardManager.clearRightBoardShips();
        isGameStarted = false;
        boardManager.isRandomButtonPressed = false;
        boardManager.resetComputerShipsLocations();
        boardManager.resetPlayerShipsLocations();
        aiLogic.resetBot();
        isPlayerTurn = true;
        boardManager.isRandomButtonPressed = false;
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                boardManager.playerTargetedArea[i][j] = 0;
                boardManager.computerTargetedArea[i][j] = 0;
            }
        }
        boardManager.disableComputerButtons();
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public void processPlayerShot(int row, int col) {
        if (!isPlayerTurn || isGameEnded) return;

        if (!boardManager.isRandomButtonPressed) {
            return;
        }

        if (boardManager.isPlayerShotAt(row, col)) {
            return;
        }

        if (row >= 1 && row <= 10 && col >= 1 && col <= 10) {
            boardManager.playerTargetedArea[row][col] = 1;
        }

        Ship ship = boardManager.computerShipsLocations[row][col];
        boolean hit = ship != null;
        boolean sunk = false;

        if (hit) {
            sunk = uiMarkingLogic.markHit(row, col, ship);
            checkGameEnd();
        } else {
            if (boardManager.computerShipButtons[row][col] != null) {
                uiMarkingLogic.markMiss(row, col);
            }
            setPlayerTurn(false);
            boardManager.disableComputerButtons();
            boardManager.enablePlayerButtons();
            if (isGameStarted && !isGameEnded) {
                this.startComputerTurn();
            }
        }

        ShipButton button = boardManager.computerShipButtons[row][col];
        if (button != null) {
            button.setEnabled(false);
        }

        // Деактивуємо кнопку "Рандом" після пострілу
        if (mainFrame.randomButton != null) {
            mainFrame.randomButton.setEnabled(false);
        }

        if (hit && !sunk && isGameStarted && !isGameEnded) {
            boardManager.enableComputerButtons();
        }
    }

    public void startPlayerTurn() {
        if (isGameEnded) return;
        setPlayerTurn(true);
        if (mainFrame.randomButton != null) {
            mainFrame.randomButton.setEnabled(true);
        }
        boardManager.isRandomButtonPressed = false;
    }

    public void startGame() {
        if (isGameEnded) {
            isGameEnded = false;
        }
        isPlayerTurn = true;
        setGameStarted(true);
        boardManager.disableComputerButtons();
        boardManager.disablePlayerButtons();
        aiLogic.resetBot();
        if (mainFrame.randomButton != null) {
            mainFrame.randomButton.setEnabled(true);
        }
        startPlayerTurn();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int row = i;
                int col = j;
                ShipButton button = boardManager.computerShipButtons[row][col];

                if (button != null) {
                    for (var l : button.getActionListeners()) {
                        button.removeActionListener(l);
                    }

                    button.addActionListener(e -> {
                        if (button.isEnabled()) {
                            processPlayerShot(row, col);
                        }
                    });
                } else {
                    System.err.println("Button is null at computerShipButtons[" + row + "][" + col + "]");
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
        aiLogic.startComputerTurn();
    }

    public void checkGameEnd() {
        if (isGameEnded) return;

        boolean playerSunk = true;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (boardManager.playerShipsLocations[i][j] != null && !boardManager.playerShipsLocations[i][j].isSunk()) {
                    playerSunk = false;
                    break;
                }
            }
            if (!playerSunk) break;
        }
        if (playerSunk) {
            endGame(false);
            return;
        }

        boolean computerSunk = true;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (boardManager.computerShipsLocations[i][j] != null && !boardManager.computerShipsLocations[i][j].isSunk()) {
                    computerSunk = false;
                    break;
                }
            }
            if (!computerSunk) break;
        }
        if (computerSunk) {
            endGame(true);
        }
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
            aiLogic.stopTimer();
            resetBoards();
            aiLogic.resetBot();
            isGameEnded = false;
        });
    }


    public boolean isCellAvailableForShot(int row, int col) {
        ShipButton button = boardManager.playerShipButtons[row][col];
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
