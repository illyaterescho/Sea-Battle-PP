package org.seabattlepp.logic;

import org.seabattlepp.gui.MainFrame;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.ships.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class UIMarkingLogic {

    public MainFrame mainFrame;

    public UIMarkingLogic(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void markComputerBoardHit(ShipButton button) {
        button.setEnabled(false);
        button.setOpaque(false);
        markHitSymbol(button);
    }

    public void markPlayerBoardHit(ShipButton button) {
        if (button.getText() == null || !button.getText().equals("⚓")) {
            button.setEnabled(false);
        }
        button.setOpaque(false);
        markHitSymbol(button);
    }

    private void markHitSymbol(ShipButton button) {
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

    public void markComputerBoardSunkShip(Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            if (mainFrame.boardManager.computerShipButtons[row][col] != null) {
                markSunkSymbol(mainFrame.boardManager.computerShipButtons[row][col]);
                mainFrame.boardManager.computerShipButtons[row][col].setEnabled(false);
                mainFrame.boardManager.computerShipButtons[row][col].setOpaque(false);
                mainFrame.boardManager.computerShipButtons[row][col].setText(null);
            }
        }
        markSurroundingCellsAsMissComputerBoard(ship);
    }

    public void markPlayerBoardSunkShip(Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            if (mainFrame.boardManager.playerShipButtons[row][col] != null) {
                markSunkSymbol(mainFrame.boardManager.playerShipButtons[row][col]);
                mainFrame.boardManager.playerShipButtons[row][col].setEnabled(false);
                mainFrame.boardManager.playerShipButtons[row][col].setOpaque(false);
                mainFrame.boardManager.playerShipButtons[row][col].setIcon(null);
            }
        }
        markSurroundingCellsAsMissPlayerBoard(ship);
    }

    private void markSunkSymbol(ShipButton button) {
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

    private void markMissSymbol(ShipButton button) {
        button.setEnabled(false);
        button.setOpaque(false);
        markMissUI(button);
    }

    public boolean markHit(int row, int col, Ship ship) {
        boolean sunk = false;
        if (mainFrame.boardManager.computerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();
            if (sunk) {
                markComputerBoardSunkShip(ship);
            } else {
                markComputerBoardHit(mainFrame.boardManager.computerShipButtons[row][col]);
            }
        }
        return sunk;
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

    public void markSurroundingCellsAsMissComputerBoard(Ship ship) {
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
                        if (mainFrame.boardManager.computerShipsLocations[adjacentRow][adjacentCol] == null && mainFrame.boardManager.computerShipButtons[adjacentRow][adjacentCol] != null) {
                            markMissSymbol(mainFrame.boardManager.computerShipButtons[adjacentRow][adjacentCol]);
                            mainFrame.boardManager.updatePlayerShotRecord(adjacentRow, adjacentCol);
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
                        if (mainFrame.boardManager.playerShipsLocations[adjacentRow][adjacentCol] == null && mainFrame.boardManager.playerShipButtons[adjacentRow][adjacentCol] != null) {
                            markMissSymbol(mainFrame.boardManager.playerShipButtons[adjacentRow][adjacentCol]);
                            mainFrame.boardManager.updateComputerShotRecord(adjacentRow, adjacentCol);
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

    public boolean markPlayerBoardHit(int row, int col, Ship ship) {
        if (mainFrame.gameLogic.checkGameEnd() || !mainFrame.boardManager.isCellAvailableForShot(row, col) || mainFrame.boardManager.isComputerShotAt(row, col)) {
            return false;
        }

        mainFrame.boardManager.updateComputerShotRecord(row, col);

        boolean sunk = false;
        if (mainFrame.boardManager.playerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();
            if (sunk) {
                markPlayerBoardSunkShip(ship);
                for (int[] coord : ship.getCoordinates()) {
                    int r = coord[0];
                    int c = coord[1];
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int newRow = r + dr;
                            int newCol = c + dc;
                            if (newRow >= 1 && newRow <= 10 && newCol >= 1 && newCol <= 10 && !mainFrame.boardManager.isComputerShotAt(newRow, newCol)) {
                                mainFrame.boardManager.updateComputerShotRecord(newRow, newCol);
                            }
                        }
                    }
                }
                if (mainFrame.gameLogic.checkGameEnd()) {
                    mainFrame.aiLogic.stopTimer();
                }
            } else {
                markPlayerBoardHit(mainFrame.boardManager.playerShipButtons[row][col]);
            }
        }
        return sunk;
    }

    public void markPlayerBoardMiss(int row, int col) {
        if (mainFrame.gameLogic.checkGameEnd() || !mainFrame.boardManager.isCellAvailableForShot(row, col) || mainFrame.boardManager.isComputerShotAt(row, col)) {
            return;
        }

        mainFrame.boardManager.updateComputerShotRecord(row, col);

        if (mainFrame.boardManager.playerShipButtons[row][col] != null) {
            ShipButton button = mainFrame.boardManager.playerShipButtons[row][col];
            if (button != null) {
                markMissSymbol(button);
                mainFrame.boardManager.updateComputerShotRecord(row, col);
            }
        }
    }
    public void markComputerBoardMiss(int row, int col) {
        ShipButton button = mainFrame.boardManager.computerShipButtons[row][col];
        if (button != null && button.isEnabled()) {
            markMissSymbol(button);
            mainFrame.boardManager.updatePlayerShotRecord(row, col);
        }
    }
}
