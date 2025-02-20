package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.ships.ShipPlacer;
import org.seabattlepp.ships.ShipValidator;

import static org.seabattlepp.gui.MainFrame.ShipPanelExample.createShipPanel;


public class MainFrame extends JFrame {

    private final ShipPanelExample.ShipButton[][] playerShipButtons = new ShipPanelExample.ShipButton[11][11]; // Зберігаємо кнопки лівої дошки
    private RoundedButton randomButton;

    public MainFrame() {

        // (налаштування вікна: заголовок, іконка, закриття)
        setTitle("Морський Бій");
        try {
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/java/org/seabattlepp/img/icon.jpg");
            setIconImage(image);
        } catch (Exception e) {
            System.err.println("Помилка завантаження іконки: " + e.getMessage());
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // (створення панелі вмісту та встановлення відступів)
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // (створення панелі для дошок та встановлення прозорості)
        JPanel boardPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        boardPanel.setOpaque(false);

        // (налаштування шрифту для написів)
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
        JPanel rightBoard = new JPanel(new BorderLayout());
        rightBoard.setOpaque(false);
        JLabel rightLabel = new JLabel("Комп'ютер", SwingConstants.CENTER);
        rightLabel.setFont(labelFont);
        rightBoard.add(rightLabel, BorderLayout.NORTH);
        rightBoard.add(rightBoard(new Color(0xFF8577)), BorderLayout.CENTER);

        // (додавання дошок на панель)
        boardPanel.add(leftBoardPanel);
        boardPanel.add(rightBoard);

        // (створення панелі з кораблями (знизу)
        JPanel shipPanel = createShipPanel();
        shipPanel.setOpaque(false);

        // (створення панелі з кнопками (зверху))
        JPanel buttonPanel = createButtonPanel();

        // Отримання кнопки "Рандом" з shipPanel
        Component[] components = shipPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof RoundedButton && ((RoundedButton) comp).getText().equals("Рандом")) {
                randomButton = (RoundedButton) comp;
                break;
            }
        }

        // Додавання ActionListener до кнопки "Рандом"
        if (randomButton != null) {
            randomButton.addActionListener(_ -> placeShipsRandomlyOnLeftBoard());
        }

        // додавання компонентів на головне вікно
        add(boardPanel, BorderLayout.CENTER);
        add(shipPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);

