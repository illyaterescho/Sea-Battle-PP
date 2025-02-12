package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
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
        setTitle("Морський Бій"); // заголовок вікна

        // Іконка для вікна
        try {
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/java/org/seabattlepp/img/icon.jpg"); // завантаження зображення іконки
            setIconImage(image); // Встановлення іконки
        } catch (Exception e) {
            System.err.println("Помилка завантаження іконки: " + e.getMessage()); // виведення помилки в консоль
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // закриття програми при закритті вікна
        setLayout(new BorderLayout(20, 20)); // макет BorderLayout з відступами

        // Створення панелі
        JPanel contentPane = new JPanel(new BorderLayout()); // Панель 
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // встановлення відступів
        setContentPane(contentPane); // встановлення вмісту панелі

        // Створення панелі для дошок
        JPanel boardPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // 1 ряд, 2 колонки, відступи між компонентами
        boardPanel.setOpaque(false); // прозорість для панелі

        // Налаштування шрифту для написів
        Font labelFont = new Font("Inter", Font.BOLD, 35); // шрифт для написів

        // Створення лівої дошки (гравця)
        JPanel leftBoard = new JPanel(new BorderLayout()); // створення панелі для лівої дошки
        leftBoard.setOpaque(false); // Прозорість
        JLabel leftLabel = new JLabel("Ваша Дошка", SwingConstants.CENTER); // створення напису
        leftLabel.setFont(labelFont); // Встановлення шрифту
        leftBoard.add(leftLabel, BorderLayout.NORTH); // Розміщення напису зверху
        leftBoard.add(createBoard(new Color(0x699BF7)), BorderLayout.CENTER); // додавання дошки в центр

        // Створення правої дошки (комп'ютера)
        JPanel rightBoard = new JPanel(new BorderLayout()); // створення панелі для правої дошки
        rightBoard.setOpaque(false); // Прозорість
        JLabel rightLabel = new JLabel("Комп'ютер", SwingConstants.CENTER); // створення напису
        rightLabel.setFont(labelFont); // Встановлення шрифту
        rightBoard.add(rightLabel, BorderLayout.NORTH); // Розміщення напису зверху
        rightBoard.add(createBoard(new Color(0xFF8577)), BorderLayout.CENTER); // додавання дошки в центр

        boardPanel.add(leftBoard); // додавання лівої дошки на панель
        boardPanel.add(rightBoard); // додавання правої дошки на панель

        // створення панелі з кораблями (знизу)
        JPanel shipPanel = createShipPanel(); // використання статичного методу для створення shipPanel
        shipPanel.setOpaque(false); // прозорість

        // створення панелі з кнопками (зверху)
        JPanel buttonPanel = createButtonPanel(); // створення панелі з кнопками

        // Додавання компонентів на головне вікно
        add(boardPanel, BorderLayout.CENTER); // додавання панелі з дошками в центр
        add(shipPanel, BorderLayout.SOUTH); // додавання панелі з кораблями знизу
        add(buttonPanel, BorderLayout.NORTH); // додавання панелі з кнопками зверху

        pack(); // автоматичне встановлення розміру вікна
        setLocationRelativeTo(null); // відображення вікна по центрі екрану
        setVisible(true); // відображення вікна
    }

    // метод для створення ігрової дошки
    private JPanel createBoard(Color backgroundColor) {
        JPanel panel = getPanel(backgroundColor); // створення панелі для дошки

        JPanel grid = getJPanel(backgroundColor); // створення клітинок для дошки
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j > 0) {
                    // додавання номерів стовпців
                    JLabel numLabel = new JLabel(String.valueOf(j), SwingConstants.CENTER); // створення напису з номером стовпця
                    numLabel.setFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту
                    numLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // видалення стандартної рамки
                    grid.add(numLabel); // додавання напису
                } else if (j == 0 && i > 0) {
                    // Додавання літер рядків
                    JLabel letterLabel = new JLabel(String.valueOf((char) ('A' + i - 1)), SwingConstants.CENTER); // створення напису з літерою рядка
                    letterLabel.setFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту
                    letterLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // видалення стандартної рамки
                    grid.add(letterLabel); // додавання напису
                } else if (i > 0) {
                    // додавання клітинок для кораблів
                    grid.add(new ShipCell()); // додавання клітинки
                } else {
                    grid.add(new JLabel("")); // пусті клітинки
                }
            }
        }
        panel.add(grid, BorderLayout.CENTER); // додавання клітинок на панель
        return panel; // повернення панелі з дошкою
    }

    // метод для створення панелі з заданим кольором фону
    private static JPanel getPanel(Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // виклик батьківського методу для малювання фону
                Graphics2D g2d = (Graphics2D) g; // створення об'єкту Graphics2D для малювання
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // включення згладжування
                g2d.setColor(backgroundColor); // встановлення кольору фону
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 220, 200); // малювання закругленого прямокутника
            }
        };
        panel.setOpaque(true); // встановлення непрозорості панелі
        return panel; // повернення панелі
    }

    // метод для створення сітки з заданим кольором фону
    private static JPanel getJPanel(Color backgroundColor) {
        JPanel grid = new JPanel(new GridLayout(11, 11, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // виклик батьківського методу для малювання фону
                Graphics2D g2d = (Graphics2D) g; // створення об'єкту Graphics2D для малювання
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
    class ShipPanelExample {
        // статичний метод для створення панелі з кораблями
        public static JPanel createShipPanel() {
            JPanel shipPanel = new JPanel(new GridBagLayout()); // використання GridBagLayout для гнучкого розміщення
            TitledBorder border = BorderFactory.createTitledBorder("Корабельна шпаргалка"); // створення заголовку для панелі
            border.setTitleFont(new Font("Inter", Font.BOLD, 25)); // встановлення шрифту для заголовку
            border.setTitleColor(Color.BLACK); // Встановлення кольору для заголовку
            border.setBorder(new RoundBorder(Color.BLACK, 3, 40)); // встановлення закругленої рамки для заголовку
            shipPanel.setBorder(border); // встановлення заголовку для панелі
            shipPanel.setOpaque(false); // встановлення прозорості для панелі

            String[] ships = {"Авіаносець", "Броненосець", "Крейсер", "Руйнівник", "Фрегат"}; // масив з назвами кораблів
            int[] shipImageCounts = {5, 4, 3, 2, 2}; // масив з кількістю зображень для кожного корабля

            GridBagConstraints gbc = new GridBagConstraints(); // створення об'єкту GridBagConstraints для налаштування розміщення компонентів
            gbc.insets = new Insets(20, 20, 20, 20); // встановлення відступів між компонентами

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
                        imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png")); // Фрегат - початок
                    } else if (i == 4 && j == 1) {
                        imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png")); // Фрегат - кінець
                    }
                    imagePanel.add(imageLabel); // додавання зображення на панель
                }

                shipContainer.add(imagePanel); // додавання панелі зображень на панель корабля

                gbc.gridx = i; // встановлення позиції корабля в сітці
                gbc.gridy = 0;
                shipPanel.add(shipContainer, gbc); // додавання панелі корабля на головну панель
            }

            return shipPanel; // повернення готової панелі з кораблями
        }
    }

    // внутрішній клас для RoundedButton (закруглена кнопка)
    static class RoundedButton extends JButton {
        private final Color backgroundColor; // колір фону кнопки
        private final Color hoverColor; // колір фону при наведенні курсору

        // Конструктор класу
        public RoundedButton(String text, Color backgroundColor, Color hoverColor) {
            super(text); // виклик конструктора батьківського класу
            this.backgroundColor = backgroundColor; // встановлення кольору фону
            this.hoverColor = hoverColor; // встановлення кольору при наведенні курсору
            setContentAreaFilled(false); // вимкнення заповнення області контенту кнопки
            setFocusPainted(false); // вимкнення малювання фокуса
            setBorderPainted(false); // вимкнення малювання рамки
            setFont(new Font("Inter", Font.BOLD, 20)); // встановлення шрифту для напису
        }

        // перемалювання компонента
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); // створення копії графіки
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // включення згладжування

            g2.setColor(getModel().isRollover() ? hoverColor : backgroundColor); // встановлення кольору фону в залежності від стану кнопки
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // малювання закругленого прямокутника

            g2.setColor(Color.BLACK); // встановлення кольору рамки
            g2.setStroke(new BasicStroke(2)); // встановлення товщини рамки
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30); // малювання рамки

            g2.dispose(); // звільнення ресурсів графіки

            super.paintComponent(g); // виклик батьківського методу для відображення інших компонентів
        }
    }

    // метод для створення панелі з кнопками
    private JPanel createButtonPanel() {
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

    // внутрішній клас для ShipCell (клітинка корабля)
    static class ShipCell extends JButton {
        public ShipCell() {
            setPreferredSize(new Dimension(20, 20)); // розмір клітинки
            setContentAreaFilled(false); // не заповнювати область контенту кнопки
            setOpaque(false); // прозора кнопка
            setBackground(Color.WHITE); // білий фон
            setBorder(new RoundBorder(Color.BLACK, 4, 45)); // закруглена рамка
        }

        // перемалювання компонента
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create(); // створення копії графіки
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // згладжування

            g2d.setColor(getBackground()); // встановлення кольору фону
            g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 45, 45); // заповнення закругленого прямокутника

            super.paintComponent(g); // виклик батьківського методу для відображення інших компонентів

            g2d.dispose(); // звільнення ресурсів графіки
        }
    }

    // внутрішній клас для RoundBorder (закруглена рамка)
    static class RoundBorder extends AbstractBorder {

        private final Color color; // колір рамки
        private final int thickness; // товщина рамки
        private final int arcRadius; // радіус закруглення

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
}