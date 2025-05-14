package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
import org.seabattlepp.logic.GameLogic;
import org.seabattlepp.ships.BoardController;

public class MainFrame extends JFrame {

    // панельки
    private BoardPanel boardPanel;
    private ShipPanel shipPanel;
    private ButtonPanel buttonPanel;

    // контролер і логіка
    private BoardController boardController;
    private GameLogic gameLogic;

    // кнопки
    private RoundedButton randomButton;
    private JButton startButton;
    private JButton resetButton;

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

        // 2️⃣ Створюємо контролер дошки
        boardController = new BoardController(boardPanel);

        // 3️⃣ Створюємо логіку гри
        gameLogic = new GameLogic(this, boardController, boardPanel.computerShipButtons, boardPanel.playerShipButtons);
        boardController.setGameLogic(gameLogic);

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
            randomButton.addActionListener(e -> boardController.placeShipsRandomlyOnLeftBoard());
        }

        if (startButton != null) {
            startButton.addActionListener(e -> {
                if (!boardController.isGameStarted()) {
                    boardController.placeShipsRandomlyOnRightBoard();
                    boardController.setGameStarted(true);
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
                boardController.resetBoards();
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