        // налаштування розміру та відображення вікна
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
                    MainFrame.ShipPanelExample.ShipButton cell = new MainFrame.ShipPanelExample.ShipButton();
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
                    ShipPanelExample.ShipButton cell = new ShipPanelExample.ShipButton();
                    playerShipButtons[i][j] = cell; // зберігаєм кнопку в масиві playerShipButtons
                    grid2.add(cell);
                } else {
                    grid2.add(new JLabel(""));
                }
            }
        }
        return grid2;
    }


    // для рандомного розміщення кораблів на лівій дошці
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
                    ImageIcon currentIcon = null;
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
                        Image scaledImage = currentIcon.getImage().getScaledInstance(buttonWidth, buttonHeight,  Image.SCALE_SMOOTH);
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

    // для очищення відображення кораблів на лівій дошці
    private void clearLeftBoardShips() {
        try  {
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


    // метод для створення панелі з заданим кольором фону
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

    // метод для створення сітки з заданим кольором фону
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

    // внутрішній клас для ShipPanel (панель з кораблями)
    public static class ShipPanelExample {
        // статичний метод для створення панелі з кораблями
        public static JPanel createShipPanel() {
            JPanel shipPanel = new JPanel(new GridBagLayout());
            TitledBorder border = BorderFactory.createTitledBorder("Корабельна шпаргалка");
            border.setTitleFont(new Font("Inter", Font.BOLD, 25));
            border.setTitleColor(Color.BLACK);
            border.setBorder(new RoundBorder(Color.BLACK, 3, 40));
            shipPanel.setBorder(border);
            shipPanel.setOpaque(false);

            String[] ships = {"Авіаносець", "Броненосець", "Крейсер", "Руйнівник"};
            int[] shipImageCounts = {5, 4, 3, 2, 2};

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);
            JButton randomButton = new RoundedButton("Рандом", new Color(0xD0D0D0), new Color(0x787878));
            randomButton.setPreferredSize(new Dimension(150, 55));
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.insets = new Insets(37, 20, 20, 20);
            gbc2.gridx = 35;
            gbc2.gridy = 0;
            shipPanel.add(randomButton, gbc2);

            for (int i = 0; i < ships.length; i++) {
                JPanel shipContainer = new JPanel();
                shipContainer.setLayout(new BoxLayout(shipContainer, BoxLayout.Y_AXIS));
                shipContainer.setOpaque(false);

                JLabel shipLabel = new JLabel(ships[i]);
                shipLabel.setFont(new Font("Inter", Font.BOLD, 23));
                shipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                shipContainer.add(shipLabel);

                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
                imagePanel.setOpaque(false);

                for (int j = 0; j < shipImageCounts[i]; j++) {
                    JLabel imageLabel = getJLabel(i, j);
                    imagePanel.add(imageLabel);
                }

                shipContainer.add(imagePanel);

                gbc.gridx = i + 1;
                gbc.gridy = 0;
                shipPanel.add(shipContainer, gbc);
            }

            return shipPanel;
        }

        public static JLabel getJLabel(int i, int j) {
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(50, 50));

            if (i == 0 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png"));
            } else if (i == 0 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png"));
            } else if (i == 0 && j == 2) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png"));
            } else if (i == 0 && j == 3) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png"));
            } else if (i == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png"));
            } else if (i == 1 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png"));
            } else if (i == 1 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png"));
            } else if (i == 1 && j == 2) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png"));
            } else if (i == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png"));
            } else if (i == 2 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png"));
            } else if (i == 2 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png"));
            } else if (i == 2 && j == 2) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png"));
            } else if (i == 3 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png"));
            } else if (i == 3 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png"));
            }
            return imageLabel;
        }

        // внутрішній клас для ShipButton (кнопка-клітинка корабля)
        public static class ShipButton extends JButton {
            public ShipButton() {
                setPreferredSize(new Dimension(50, 50));
                setContentAreaFilled(false);
                setOpaque(false);
                setBackground(Color.WHITE);
                setBorder(new RoundBorder(Color.BLACK, 4, 45));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 45, 45);

                super.paintComponent(g);

                g2d.dispose();
            }
        }
    }

    // внутрішній клас для RoundedButton
    public static class RoundedButton extends JButton {
        public final Color backgroundColor;
        public final Color hoverColor;

        public RoundedButton(String text, Color backgroundColor, Color hoverColor) {
            super(text);
            this.backgroundColor = backgroundColor;
            this.hoverColor = hoverColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Inter", Font.BOLD, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getModel().isRollover() ? hoverColor : backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // метод для створення панелі з кнопками
    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 100, 0));
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 0));

        startPanel.setOpaque(false);
        resetPanel.setOpaque(false);
        exitPanel.setOpaque(false);

        JButton startButton = new RoundedButton("Почати Гру", new Color(0x33CC66), new Color(0x388E3C));
        JButton resetButton = new RoundedButton("Скинути", new Color(0x699BF7), new Color(0x3366CC));
        JButton exitButton = new RoundedButton("Вийти", new Color(0xFF8577), new Color(0xD32F2F));

        startButton.setPreferredSize(new Dimension(750, 55));
        resetButton.setPreferredSize(new Dimension(200, 55));
        exitButton.setPreferredSize(new Dimension(200, 55));

        startPanel.add(startButton);
        resetPanel.add(resetButton);
        exitPanel.add(exitButton);

        buttonPanel.add(startPanel, BorderLayout.CENTER);
        buttonPanel.add(resetPanel, BorderLayout.WEST);
        buttonPanel.add(exitPanel, BorderLayout.EAST);

        return buttonPanel;
    }

    // внутрішній клас для RoundBorder (закруглена рамка)
    public static class RoundBorder extends AbstractBorder {

        public final Color color;
        public final int thickness;
        public final int arcRadius;

        public RoundBorder(Color color, int thickness, int arcRadius) {
            this.color = color;
            this.thickness = thickness;
            this.arcRadius = arcRadius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int adjustedX = x + thickness / 2;
            int adjustedY = y + thickness / 2;
            int adjustedWidth = width - thickness;
            int adjustedHeight = height - thickness;

            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight, arcRadius, arcRadius);

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

    }
}