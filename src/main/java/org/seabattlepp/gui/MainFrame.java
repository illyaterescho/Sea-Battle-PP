package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {

    private BoardPanel boardPanel; // панель для дошок
    private ShipPanel shipPanel;   // панель для кораблів
    private ButtonPanel buttonPanel; // панель для кнопок
    private RoundedButton randomButton; // кнопка "Рандом"

    public MainFrame() {

        // налаштування вікна заголовок, іконка, закриття
        setTitle("Морський Бій");
        try {
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/java/org/seabattlepp/img/icon.jpg");
            setIconImage(image);
        } catch (Exception e) {
            System.err.println("Помилка завантаження іконки: " + e.getMessage());
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // створення панелі та відступів
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // створення панелі для дошок
        boardPanel = new BoardPanel(this);

        // створення панелі з кораблями (знизу)
        shipPanel = new ShipPanel();

        // створення панелі з кнопками (зверху)
        buttonPanel = new ButtonPanel();

        // отримання кнопки "Рандом" з shipPanel після того як ShipPanel спрацювало
        for (Component comp : shipPanel.getComponents()) {
            if (comp instanceof RoundedButton && ((RoundedButton) comp).getText().equals("Рандом")) {
                randomButton = (RoundedButton) comp;
                break;
            }
        }

        // додавання ActionListener до кнопки "Рандом"
        if (randomButton != null) {
            randomButton.addActionListener(e -> boardPanel.placeShipsRandomlyOnLeftBoard()); // виклик методу через boardPanel
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
}