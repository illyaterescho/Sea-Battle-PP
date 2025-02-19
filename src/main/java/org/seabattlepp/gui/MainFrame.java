package org.seabattlepp.gui;

Illia-Tereshko
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

// імпорт для використання статичного методу createShipPanel
import static org.seabattlepp.gui.MainFrame.ShipPanelExample.createShipPanel;

public class MainFrame extends JFrame {
    // для запуску програми
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new); // запуск програми Swing
    }

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
        JPanel leftBoard = new JPanel(new BorderLayout());
        leftBoard.setOpaque(false);
        JLabel leftLabel = new JLabel("Ваша Дошка", SwingConstants.CENTER);
        leftLabel.setFont(labelFont);
        leftBoard.add(leftLabel, BorderLayout.NORTH);
        JPanel grid = leftBoard(new Color(0x699BF7)); // Створюємо та додаємо сітку на ліву дошку
        leftBoard.add(grid, BorderLayout.CENTER);

        // (створення правої дошки (комп'ютера)
        JPanel rightBoard = new JPanel(new BorderLayout());
        rightBoard.setOpaque(false);
        JLabel rightLabel = new JLabel("Комп'ютер", SwingConstants.CENTER);
        rightLabel.setFont(labelFont);
        rightBoard.add(rightLabel, BorderLayout.NORTH);
        rightBoard.add(rightBoard(new Color(0xFF8577)), BorderLayout.CENTER);

        // (додавання дошок на панель)
        boardPanel.add(leftBoard);
        boardPanel.add(rightBoard);

        // (створення панелі з кораблями (знизу)
        JPanel shipPanel = createShipPanel();
        shipPanel.setOpaque(false);

        // (створення панелі з кнопками (зверху))
        JPanel buttonPanel = createButtonPanel();

        // додавання компонентів на головне вікно
        add(boardPanel, BorderLayout.CENTER);
        add(shipPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);

        // налаштування розміру та відображення вікна
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // метод для створення ігрової дошки1
    public JPanel rightBoard(Color backgroundColor) {
        JPanel panel = getPanel(backgroundColor); // створення панелі для дошки

        JPanel grid1 = getJPanel(backgroundColor); // створення клітинок для дошки
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j > 0) {
                    // додавання номерів стовпців
                    JLabel numLabel = new JLabel(String.valueOf(j), SwingConstants.CENTER); // створення напису з номером стовпця
                    numLabel.setFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту
                    numLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // видалення стандартної рамки
                    grid1.add(numLabel); // додавання напису
                } else if (j == 0 && i > 0) {
                    // Додавання літер рядків
                    JLabel letterLabel = new JLabel(String.valueOf((char) ('A' + i - 1)), SwingConstants.CENTER); // створення напису з літерою рядка
                    letterLabel.setFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту
                    letterLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // видалення стандартної рамки
                    grid1.add(letterLabel); // додавання напису
                } else if (i > 0) {
                    // додавання клітинок-кнопок для кораблів
                    MainFrame.ShipPanelExample.ShipButton cell = new MainFrame.ShipPanelExample.ShipButton(); // створення кнопки
                    grid1.add(cell); // додавання клітинки
                } else {
                    grid1.add(new JLabel("")); // пусті клітинки
                }
            }
        }
        panel.add(grid1, BorderLayout.CENTER); // додавання клітинок на панель
        return panel; // повернення панелі з дошкою
    }

    // метод для створення дошки2
    public JPanel leftBoard(Color backgroundColor) {
        JPanel grid2 = getJPanel(backgroundColor); // створення клітинок для дошки
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j > 0) {
                    // додавання номерів стовпців
                    JLabel numLabel = new JLabel(String.valueOf(j), SwingConstants.CENTER); // створення напису з номером стовпця
                    numLabel.setFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту
                    numLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // видалення стандартної рамки
                    grid2.add(numLabel); // додавання напису
                } else if (j == 0 && i > 0) {
                    // Додавання літер рядків
                    JLabel letterLabel = new JLabel(String.valueOf((char) ('A' + i - 1)), SwingConstants.CENTER); // створення напису з літерою рядка
                    letterLabel.setFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту
                    letterLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // видалення стандартної рамки
                    grid2.add(letterLabel); // додавання напису
                } else if (i > 0) {
                    // додавання клітинок-кнопок для кораблів та збереження їх в масив
                    ShipPanelExample.ShipButton cell = new ShipPanelExample.ShipButton(); // створення кнопки
                    grid2.add(cell); // додавання клітинки
                } else {
                    grid2.add(new JLabel("")); // пусті клітинки
                }
            }
        }
        return grid2;
    }

    // метод для створення панелі з заданим кольором фону
    public static JPanel getPanel(Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // виклик батьківського методу для малювання фону
                Graphics2D g2d = (Graphics2D) g; // створення об'єкта Graphics2D для малювання
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // включення згладжування
                g2d.setColor(backgroundColor); // встановлення кольору фону
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 220, 200); // малювання закругленого прямокутника
            }
        };
        panel.setOpaque(true); // встановлення непрозорості панелі
        return panel; // повернення панелі
    }

    // метод для створення сітки з заданим кольором фону
    public static JPanel getJPanel(Color backgroundColor) {
        JPanel grid = new JPanel(new GridLayout(11, 11, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // виклик батьківського методу для малювання фону
                Graphics2D g2d = (Graphics2D) g; // створення об'єкта Graphics2D для малювання
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // включення згладжування
                g2d.setColor(backgroundColor); // встановлення кольору фону
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 200, 200); // малювання закругленого прямокутника
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 200, 200);
            }
        };
        grid.setBorder(new CompoundBorder(new RoundBorder(Color.BLACK, 3, 200), new EmptyBorder(5, 5, 80, 50))); // встановлення рамки для сітки
        grid.setOpaque(false); // встановлення прозорості сітки
        return grid; // повернення сітки
    }

    // внутрішній клас для ShipPanel (панель з кораблями)
    public static class ShipPanelExample {
        // статичний метод для створення панелі з кораблями
        public static JPanel createShipPanel() {
            JPanel shipPanel = new JPanel(new GridBagLayout()); // використання GridBagLayout для гнучкого розміщення
            TitledBorder border = BorderFactory.createTitledBorder("Корабельна шпаргалка"); // створення заголовка для панелі
            border.setTitleFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту для заголовка
            border.setTitleColor(Color.BLACK); // Встановлення кольору для заголовка
            border.setBorder(new RoundBorder(Color.BLACK, 3, 40)); // встановлення закругленої рамки для заголовка
            shipPanel.setBorder(border); // встановлення заголовка для панелі
            shipPanel.setOpaque(false); // встановлення прозорості для панелі

            String[] ships = {"Авіаносець", "Броненосець", "Крейсер", "Руйнівник", "Фрегат"}; // масив з назвами кораблів
            int[] shipImageCounts = {5, 4, 3, 2, 2}; // масив з кількістю зображень для кожного корабля

            GridBagConstraints gbc = new GridBagConstraints(); // створення об'єкта GridBagConstraints для налаштування розміщення компонентів
            gbc.insets = new Insets(20, 20, 20, 20); // встановлення відступів між компонентами
            JButton randomButton = new RoundedButton("Рандом", new Color(0xD0D0D0), new Color(0x787878));
            randomButton.setPreferredSize(new Dimension(150, 55));
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.insets = new Insets(37, 20, 20, 20);
            gbc2.gridx = 35;
            gbc2.gridy = 0;
            shipPanel.add(randomButton, gbc2); // заміна gbc на gbc2

            for (int i = 0; i < ships.length; i++) {
                JPanel shipContainer = new JPanel(); // створення панелі для кожного корабля
                shipContainer.setLayout(new BoxLayout(shipContainer, BoxLayout.Y_AXIS)); // встановлення вертикального розміщення для компонентів
                shipContainer.setOpaque(false); // Встановлення прозорості для панелі

                JLabel shipLabel = new JLabel(ships[i]); // створення напису з назвою корабля
                shipLabel.setFont(new Font("Inter", Font.BOLD, 23)); // встановлення шрифту для напису
                shipLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // вирівнювання напису по центру
                shipContainer.add(shipLabel); // додавання напису на панель корабля

                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2)); // створення панелі для зображень корабля
                imagePanel.setOpaque(false); // встановлення прозорості для панелі

                for (int j = 0; j < shipImageCounts[i]; j++) {
                    JLabel imageLabel = getJLabel(i, j);
                    imagePanel.add(imageLabel); // додавання зображення на панель
                }

                shipContainer.add(imagePanel); // додавання панелі зображень на панель корабля

                gbc.gridx = i + 1; // встановлення позиції корабля в сітці
                gbc.gridy = 0;
                shipPanel.add(shipContainer, gbc); // додавання панелі корабля на головну панель
            }

            return shipPanel; // повернення готової панелі з кораблями
        }
        public static JLabel getJLabel(int i, int j) {
            JLabel imageLabel = new JLabel(); // створення JLabel для зображення
            imageLabel.setPreferredSize(new Dimension(50, 50)); // встановлення розміру для зображення

            // встановлення зображень для кожного корабля та його частини
            if (i == 0 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png")); // Авіаносець - початок
            } else if (i == 0 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png")); // Авіаносець - середина
            } else if (i == 0 && j == 2) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png")); // Авіаносець - середина
            } else if (i == 0 && j == 3) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png")); // Авіаносець - середина
            } else if (i == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png")); // Авіаносець - кінець
            } else if (i == 1 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png")); // Броненосець - початок
            } else if (i == 1 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png")); // Броненосець - середина
            } else if (i == 1 && j == 2) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png")); // Броненосець - середина
            } else if (i == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png")); // Броненосець - кінець
            } else if (i == 2 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png")); // Крейсер - початок
            } else if (i == 2 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/centerKor.png")); // Крейсер - середина
            } else if (i == 2 && j == 2) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png")); // Крейсер - кінець
            } else if (i == 3 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png")); // Руйнівник - початок
            } else if (i == 3 && j == 1) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png")); // Руйнівник - кінець
            } else if (i == 4 && j == 0) {
                imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/head.png")); // Фрегат - початок
            }
            return imageLabel;
        }
        // внутрішній клас для ShipButton (кнопка-клітинка корабля)
        public static class ShipButton extends JButton {
            public ShipButton() {
                setPreferredSize(new Dimension(50, 50)); // розмір клітинки
                setContentAreaFilled(false); // не заповнювати область контенту кнопки
                setOpaque(false); // прозора кнопка
                setBackground(Color.WHITE); // білий фон
                setBorder(new RoundBorder(Color.BLACK, 4, 45)); // закруглена рамка

                // Додаємо слухача миші для зміни кольору фону при наведенні
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent evt) {
                        setBackground(Color.GRAY);
                    }

                    @Override
                    public void mouseExited(MouseEvent evt) {
                        setBackground(Color.WHITE);
                    }
                });
            }


            // перемалювання компонента
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create(); // створення копії графіки
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);g2d.setColor(getBackground()); // встановлення кольору фону
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 45, 45); // заповнення закругленого прямокутника

                super.paintComponent(g); // виклик батьківського методу для відображення інших компонентів

                g2d.dispose(); // звільнення ресурсів графіки
            }
        }
    }
    // внутрішній клас для RoundedButton
    public static class RoundedButton extends JButton {
        public final Color backgroundColor; // колір фону кнопки
        public final Color hoverColor; // колір фону при наведенні курсора

        // Конструктор класу
        public RoundedButton(String text, Color backgroundColor, Color hoverColor) {
            super(text); // виклик конструктора батьківського класу
            this.backgroundColor = backgroundColor; // встановлення кольору фону
            this.hoverColor = hoverColor; // встановлення кольору при наведенні курсора
            setContentAreaFilled(false); // вимкнення заповнення області контенту кнопки
            setFocusPainted(false); // вимкнення малювання фокуса
            setBorderPainted(false); // вимкнення малювання рамки
            setFont(new Font("Inter", Font.BOLD, 20)); // встановлення шрифту для напису
        }

        // перемалювання компонента
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); // створюємо копію графіки
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // включаємо згладжування

            g2.setColor(getModel().isRollover() ? hoverColor : backgroundColor); // встановлюємо колір фону залежно від стану кнопки
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // малюємо закруглений прямокутник

            g2.setColor(Color.BLACK); // встановлюємо колір рамки
            g2.setStroke(new BasicStroke(2)); // встановлюємо товщину рамки
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30); // малюємо рамку

            g2.dispose(); // звільняємо ресурси графіки

            super.paintComponent(g); // викликаємо батьківський метод для відображення інших компонентів
        }
    }

    // метод для створення панелі з кнопками
    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout()); // створення панелі з макетом BorderLayout
        buttonPanel.setOpaque(false); // встановлення прозорості

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // створення панелі для кнопки "Почати гру" з макетом FlowLayout
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 100, 0)); // створення панелі для кнопки "Скинути" з макетом FlowLayout
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 0)); // створення панелі для кнопки "Вийти" з макетом FlowLayout

        startPanel.setOpaque(false); // встановлення прозорості для панелей
        resetPanel.setOpaque(false);
        exitPanel.setOpaque(false);

        JButton startButton = new RoundedButton("Почати Гру", new Color(0x33CC66), new Color(0x388E3C)); // створення кнопки "Почати гру"
        JButton resetButton = new RoundedButton("Скинути", new Color(0x699BF7), new Color(0x3366CC)); // створення кнопки "Скинути"
        JButton exitButton = new RoundedButton("Вийти", new Color(0xFF8577), new Color(0xD32F2F)); // створення кнопки "Вийти"

        startButton.setPreferredSize(new Dimension(750, 55)); // встановлення розміру для кнопок
        resetButton.setPreferredSize(new Dimension(200, 55));
        exitButton.setPreferredSize(new Dimension(200, 55));

        startPanel.add(startButton); // додавання кнопок на панелі
        resetPanel.add(resetButton);
        exitPanel.add(exitButton);

        buttonPanel.add(startPanel, BorderLayout.CENTER); // розміщення панелей на головній панелі
        buttonPanel.add(resetPanel, BorderLayout.WEST);
        buttonPanel.add(exitPanel, BorderLayout.EAST);

        return buttonPanel; // повернення панелі з кнопками
    }

    // внутрішній клас для RoundBorder (закруглена рамка)
    public static class RoundBorder extends AbstractBorder {

        public final Color color; // колір рамки
        public final int thickness; // товщина рамки
        public final int arcRadius; // радіус закруглення

        // конструктор класу
        public RoundBorder(Color color, int thickness, int arcRadius) {
            this.color = color;
            this.thickness = thickness;
            this.arcRadius = arcRadius;
        }

        // метод для малювання рамки
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create(); // створення копії графіки
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // згладжування

            int adjustedX = x + thickness / 2; // коригування координат для врахування товщини рамки
            int adjustedY = y + thickness / 2;
            int adjustedWidth = width - thickness;
            int adjustedHeight = height - thickness;

            g2d.setColor(color); // встановлення кольору рамки
            g2d.setStroke(new BasicStroke(thickness)); // встановлення товщини рамки
            g2d.drawRoundRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight, arcRadius, arcRadius); // малювання закругленого прямокутника

            g2d.dispose(); // звільнення ресурсів графіки
        }

        // метод для отримання відступів рамки
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness); // повернення відступів з усіх боків
        }

    }

