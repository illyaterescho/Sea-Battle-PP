package org.seabattlepp.gui;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ShipPanel extends JPanel {

    public ShipPanel() {
        createShipPanel();
    }

    // метод для створення панелі з кораблями та кнопкою Рандом
    public void createShipPanel() {
        setLayout(new GridBagLayout());
        TitledBorder border = BorderFactory.createTitledBorder("Корабельна шпаргалка");
        border.setTitleFont(new Font("Inter", Font.BOLD, 25));
        border.setTitleColor(Color.BLACK);
        border.setBorder(new RoundBorder(Color.BLACK, 3, 40));
        setBorder(border);
        setOpaque(false);

        String[] ships = {"Авіаносець", "Броненосець", "Крейсер", "Руйнівник"};
        int[] shipImageCounts = {5, 4, 3, 2, 2};

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        RoundedButton randomButton = new RoundedButton("Рандом", new Color(0xD0D0D0), new Color(0x787878));
        randomButton.setPreferredSize(new Dimension(150, 55));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(37, 20, 20, 20);
        gbc2.gridx = 35;
        gbc2.gridy = 0;
        add(randomButton, gbc2);

        // створення візуалізації кожного корабля
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
            add(shipContainer, gbc);
        }
    }

    public JLabel getJLabel(int i, int j) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(50, 50));
        if (i == 0 && j == 0) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/pochatokKor.png"));
        } else if (i == 0 && j == 1) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/centerKor.png"));
        } else if (i == 0 && j == 2) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/centerKor.png"));
        } else if (i == 0 && j == 3) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/centerKor.png"));
        } else if (i == 0) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/kinecKor.png"));
        } else if (i == 1 && j == 0) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/pochatokKor.png"));
        } else if (i == 1 && j == 1) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/centerKor.png"));
        } else if (i == 1 && j == 2) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/centerKor.png"));
        } else if (i == 1) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/kinecKor.png"));
        } else if (i == 2 && j == 0) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/pochatokKor.png"));
        } else if (i == 2 && j == 1) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/centerKor.png"));
        } else if (i == 2 && j == 2) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/kinecKor.png"));
        } else if (i == 3 && j == 0) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/pochatokKor.png"));
        } else if (i == 3 && j == 1) {
            imageLabel.setIcon(new ImageIcon("src/main/java/org/seabattlepp/gui/img/kinecKor.png"));
        }
        return imageLabel;
    }
}
