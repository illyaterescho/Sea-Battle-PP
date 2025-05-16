//package org.seabattlepp.logic;
//
//import java.awt.*;
//import org.seabattlepp.logic.GameLogic;
//import org.seabattlepp.ships.Ship;
//import org.seabattlepp.ships.ShipPlacer;
//import org.seabattlepp.ships.ShipValidator;
//import org.seabattlepp.gui.ShipButton;
//
//import javax.swing.plaf.basic.BasicButtonUI;
//import java.util.List;
//
//
//public class BoardManager {
//    private final GameLogic gameLogic;
//    private final AILogic aiLogic;
//
//
//    public BoardManager() {
//        this.gameLogic = new GameLogic();
//    }
//
//    // Розміщує кораблі випадковим чином на лівому полі (гравця).
//    public void placeShipsRandomlyOnLeftBoard() {
//        ShipPlacer placer = new ShipPlacer(new ShipValidator());
//        List<Ship> placedShips = placer.placeShipsRandomly();
//        gameLogic.resetPlayerShipsLocations();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0];
//                int col = coord[1];
//                gameLogic.playerShipsLocations[row][col] = ship;
//            }
//        }
//        clearLeftBoardShips();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0], col = coord[1];
//                ShipButton button = gameLogic.playerShipButtons[row][col];
//                if (button != null) {
//                    button.setText("⚓");
//                    button.setFont(new Font("Inter", Font.BOLD, 50));
//                    button.setForeground(Color.BLACK);
//                    button.setBackground(Color.WHITE);
//                    button.setEnabled(true);
//                    button.setIcon(null);
//                }
//            }
//        }
//    }
//
//    public void placeShipsRandomlyOnRightBoard() {
//        ShipPlacer placer = new ShipPlacer(new ShipValidator());
//        List<Ship> placedShips = placer.placeShipsRandomly();
//        gameLogic.resetComputerShipsLocations();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0];
//                int col = coord[1];
//                gameLogic.computerShipsLocations[row][col] = ship;
//            }
//        }
//        clearRightBoardShips();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0], col = coord[1];
//                ShipButton button = gameLogic.computerShipButtons[row][col];
//                if (button != null) {
//                    button.setBackground(Color.WHITE);
//                    button.setFont(new Font("Inter", Font.BOLD, 25));
//                    button.setEnabled(true);
//                    button.setOpaque(false);
//                    button.setIcon(null);
//                }
//            }
//        }
//    }
//
//
//    public void clearLeftBoardShips() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                ShipButton button = gameLogic.playerShipButtons[i][j];
//                if (button != null) {
//                    button.setText("");
//                    button.setIcon(null);
//                    button.setBackground(Color.WHITE);
//                    button.setEnabled(true);
//                    button.setOpaque(false);
//                    button.setUI(new BasicButtonUI());
//                }
//            }
//        }
//    }
//
//    public void clearRightBoardShips() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                ShipButton button = gameLogic.computerShipButtons[i][j];
//                if (button != null) {
//                    button.setText("");
//                    button.setIcon(null);
//                    button.setBackground(Color.WHITE);
//                    button.setEnabled(true);
//                    button.setOpaque(false);
//                    button.setUI(new BasicButtonUI());
//                }
//            }
//        }
//    }
//
//    public void resetBoards() {
//        clearLeftBoardShips();
//        clearRightBoardShips();
//        gameLogic.isGameStarted = false;
//        gameLogic.isRandomButtonPressed = false;
//        gameLogic.resetComputerShipsLocations();
//        gameLogic.resetPlayerShipsLocations();
//        aiLogic.resetAI();
//        gameLogic.isPlayerTurn = true;
//        gameLogic.isRandomButtonPressed = false;
//        for (int i = 0; i <= 10; i++) {
//            for (int j = 0; j <= 10; j++) {
//                gameLogic.playerTargetedArea[i][j] = 0;
//                gameLogic.computerTargetedArea[i][j] = 0;
//            }
//        }
//        enablePlayerButtonsForPlacement();
//        disableComputerButtons();
//    }
//
//
//    public void enablePlayerButtonsForPlacement() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                ShipButton button = playerShipButtons[i][j];
//                if (button != null) {
//                    button.setEnabled(true);
//                }
//            }
//        }
//    }
//
//    public void enableComputerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                ShipButton button = computerShipButtons[i][j];
//                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED && !isPlayerShotAt(i, j)) {
//                    if (isRandomButtonPressed) {
//                        button.setEnabled(true);
//                    } else {
//                        button.setEnabled(false);
//                    }
//                }
//            }
//        }
//    }
//
//    public void enablePlayerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                ShipButton button = playerShipButtons[i][j];
//                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED) {
//                    if (button.getText() == null || !button.getText().equals("⚓")) {
//                        button.setEnabled(true);
//                    }
//                }
//            }
//        }
//    }
//
//    public void disableComputerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                ShipButton button = computerShipButtons[i][j];
//                if (button != null) {
//                    button.setEnabled(false);
//                }
//            }
//        }
//    }
//
//    public void disablePlayerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                ShipButton button = playerShipButtons[i][j];
//                if (button != null) {
//                    if (button.getText() == null || !button.getText().equals("⚓")) {
//                        button.setEnabled(false);
//                    }
//                }
//            }
//        }
//    }
//}
