package org.seabattlepp.logic;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.ships.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class UIMarkingLogic {

    private final GameLogic gameLogic;

    public UIMarkingLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void markHitSymbol(ShipButton button) {
        button.setEnabled(false);
        button.setOpaque(false);
        markHitUI(button);
    }

    public void markHitSymbolPlayerBoard(ShipButton button) {
        if (button.getText() == null || !button.getText().equals("⚓")) {
            button.setEnabled(false);
        }
        button.setOpaque(false);
        markHitUI(button);
    }

    private void markHitUI(ShipButton button) {
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                super.paint(g, c);

                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Inter", Font.BOLD, 70));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "✖";
                int textWidth = fm.stringWidth(text);
                int x = (c.getWidth() - textWidth) / 2;

                int verticalOffset = 41;
                int y = c.getHeight() / 3 + verticalOffset;

                g2d.drawString(text, x, y);
            }
        });
    }

    public void markSunkShip(Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            if (gameLogic.boardManager.computerShipButtons[row][col] != null) {
                markSunkCellUI(gameLogic.boardManager.computerShipButtons[row][col]);
                gameLogic.boardManager.computerShipButtons[row][col].setEnabled(false);
                gameLogic.boardManager.computerShipButtons[row][col].setOpaque(false);
                gameLogic.boardManager.computerShipButtons[row][col].setText(null);
            }
        }
        markSurroundingCellsAsMiss(ship);
    }

    public void markSunkShipPlayerBoard(Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            if (gameLogic.boardManager.playerShipButtons[row][col] != null) {
                markSunkCellUI(gameLogic.boardManager.playerShipButtons[row][col]);
                gameLogic.boardManager.playerShipButtons[row][col].setEnabled(false);
                gameLogic.boardManager.playerShipButtons[row][col].setOpaque(false);
                gameLogic.boardManager.playerShipButtons[row][col].setIcon(null);
            }
        }
        markSurroundingCellsAsMissPlayerBoard(ship);
    }

    private void markSunkCellUI(ShipButton button) {
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 50, 50);

                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Inter", Font.BOLD, 50));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "☠";
                int textWidth = fm.stringWidth(text);
                int x = (c.getWidth() - textWidth) / 2;

                int verticalOffset = 20;
                int y = c.getHeight() / 2 + verticalOffset;

                g2d.drawString(text, x, y);
            }
        });
    }

    public void markMiss(int row, int col) {
        ShipButton button = gameLogic.boardManager.computerShipButtons[row][col];
        if (button != null && button.isEnabled()) {
            markMissSymbol(button);
            gameLogic.markPlayerShot(row, col); // Mark as shot in playerTargetedArea
        }
    }

    public void markMissPlayerBoard(int row, int col) {
        ShipButton button = gameLogic.boardManager.playerShipButtons[row][col];
        if (button != null) {
            markMissSymbol(button);
            gameLogic.markComputerShot(row, col); // Mark as shot in computerTargetedArea
        }
    }

    private void markMissSymbol(ShipButton button) {
        button.setEnabled(false);
        button.setOpaque(false);
        markMissUI(button);
    }

    private void markMissUI(ShipButton button) {
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                super.paint(g, c);

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Inter", Font.BOLD, 70));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "•";
                int textWidth = fm.stringWidth(text);
                int x = (c.getWidth() - textWidth) / 2;

                int verticalOffset = 39;
                int y = c.getHeight() / 3 + verticalOffset;

                g2d.drawString(text, x, y);
            }
        });
    }

    public void markSurroundingCellsAsMiss(Ship ship) {
        Set<String> markedCells = new HashSet<>();

        for (int[] coord : ship.getCoordinates()) {
            int shipRow = coord[0];
            int shipCol = coord[1];

            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    if (rowOffset == 0 && colOffset == 0) continue;

                    int adjacentRow = shipRow + rowOffset;
                    int adjacentCol = shipCol + colOffset;

                    String cellKey = adjacentRow + "," + adjacentCol;

                    if (isValidCell(adjacentRow, adjacentCol) && !markedCells.contains(cellKey)) {
                        if (gameLogic.boardManager.computerShipsLocations[adjacentRow][adjacentCol] == null && gameLogic.boardManager.computerShipButtons[adjacentRow][adjacentCol] != null) {
                            markMissSymbol(gameLogic.boardManager.computerShipButtons[adjacentRow][adjacentCol]);
                            gameLogic.markPlayerShot(adjacentRow, adjacentCol); // Mark as shot in playerTargetedArea
                            markedCells.add(cellKey);
                        }
                    }
                }
            }
        }
    }

    public void markSurroundingCellsAsMissPlayerBoard(Ship ship) {
        Set<String> markedCells = new HashSet<>();

        for (int[] coord : ship.getCoordinates()) {
            int shipRow = coord[0];
            int shipCol = coord[1];

            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    if (rowOffset == 0 && colOffset == 0) continue;

                    int adjacentRow = shipRow + rowOffset;
                    int adjacentCol = shipCol + colOffset;

                    String cellKey = adjacentRow + "," + adjacentCol;

                    if (isValidCell(adjacentRow, adjacentCol) && !markedCells.contains(cellKey)) {
                        if (gameLogic.boardManager.playerShipsLocations[adjacentRow][adjacentCol] == null && gameLogic.boardManager.playerShipButtons[adjacentRow][adjacentCol] != null) {
                            markMissSymbol(gameLogic.boardManager.playerShipButtons[adjacentRow][adjacentCol]);
                            gameLogic.markComputerShot(adjacentRow, adjacentCol); // Mark as shot in computerTargetedArea
                            markedCells.add(cellKey);
                        }
                    }
                }
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }
}
