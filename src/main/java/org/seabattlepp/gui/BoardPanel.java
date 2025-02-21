package org.seabattlepp.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.ships.ShipPlacer;
import org.seabattlepp.ships.ShipValidator;

public class BoardPanel extends JPanel {

    private final ShipButton[][] playerShipButtons = new ShipButton[11][11]; // Зберігаємо кнопки лівої дошки
    private MainFrame MainFrame;

    public BoardPanel(MainFrame mainFrame) {
        this.MainFrame = MainFrame;
        setLayout(new GridLayout(1, 2, 20, 20));
        setOpaque(false);

        Font labelFont = new Font("Inter", Font.BOLD, 35);

        // (створення лівої дошки (гравця)
        JPanel leftBoardPanel = new JPanel(new BorderLayout()); // Панель для лівої дошки
        leftBoardPanel.setOpaque(false);
        JLabel leftLabel = new JLabel("Ваша Дошка", SwingConstants.CENTER);
        leftLabel.setFont(labelFont);
        leftBoardPanel.add(leftLabel, BorderLayout.NORTH);
        JPanel grid = leftBoard(new Color(0x699BF7)); // Створюємо та додаємо сітку на ліву дошку
        leftBoardPanel.add(grid, BorderLayout.CENTER);

        // (створення правої дошки (комп'ютера)
        JPanel rightBoardPanel = new JPanel(new BorderLayout());
        rightBoardPanel.setOpaque(false);
        JLabel rightLabel = new JLabel("Комп'ютер", SwingConstants.CENTER);
        rightLabel.setFont(labelFont);
        rightBoardPanel.add(rightLabel, BorderLayout.NORTH);
        rightBoardPanel.add(rightBoard(new Color(0xFF8577)), BorderLayout.CENTER);

        // (додавання дошок на панель)
        add(leftBoardPanel);
        add(rightBoardPanel);
    }


    // метод для створення ігрової дошки1 (права дошка - Комп'ютер)
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
                    grid1.add(cell);
                } else {
                    grid1.add(new JLabel(""));
                }
            }
        }
        panel.add(grid1, BorderLayout.CENTER);
        return panel;
    }

    //для створення дошки2 (ліва дошка - гравець)
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
                    playerShipButtons[i][j] = cell; // зберігаєм кнопку в масиві playerShipButtons
                    grid2.add(cell);
                } else {
                    grid2.add(new JLabel(""));
                }
            }
        }
        return grid2;
    }

    // для рандомного розміщення кораблів на лівій дошці (перенесено з MainFrame та адаптовано)
    public void placeShipsRandomlyOnLeftBoard() {
        ShipValidator validator = new ShipValidator();
        ShipPlacer placer = new ShipPlacer(validator);
        List<Ship> placedShips = placer.placeShipsRandomly();
        ImageIcon pochatokKorIcon = null;
        ImageIcon centerKorIcon = null;
        ImageIcon kinecKorIcon = null;
        ImageIcon centerKorVertIcon = null;
        ImageIcon headIcon = null;
        ImageIcon botKorVertIcon = null;
        try {
            pochatokKorIcon = new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png");
            centerKorIcon = new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png");
            kinecKorIcon = new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png");
            centerKorVertIcon = new ImageIcon("src/main/java/org/seabattlepp/img/centerKorVert.png");
            headIcon = new ImageIcon("src/main/java/org/seabattlepp/img/head.png");
            botKorVertIcon = new ImageIcon("src/main/java/org/seabattlepp/img/botKorVert.png");
        } catch (Exception e) {
            System.err.println("Помилка завантаження зображень кораблів: " + e.getMessage());
        }

        // видалення попереднього розміщення
        clearLeftBoardShips();
        for (Ship ship : placedShips) {
            List<int[]> coordinates = ship.getCoordinates();
            int shipLength = ship.getLength();
            boolean isVertical = false;
            if (shipLength > 1) {
                if (coordinates.get(0)[1] == coordinates.get(1)[1]) {
                    isVertical = true;
                }
            }

            for (int i = 0; i < shipLength; i++) {
                int[] coord = coordinates.get(i);
                int row = coord[0];
                int col = coord[1];
                if (playerShipButtons[row][col] != null) {
                    ImageIcon currentIcon;


                    // розміри кнопки для масштабування зображення
                    int buttonWidth = playerShipButtons[row][col].getWidth();
                    int buttonHeight = playerShipButtons[row][col].getHeight();


                    // перевірка чи розміри підходять
                    if (buttonWidth <= 0 || buttonHeight <= 0) {
                        buttonWidth = 50; // розмір по дефолту
                        buttonHeight = 50;
                    }
                    if (shipLength == 1) {
                        currentIcon = headIcon;
                    } else if (i == 0) {
                        if (isVertical) {
                            currentIcon = headIcon;
                        } else {
                            currentIcon = pochatokKorIcon;
                        }
                    } else if (i == shipLength - 1) {
                        if (isVertical) {
                            currentIcon = botKorVertIcon;
                        } else {
                            currentIcon = kinecKorIcon;
                        }
                    } else {
                        if (isVertical) {
                            currentIcon = centerKorVertIcon;
                        } else {
                            currentIcon = centerKorIcon;
                        }
                    }
                    if (currentIcon != null) {

                        // масштабування іконки до розміру кнопки
                        Image scaledImage = currentIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                        currentIcon = new ImageIcon(scaledImage);
                        playerShipButtons[row][col].setIcon(currentIcon);
                    }
                    playerShipButtons[row][col].setText(null);
                    playerShipButtons[row][col].setEnabled(false);
                    playerShipButtons[row][col].setBackground(Color.GRAY);
                }
            }
        }
    }

    // для очищення відображення кораблів на лівій дошці (перенесено з MainFrame та адаптовано)
    public void clearLeftBoardShips() {
        try {
        } catch (Exception e) {
            System.err.println("Помилка завантаження зображення води: " + e.getMessage());
        }
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerShipButtons[i][j] != null) {
                    playerShipButtons[i][j].setIcon(null);
                    playerShipButtons[i][j].setText("");
                    playerShipButtons[i][j].setBackground(Color.WHITE); // повертаю білий фон
                    playerShipButtons[i][j].setEnabled(true); // клітинки знову ектів)
                }
            }
        }
    }

    // метод для створення панелі з заданим кольором фону (статичний, залишається таким)
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

    // метод для створення сітки з заданим кольором фону (статичний, залишається таким)
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