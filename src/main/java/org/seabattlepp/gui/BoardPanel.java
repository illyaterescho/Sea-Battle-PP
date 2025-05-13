package org.seabattlepp.gui;

//import org.seabattlepp.logic.GameLogic;
//import org.seabattlepp.ships.Ship;
//import org.seabattlepp.ships.ShipPlacer;
//import org.seabattlepp.ships.ShipValidator;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
//import java.util.List;

public class BoardPanel extends JPanel {

    public final ShipButton[][] playerShipButtons = new ShipButton[11][11];
    public final ShipButton[][] computerShipButtons = new ShipButton[11][11];
    public MainFrame MainFrame;
//    public GameLogic gameLogic;
//    private boolean isGameStarted = false;

    public BoardPanel(MainFrame mainFrame) {
        this.MainFrame = mainFrame;
        setLayout(new GridLayout(1, 2, 20, 20));
        setOpaque(false);

        Font labelFont = new Font("Inter", Font.BOLD, 35);

        JPanel leftBoardPanel = new JPanel(new BorderLayout());
        leftBoardPanel.setOpaque(false);
        JLabel leftLabel = new JLabel("Ваша Дошка", SwingConstants.CENTER);
        leftLabel.setFont(labelFont);
        leftBoardPanel.add(leftLabel, BorderLayout.NORTH);
        JPanel grid = leftBoard(new Color(0x699BF7));
        leftBoardPanel.add(grid, BorderLayout.CENTER);

        JPanel rightBoardPanel = new JPanel(new BorderLayout());
        rightBoardPanel.setOpaque(false);
        JLabel rightLabel = new JLabel("Комп'ютер", SwingConstants.CENTER);
        rightLabel.setFont(labelFont);
        rightBoardPanel.add(rightLabel, BorderLayout.NORTH);
        rightBoardPanel.add(rightBoard(new Color(0xFF8577)), BorderLayout.CENTER);

        add(leftBoardPanel);
        add(rightBoardPanel);

//        gameLogic = new GameLogic(this, computerShipButtons, playerShipButtons);
    }

    public JPanel rightBoard(Color backgroundColor) {
        JPanel panel = getPanel(backgroundColor);
        JPanel grid1 = getJPanel(backgroundColor);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j > 0) {
                    JLabel numLabel = new JLabel(String.valueOf(j), SwingConstants.CENTER);
                    numLabel.setFont(new Font("Inter", Font.BOLD, 25));
                    numLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    grid1.add(numLabel);
                } else if (j == 0 && i > 0) {
                    JLabel letterLabel = new JLabel(String.valueOf((char) ('A' + i - 1)), SwingConstants.CENTER);
                    letterLabel.setFont(new Font("Inter", Font.BOLD, 25));
                    letterLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    grid1.add(letterLabel);
                } else if (i > 0) {
                    ShipButton cell = new ShipButton();
                    computerShipButtons[i][j] = cell;
//                    final int row = i;
//                    final int col = j;
//                    cell.setEnabled(true);
//                    cell.addActionListener(e -> {
//                        if (isGameStarted && gameLogic.isPlayerTurn()) { // перевірка черги гравця
//                            gameLogic.processShot(row, col);
//                        }
//                    });
                    grid1.add(cell);
                } else {
                    grid1.add(new JLabel(""));
                }
            }
        }
        panel.add(grid1, BorderLayout.CENTER);
        return panel;
    }

    public JPanel leftBoard(Color backgroundColor) {
        JPanel grid2 = getJPanel(backgroundColor);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j > 0) {
                    JLabel numLabel = new JLabel(String.valueOf(j), SwingConstants.CENTER);
                    numLabel.setFont(new Font("Inter", Font.BOLD, 25));
                    numLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    grid2.add(numLabel);
                } else if (j == 0 && i > 0) {
                    JLabel letterLabel = new JLabel(String.valueOf((char) ('A' + i - 1)), SwingConstants.CENTER);
                    letterLabel.setFont(new Font("Inter", Font.BOLD, 25));
                    letterLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    grid2.add(letterLabel);
                } else if (i > 0) {
                    ShipButton cell = new ShipButton();
                    playerShipButtons[i][j] = cell;
//                    cell.setEnabled(true);
                    grid2.add(cell);
                } else {
                    grid2.add(new JLabel(""));
                }
            }
        }
        return grid2;
    }

    public static JPanel getPanel(Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 220, 200);
            }
        };
        panel.setOpaque(true);
        return panel;
    }

    public static JPanel getJPanel(Color backgroundColor) {
        JPanel grid = new JPanel(new GridLayout(11, 11, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 200, 200);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 200, 200);
            }
        };
        grid.setBorder(new CompoundBorder(new RoundBorder(Color.BLACK, 3, 200), new EmptyBorder(5, 5, 80, 50)));
        grid.setOpaque(false);
        return grid;
    }
}
//    public void placeShipsRandomlyOnLeftBoard() {
//        ShipValidator validator = new ShipValidator();
//        ShipPlacer placer = new ShipPlacer(validator);
//        List<Ship> placedShips = placer.placeShipsRandomly();
//        gameLogic.setPlayerShips(placedShips);
//
//        clearLeftBoardShips();
//        for (Ship ship : placedShips) {
//            List<int[]> coordinates = ship.getCoordinates();
//            int shipLength = ship.getLength();
//
//            for (int i = 0; i < shipLength; i++) {
//                int[] coord = coordinates.get(i);
//                int row = coord[0];
//                int col = coord[1];
//                if (playerShipButtons[row][col] != null) {
//                    playerShipButtons[row][col].setIcon(null);
//                    playerShipButtons[row][col].setText("⚓");
//                    playerShipButtons[row][col].setFont(new Font("Inter", Font.BOLD, 50));
//                    playerShipButtons[row][col].setForeground(Color.BLACK);
//                    playerShipButtons[row][col].setEnabled(true);
//                    playerShipButtons[row][col].setBackground(Color.WHITE);
//                }
//            }
//        }
//    }
//
//    public void placeShipsRandomlyOnRightBoard() {
//        ShipValidator validator = new ShipValidator();
//        ShipPlacer placer = new ShipPlacer(validator);
//        List<Ship> placedShips = placer.placeShipsRandomly();
//
//        gameLogic.setComputerShips(placedShips);
//
//        clearRightBoardShips();
//
//        for (Ship ship : placedShips) {
//            List<int[]> coordinates = ship.getCoordinates();
//            int shipLength = ship.getLength();
//
//            for (int i = 0; i < shipLength; i++) {
//                int[] coord = coordinates.get(i);
//                int row = coord[0];
//                int col = coord[1];
//                if (computerShipButtons[row][col] != null) {
//                    computerShipButtons[row][col].setBackground(Color.WHITE);
//                    computerShipButtons[row][col].setForeground(Color.BLACK);
//                    computerShipButtons[row][col].setFont(new Font("Inter", Font.BOLD, 25));
//                    computerShipButtons[row][col].setIcon(null);
//                    computerShipButtons[row][col].setOpaque(false);
//                    computerShipButtons[row][col].setEnabled(true); // спочатку ввимкнено для пк
//                }
//            }
//        }
//    }


