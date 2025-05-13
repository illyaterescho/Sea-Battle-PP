package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private BoardPanel boardPanel;
    private ShipPanel shipPanel;
    private ButtonPanel buttonPanel;
    private RoundedButton randomButton;
    private JButton startButton;
    private JButton resetButton;
    private JButton exitButton;

    public MainFrame() {

        setTitle("Морський Бій");
        try {
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/java/org/seabattlepp/img/icon.png");
            setIconImage(image);
        } catch (Exception e) {
            System.err.println("помилка завантаження іконки: " + e.getMessage());
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        boardPanel = new BoardPanel(this);

        shipPanel = new ShipPanel();

        buttonPanel = new ButtonPanel();
        startButton = buttonPanel.getStartButton();
        resetButton = buttonPanel.getResetButton();
        exitButton = buttonPanel.getExitButton();

        for (Component comp : shipPanel.getComponents()) {
            if (comp instanceof RoundedButton && ((RoundedButton) comp).getText().equals("Рандом")) {
                randomButton = (RoundedButton) comp;
                break;
            }
        }

        if (randomButton != null) {
            randomButton.setEnabled(false);
        }

        if (randomButton != null) {
            randomButton.addActionListener(e -> boardPanel.placeShipsRandomlyOnLeftBoard());
        }

        if (startButton != null) {
            startButton.addActionListener(e -> {
                if (!boardPanel.isGameStarted()) { // перевіряємо чи гра почалась
                    boardPanel.placeShipsRandomlyOnRightBoard();
                    boardPanel.setGameStarted(true);
                    if (randomButton != null) {
                        randomButton.setEnabled(true);
                    }
                    boardPanel.gameLogic.startGame(); // старт для початку гри
                    startButton.setEnabled(false); // кнопка Початок Гри не активна стає після початку
                }
            });
        }

        if (resetButton != null) {
            resetButton.addActionListener(e -> {
                boardPanel.resetBoards();
                if (randomButton != null) {
                    randomButton.setEnabled(false);
                }
                startButton.setEnabled(true); // після скидання робимо Початок Гри знову активною
            });
        }


        add(boardPanel, BorderLayout.CENTER);
        add(shipPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void disableRandomButton() {
        if (randomButton != null) {
            randomButton.setEnabled(false);
        }
    }
}