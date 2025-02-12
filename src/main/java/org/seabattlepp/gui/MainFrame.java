package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import static org.seabattlepp.gui.MainFrame.ShipPanelExample.createShipPanel;

public class MainFrame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }


    public MainFrame() {
        setTitle("Морський Бій");
        try {
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/java/org/seabattlepp/img/icon.jpg");
            setIconImage(image);
        } catch (Exception e) {
            System.err.println("Помилка завантаження іконки: " + e.getMessage());
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JPanel boardPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        boardPanel.setOpaque(false);

        Font labelFont = new Font("Inter", Font.BOLD, 35);

        JPanel leftBoard = new JPanel(new BorderLayout());
        leftBoard.setOpaque(false);
        JLabel leftLabel = new JLabel("Ваша Дошка", SwingConstants.CENTER);
        leftLabel.setFont(labelFont);
        leftBoard.add(leftLabel, BorderLayout.NORTH);
        leftBoard.add(createBoard(new Color(0x699BF7)), BorderLayout.CENTER);

        JPanel rightBoard = new JPanel(new BorderLayout());
        rightBoard.setOpaque(false);
        JLabel rightLabel = new JLabel("Комп'ютер", SwingConstants.CENTER);
        rightLabel.setFont(labelFont);
        rightBoard.add(rightLabel, BorderLayout.NORTH);
        rightBoard.add(createBoard(new Color(0xFF8577)), BorderLayout.CENTER);

        boardPanel.add(leftBoard);
        boardPanel.add(rightBoard);

        JPanel shipPanel = createShipPanel();
        shipPanel.setOpaque(false);

        add(boardPanel, BorderLayout.CENTER);
        add(shipPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private JPanel createBoard(Color backgroundColor) {
        JPanel panel = getPanel(backgroundColor);

        JPanel grid = getJPanel(backgroundColor);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j > 0) {
                    JLabel numLabel = new JLabel(String.valueOf(j), SwingConstants.CENTER);
                    numLabel.setFont(new Font("Inter", Font.BOLD, 25));
                    numLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    grid.add(numLabel);
                } else if (j == 0 && i > 0) {
                    JLabel letterLabel = new JLabel(String.valueOf((char) ('A' + i - 1)), SwingConstants.CENTER);
                    letterLabel.setFont(new Font("Inter", Font.BOLD, 25));
                    letterLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                    grid.add(letterLabel);
                } else if (i > 0) {
                    grid.add(new ShipCell());
                } else {
                    grid.add(new JLabel(""));
                }
            }
        }
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel getPanel(Color backgroundColor) {
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

    private static JPanel getJPanel(Color backgroundColor) {
        JPanel grid = new JPanel(new GridLayout(11, 11, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 200, 200);
            }
        };
        grid.setBorder(new CompoundBorder(new RoundBorder(Color.BLACK, 3, 200), new EmptyBorder(5, 5, 80, 50)));
        grid.setOpaque(false);
        return grid;
    }

    public static class ShipPanelExample {
        public static void main(String[] args) {
            JFrame frame = new JFrame("Ship Panel Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(createShipPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        static JPanel createShipPanel() {
            JPanel shipPanel = new JPanel(new GridBagLayout());
            TitledBorder border = BorderFactory.createTitledBorder("Корабельна шпаргалка");
            border.setTitleFont(new Font("Inter", Font.BOLD, 25));
            border.setTitleColor(Color.BLACK);
            border.setBorder(new RoundBorder(Color.BLACK, 3, 40));
            shipPanel.setBorder(border);
            shipPanel.setOpaque(false);

            String[] ships = {"Авіаносець", "Броненосець", "Крейсер", "Руйнівник", "Фрегат"};
            int[] shipImageCounts = {5, 4, 3, 2, 2};

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);

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
                    } else if (i == 4 && j == 0) {
                        imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/pochatokKor.png"));
                    } else if (i == 4 && j == 1) {
                        imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/img/kinecKor.png"));
                    }
                    imagePanel.add(imageLabel);
                }

                shipContainer.add(imagePanel);

                gbc.gridx = i;
                gbc.gridy = 0;
                shipPanel.add(shipContainer, gbc);
            }

            return shipPanel;
        }
    }

    static class RoundedButton extends JButton {
            private final Color backgroundColor;
            private final Color hoverColor;

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

        private JPanel createButtonPanel() {
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
    static class ShipCell extends JButton {
        public ShipCell() {
            setPreferredSize(new Dimension(20, 20));
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

    static class RoundBorder extends AbstractBorder {

        private final Color color;
        private final int thickness;
        private final int arcRadius;

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
