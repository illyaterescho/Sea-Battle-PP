package org.seabattlepp.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

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