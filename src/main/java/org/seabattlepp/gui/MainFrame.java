package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
import org.seabattlepp.logic.GameLogic;

public class MainFrame extends JFrame {

    // панельки
    private final BoardPanel boardPanel;
    private final ShipPanel shipPanel;
    private final ButtonPanel buttonPanel;

    // логіка
    private final GameLogic gameLogic;

    // кнопки
    private RoundedButton randomButton;
    private final JButton startButton;
    private final JButton resetButton;

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

        // 1️⃣ Створюємо панелі
        boardPanel = new BoardPanel(this);
        shipPanel = new ShipPanel();
        buttonPanel = new ButtonPanel();


        // 3️⃣ Створюємо логіку гри
        gameLogic = new GameLogic(this, boardPanel, boardPanel.computerShipButtons, boardPanel.playerShipButtons);


        // 4️⃣ Кнопки
        startButton = buttonPanel.getStartButton();
        resetButton = buttonPanel.getResetButton();

        // 5️⃣ Шукаємо кнопку "Рандом"
        for (Component comp : shipPanel.getComponents()) {
            if (comp instanceof RoundedButton && ((RoundedButton) comp).getText().equals("Рандом")) {
                randomButton = (RoundedButton) comp;
                break;
            }
        }

        if (randomButton != null) {
            randomButton.setEnabled(false);
            randomButton.addActionListener(e -> gameLogic.placeShipsRandomlyOnLeftBoard());
        }

        if (startButton != null) {
            startButton.addActionListener(e -> {
                if (!gameLogic.isGameStarted()) {
                    gameLogic.placeShipsRandomlyOnRightBoard();
                    if (randomButton != null) {
                        randomButton.setEnabled(true);
                    }
                    gameLogic.startGame();
                    startButton.setEnabled(false);
                }
            });
        }

        if (resetButton != null) {
            resetButton.addActionListener(e -> {
                gameLogic.resetBoards();
                if (randomButton != null) {
                    randomButton.setEnabled(false);
                }
                startButton.setEnabled(true);
            });
        }

        // 6️⃣ Додаємо все на екран
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
