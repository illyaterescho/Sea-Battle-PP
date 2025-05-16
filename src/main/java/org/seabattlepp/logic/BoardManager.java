//package org.seabattlepp.logic;
//
////import javax.swing.*;
////import javax.swing.plaf.basic.BasicButtonUI;
////import java.awt.*;
////import java.util.List;
//
//import org.seabattlepp.logic.GameLogic;
//
//
//public class BoardManager {
//
//
//    public BoardManager(MainFrame mainFrame, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
//        this.mainFrame = mainFrame;
//        this.computerShipButtons = computerShipButtons;
//        this.playerShipButtons = playerShipButtons;
//        this.aiLogic = new AILogic(this, playerShipButtons);
//        this.uiMarkingLogic = new UIMarkingLogic(this, computerShipButtons, playerShipButtons);
//        this.isPlayerTurn = true;
//        this.isGameStarted = false;
//        this.isRandomButtonPressed = false;
//        this.isGameEnded = false;
//        this.playerTargetedArea = new int[11][11];
//        this.computerTargetedArea = new int[11][11];
//        resetComputerShipsLocations();
//        resetPlayerShipsLocations();
//    }
//
//    // Розміщує кораблі випадковим чином на лівому полі (гравця).
//    public void placeShipsRandomlyOnLeftBoard() {
//        ShipPlacer placer = new ShipPlacer(new ShipValidator());
//        List<Ship> placedShips = placer.placeShipsRandomly();
//        resetPlayerShipsLocations();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0];
//                int col = coord[1];
//                playerShipsLocations[row][col] = ship;
//            }
//        }
//        clearLeftBoardShips();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0], col = coord[1];
//                ShipButton button = playerShipButtons[row][col];
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
//        resetComputerShipsLocations();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0];
//                int col = coord[1];
//                computerShipsLocations[row][col] = ship;
//            }
//        }
//        clearRightBoardShips();
//        for (Ship ship : placedShips) {
//            for (int[] coord : ship.getCoordinates()) {
//                int row = coord[0], col = coord[1];
//                ShipButton button = computerShipButtons[row][col];
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
//                ShipButton button = playerShipButtons[i][j];
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
//                ShipButton button = computerShipButtons[i][j];
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
//        isGameStarted = false;
//        isRandomButtonPressed = false;
//        resetComputerShipsLocations();
//        resetPlayerShipsLocations();
//        aiLogic.resetAI();
//        isPlayerTurn = true;
//        isRandomButtonPressed = false;
//        for (int i = 0; i <= 10; i++) {
//            for (int j = 0; j <= 10; j++) {
//                playerTargetedArea[i][j] = 0;
//                computerTargetedArea[i][j] = 0;
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