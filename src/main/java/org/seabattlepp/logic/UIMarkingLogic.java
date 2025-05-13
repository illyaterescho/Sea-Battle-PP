package org.seabattlepp.logic;

import org.seabattlepp.gui.BoardPanel;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.ships.Ship;

import javax.swing.*;
import java.awt.*;

public class UIMarkingLogic {

    private final BoardPanel boardPanel;
    private final ShipButton[][] computerShipButtons;
    private final ShipButton[][] playerShipButtons;
    private final GameLogic gameLogic;

    public UIMarkingLogic(GameLogic gameLogic, BoardPanel boardPanel, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.gameLogic = gameLogic;
        this.boardPanel = boardPanel;
        this.computerShipButtons = computerShipButtons;
        this.playerShipButtons = playerShipButtons;
    }

    public void markHitSymbol(ShipButton button) {
        button.setEnabled(false);
        button.setOpaque(false);
        markHitUI(button);
    }

    public void markHitSymbolPlayerBoard(ShipButton button) {
        button.setEnabled(false);
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
            if (computerShipButtons[row][col] != null) {
                markSunkCellUI(computerShipButtons[row][col]);
                computerShipButtons[row][col].setEnabled(false);
                computerShipButtons[row][col].setOpaque(false);
                computerShipButtons[row][col].setText(null);
            }
        }
        markSurroundingCellsAsMiss(ship);
    }

    public void markSunkShipPlayerBoard(Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            if (playerShipButtons[row][col] != null) {
                markSunkCellUI(playerShipButtons[row][col]);
                playerShipButtons[row][col].setEnabled(false);
                playerShipButtons[row][col].setOpaque(false);
                playerShipButtons[row][col].setIcon(null);
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
        if (computerShipButtons[row][col] != null) {
            markMissSymbol(computerShipButtons[row][col]);
        }
    }

    public void markMissPlayerBoard(int row, int col) {
        if (playerShipButtons[row][col] != null) {
            markMissSymbolPlayerBoard(playerShipButtons[row][col]);
        }
    }

    private void markMissSymbol(ShipButton button) {
        button.setEnabled(false);
        button.setOpaque(false);
        markMissUI(button);
    }

    private void markMissSymbolPlayerBoard(ShipButton button) {
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
        // Use a set to avoid marking the same cell multiple times
        java.util.Set<String> markedCells = new java.util.HashSet<>();

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
                        if (gameLogic.getShipAt(adjacentRow, adjacentCol) == null && computerShipButtons[adjacentRow][adjacentCol] != null) {
                            markMissSymbol(computerShipButtons[adjacentRow][adjacentCol]);
                            markedCells.add(cellKey);
                        }
                    }
                }
            }
        }
    }

    public void markSurroundingCellsAsMissPlayerBoard(Ship ship) {
        // Use a set to avoid marking the same cell multiple times
        java.util.Set<String> markedCells = new java.util.HashSet<>();

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
                        if (gameLogic.getPlayerShipAt(adjacentRow, adjacentCol) == null && playerShipButtons[adjacentRow][adjacentCol] != null) {
                            markMissSymbolPlayerBoard(playerShipButtons[adjacentRow][adjacentCol]);
                            markedCells.add(cellKey);
                        }
                    }
                }
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }
}