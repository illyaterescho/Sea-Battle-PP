package org.seabattlepp.logic;

import org.seabattlepp.logic.ai.AILogic;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.gui.MainFrame;
import org.seabattlepp.ships.ShipPlacer;
import org.seabattlepp.ships.ShipValidator;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.List;

public class GameLogic {

    private final MainFrame mainFrame;
    private final ShipButton[][] computerShipButtons;
    private final ShipButton[][] playerShipButtons;
    private Ship[][] computerShipsLocations;
    private Ship[][] playerShipsLocations;
    private boolean isGameStarted;
    private boolean isPlayerTurn;
    private final AILogic aiLogic;
    private final UIMarkingLogic uiMarkingLogic;
    private int[][] playerTargetedArea;

    public GameLogic(MainFrame mainFrame, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.mainFrame = mainFrame;
        this.computerShipButtons = computerShipButtons;
        this.playerShipButtons = playerShipButtons;
        this.aiLogic = new AILogic(this, playerShipButtons);
        this.uiMarkingLogic = new UIMarkingLogic(this, computerShipButtons, playerShipButtons);
        this.isPlayerTurn = true;
        this.isGameStarted = false;
        this.playerTargetedArea = new int[11][11];
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
    }

    public void placeShipsRandomlyOnLeftBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        setPlayerShips(placedShips);
        clearLeftBoardShips();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0], col = coord[1];
                ShipButton btn = playerShipButtons[row][col];
                if (btn != null) {
                    btn.setText("⚓");
                    btn.setFont(new Font("Inter", Font.BOLD, 50));
                    btn.setForeground(Color.BLACK);
                    btn.setBackground(Color.WHITE);
                    btn.setEnabled(true);
                    btn.setIcon(null);
                }
            }
        }
    }

    public void placeShipsRandomlyOnRightBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        setComputerShips(placedShips);
        clearRightBoardShips();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0], col = coord[1];
                ShipButton button = computerShipButtons[row][col];
                if (button != null) {
                    button.setBackground(Color.WHITE);
                    button.setFont(new Font("Inter", Font.BOLD, 25));
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setIcon(null);
                }
            }
        }
    }

    public void clearLeftBoardShips() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null) {
                    button.setText("");
                    button.setIcon(null);
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setUI(new BasicButtonUI());
                }
            }
        }
    }

    public void clearRightBoardShips() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = computerShipButtons[i][j];
                if (button != null) {
                    button.setText("");
                    button.setIcon(null);
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setUI(new BasicButtonUI());
                }
            }
        }
    }

    public void resetBoards() {
        clearLeftBoardShips();
        clearRightBoardShips();
        isGameStarted = false;
        resetGame();
        enablePlayerButtonsForPlacement();
        disableComputerButtons();
    }

    public void setGameStarted(boolean started) {
        isGameStarted = started;
        if (started && !isPlayerTurn()) this.startComputerTurn();
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void enablePlayerButtonsForPlacement() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null) {
                    button.setEnabled(true);
                }
            }
        }
    }

    public void enableComputerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = computerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED && !isPlayerShotAt(i, j)) {
                    button.setEnabled(true);
                }
            }
        }
    }

    public void enablePlayerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED) {
                    if (button.getText() == null || !button.getText().equals("⚓")) {
                        button.setEnabled(true);
                    }
                }
            }
        }
    }

    public void disableComputerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = computerShipButtons[i][j];
                if (button != null) {
                    button.setEnabled(false);
                }
            }
        }
    }

    public void disablePlayerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null) {
                    if (button.getText() == null || !button.getText().equals("⚓")) {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
        System.out.println("Turn set to: " + (playerTurn ? "Player" : "Computer"));
    }

    public void processPlayerShot(int row, int col) {
        if (!isPlayerTurn) return;

        // Перевірка на повторний постріл у ту саму клітинку
        if (isPlayerShotAt(row, col)) {
            System.out.println("Player tried to shoot at already used cell: row=" + row + ", col=" + col);
            return;
        }

        markPlayerShot(row, col);

        Ship ship = getShipAt(row, col);
        boolean hit = ship != null;
        boolean sunk = false;

        System.out.println("Player shot: row=" + row + ", col=" + col + ", hit=" + hit);

        if (hit) {
            sunk = markHit(row, col, ship);
            checkGameEnd();
        } else {
            System.out.println("Player missed, marking miss at row=" + row + ", col=" + col);
            markMiss(row, col);
            setPlayerTurn(false);
            disableComputerButtons();
            enablePlayerButtons();
            if (isGameStarted) {
                this.startComputerTurn();
            }
        }

        // Деактивація кнопки після пострілу
        ShipButton button = computerShipButtons[row][col];
        if (button != null) {
            button.setEnabled(false);
        }

        if (hit && !sunk && isGameStarted) {
            enableComputerButtons(); // Залишаємо кнопки активними для повторного ходу
        }
    }

    public void startPlayerTurn() {
        setPlayerTurn(true);
        enableComputerButtons();
        System.out.println("Player turn started");
    }

    public void startGame() {
        isPlayerTurn = true;
        setGameStarted(true);
        enableComputerButtons();
        disablePlayerButtons();
        aiLogic.resetAI();
        startPlayerTurn();
        addComputerBoardListeners();
        System.out.println("Game started, player turn: " + isPlayerTurn);
    }

    private void addComputerBoardListeners() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int row = i;
                int col = j;
                ShipButton button = computerShipButtons[row][col];

                if (button != null) {
                    for (var l : button.getActionListeners()) {
                        button.removeActionListener(l);
                    }

                    button.addActionListener(e -> {
                        if (button.isEnabled()) {
                            System.out.println("Player clicked: row=" + row + ", col=" + col);
                            processPlayerShot(row, col);
                        }
                    });
                } else {
                    System.err.println("Button is null at computerShipButtons[" + row + "][" + col + "]");
                }
            }
        }
    }

    public void startComputerTurn() {
        aiLogic.startComputerTurn();
    }

    public Ship getShipAt(int row, int col) {
        return computerShipsLocations[row][col];
    }

    public Ship getPlayerShipAt(int row, int col) {
        return playerShipsLocations[row][col];
    }

    public boolean markHit(int row, int col, Ship ship) {
        boolean sunk = false;
        if (computerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();
            if (sunk) {
                uiMarkingLogic.markSunkShip(ship);
            } else {
                uiMarkingLogic.markHitSymbol(computerShipButtons[row][col]);
            }
        }
        return sunk;
    }

    public boolean markHitPlayerBoard(int row, int col, Ship ship) {
        boolean sunk = false;
        if (playerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();
            if (sunk) {
                uiMarkingLogic.markSunkShipPlayerBoard(ship);
            } else {
                uiMarkingLogic.markHitSymbolPlayerBoard(playerShipButtons[row][col]);
            }
        }
        return sunk;
    }

    void markMiss(int row, int col) {
        if (computerShipButtons[row][col] != null) {
            uiMarkingLogic.markMiss(row, col);
        }
    }

    public void markMissPlayerBoard(int row, int col) {
        if (playerShipButtons[row][col] != null) {
            uiMarkingLogic.markMissPlayerBoard(row, col);
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
        JOptionPane.showMessageDialog(
                mainFrame,
                playerWon ? "Ви перемогли!" : "Комп'ютер переміг!",
                "Кінець гри!",
                JOptionPane.INFORMATION_MESSAGE
        );
        setGameStarted(false);
        mainFrame.disableRandomButton();
        resetBoards();
        aiLogic.resetAI();
    }

    public void resetGame() {
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
        aiLogic.resetAI();
        isPlayerTurn = true;
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                playerTargetedArea[i][j] = 0;
            }
        }
    }

    public boolean isPlayerShotAt(int row, int col) {
        return playerTargetedArea[row][col] == 1;
    }

    private void markPlayerShot(int row, int col) {
        if (row >= 1 && row <= 10 && col >= 1 && col <= 10) {
            playerTargetedArea[row][col] = 1;
        }
    }
}