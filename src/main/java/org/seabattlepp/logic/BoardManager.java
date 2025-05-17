package org.seabattlepp.logic;

import java.awt.*;

import javax.swing.plaf.basic.BasicButtonUI;
import java.util.List;

import org.seabattlepp.ships.Ship;
import org.seabattlepp.ships.ShipPlacer;
import org.seabattlepp.ships.ShipValidator;
import org.seabattlepp.gui.ShipButton;

public class BoardManager {
    // розташування кораблів на полях
    public Ship[][] computerShipsLocations;
    public Ship[][] playerShipsLocations;
    // кнопки gui, що відповідають клітинкам на полі
    public final ShipButton[][] computerShipButtons;
    public final ShipButton[][] playerShipButtons;
    // масиви які зберігають історію пострілів
    public final int[][] playerTargetedArea;
    public final int[][] computerTargetedArea;
    public boolean isRandomButtonPressed;

    public BoardManager(ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.computerShipButtons = computerShipButtons;
        this.playerShipButtons = playerShipButtons;
        this.playerTargetedArea = new int[11][11];
        this.computerTargetedArea = new int[11][11];
        this.isRandomButtonPressed = false;
    }

    // рандомне розміщення кораблів на лівій панелі
    public void placeShipsRandomlyOnLeftBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        resetPlayerShipsLocations();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                playerShipsLocations[row][col] = ship;
            }
        }
        clearLeftBoardShips();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0], col = coord[1];
                ShipButton button = playerShipButtons[row][col];
                if (button != null) {
                    button.setText("⚓");
                    button.setFont(new Font("Inter", Font.BOLD, 50));
                    button.setForeground(Color.BLACK);
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setIcon(null);
                }
            }
        }
    }

    // рандомне розміщення кораблів на правій панелі
    public void placeShipsRandomlyOnRightBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        resetComputerShipsLocations();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                computerShipsLocations[row][col] = ship;
            }
        }
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

    // очищення поля гравця
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

    // очищення поля комп'ютера
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

    // увімкнення клітинок для пострілу по комп'ютеру
    public void enableComputerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = computerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED && !isPlayerShotAt(i, j) && !"•".equals(button.getText())) {
                    button.setEnabled(isRandomButtonPressed);
                }
            }
        }
    }

    // увімкнення клітинок для по стрілу по гравцю
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

    // вимкнення клітинок для постріла гравця по комп'ютеру
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

    // вимкнення клітинок для постріла комп'ютера по гравцю
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

    // увімкнення стрільби після натискання кнопки рандом
    public void enableShootingAfterRandom() {
        isRandomButtonPressed = true;
        enableComputerButtons();
    }

    // скидання розміщення кораблів комп'ютера
    public void resetComputerShipsLocations() {
        computerShipsLocations = new Ship[11][11];
    }

    // скидання розміщення кораблів гравця
    public void resetPlayerShipsLocations() {
        playerShipsLocations = new Ship[11][11];
    }

    /*
    Доп методи по перевірці на постріл:
     */
    public boolean isPlayerShotAt(int row, int col) {
        return playerTargetedArea[row][col] == 1;
    }

    public boolean isComputerShotAt(int row, int col) {
        return computerTargetedArea[row][col] == 1;
    }
}