//    public void clearLeftBoardShips() {
//        try {
//        } catch (Exception e) {
//            System.err.println("Помилка завантаження зображення: " + e.getMessage());
//        }
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                if (playerShipButtons[i][j] != null) {
//                    playerShipButtons[i][j].setIcon(null); // очищення іконки
//                    playerShipButtons[i][j].setText("");    // Очищення тексту (якщо є)
//                    playerShipButtons[i][j].setBackground(Color.WHITE); // повернення фону до білого
//                    playerShipButtons[i][j].setEnabled(true);  // кнопки знову активні
//                    playerShipButtons[i][j].setOpaque(false);
//                    playerShipButtons[i][j].setUI(new javax.swing.plaf.basic.BasicButtonUI()); // повернення до стандартного UI
//                }
//            }
//        }
//    }

//    public void clearRightBoardShips() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                if (computerShipButtons[i][j] != null) {
//                    computerShipButtons[i][j].setIcon(null); // очищення іконки
//                    computerShipButtons[i][j].setText("");   // очищення тексту
//                    computerShipButtons[i][j].setBackground(Color.WHITE); // Повернення фону до білого
//                    computerShipButtons[i][j].setEnabled(true);   // кнопки знову активні
//                    computerShipButtons[i][j].setOpaque(false);
//                    computerShipButtons[i][j].setUI(new javax.swing.plaf.basic.BasicButtonUI()); // повернення до стандартного UI
//                }
//            }
//        }
//    }

//    public void resetBoards() {
//        clearLeftBoardShips();
//        clearRightBoardShips();
//        isGameStarted = false;
//        gameLogic.resetGame();
//        enablePlayerButtonsForPlacement(); // окремий метод для ввімкнення кнопок гравця
//        disableComputerButtons();
//    }

//    public void setGameStarted(boolean started) {
//        isGameStarted = started;
//        if (started && !gameLogic.isPlayerTurn()) {
//            gameLogic.startComputerTurn();
//        }
//        if (started) {
//            // після початку гри кнопки початок гри та рандом вимикаємо
//        }
//    }

//    public boolean isGameStarted() {
//        return isGameStarted;
//    }

// для того щоб кнопки не багались і були активні до початку гри
//    public void enablePlayerButtonsForPlacement() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                if (playerShipButtons[i][j] != null) {
//                    playerShipButtons[i][j].setEnabled(true); // умикаємо всі кнопки гравця для розміщення
//                }
//            }
//        }
//    }


// вмикаємо кнопки пк коли він стріляє
//    public void enableComputerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                if (computerShipButtons[i][j] != null && computerShipButtons[i][j].isEnabled() && computerShipButtons[i][j].getIcon() == null && computerShipButtons[i][j].getBackground() != Color.RED) { // Вмикаємо тільки вільні клітинки, не підбиті і не потоплені
//                    computerShipButtons[i][j].setEnabled(true);
//                }
//                else if (computerShipButtons[i][j] != null && !computerShipButtons[i][j].isEnabled()) {
//                }
//            }
//        }
//    }
//
//    // Метод для вимкнення кнопок комп'ютера
//    public void disableComputerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                if (computerShipButtons[i][j] != null) {
//                    computerShipButtons[i][j].setEnabled(true);
//                }
//            }
//        }
//    }


//    // вмикаємо кнопки гравця коли його черга стреляти
//    public void enablePlayerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                if (playerShipButtons[i][j] != null && playerShipButtons[i][j].isEnabled() && playerShipButtons[i][j].getIcon() == null && playerShipButtons[i][j].getBackground() != Color.RED) { // Вмикаємо тільки вільні клітинки, не підбиті і не потоплені
//                    playerShipButtons[i][j].setEnabled(true);
//                }
//                else if (playerShipButtons[i][j] != null && !playerShipButtons[i][j].isEnabled()) {
//                }
//            }
//        }
//    }
//
//    // вимкнення кнопок гравця коли не його черга стреляти
//    public void disablePlayerButtons() {
//        for (int i = 1; i <= 10; i++) {
//            for (int j = 1; j <= 10; j++) {
//                if (playerShipButtons[i][j] != null) {
//                    playerShipButtons[i][j].setEnabled(true);
//                }
//            }
//        }
//    }
