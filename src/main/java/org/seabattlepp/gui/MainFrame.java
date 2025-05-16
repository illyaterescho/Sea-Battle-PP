package org.seabattlepp.gui;

import javax.swing.*;
import java.awt.*;
import org.seabattlepp.logic.GameLogic;

public class MainFrame extends JFrame {

    // 🔷 Кнопки
    public RoundedButton randomButton;
    public JButton startButton;
    public JButton resetButton;

    // 🔷 Панельки
    public BoardPanel boardPanel;
    public ShipPanel shipPanel;
    public ButtonPanel buttonPanel;

    // 🔷 Логіка гри
    private final GameLogic gameLogic;

    public MainFrame() {
        setTitle("Морський Бій");

        // 🖼 Спроба завантажити іконку гри
        try {
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/java/org/seabattlepp/gui/img/icon.png");
            setIconImage(image);
        } catch (Exception e) {
            System.err.println("помилка завантаження іконки: " + e.getMessage());
        }

        // 🔧 Стандартні налаштування JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // 📦 Основна панель контенту з відступами
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // 1️⃣ Створюємо панелі
        boardPanel = new BoardPanel(this);
        shipPanel = new ShipPanel();
        buttonPanel = new ButtonPanel();

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

        // 6️⃣ Додаємо панелі на головне вікно
        add(boardPanel, BorderLayout.CENTER);
        add(shipPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);

        // 📏 Налаштування розміру вікна
        pack();
        setLocationRelativeTo(null);

        // 3️⃣ Створюємо логіку гри
        gameLogic = new GameLogic(this, boardPanel.computerShipButtons, boardPanel.playerShipButtons);

        // 🌀 Налаштування кнопки "Рандом"
        if (randomButton != null) {
            randomButton.setEnabled(false); // Деактивована до початку гри
            randomButton.addActionListener(e -> {
                gameLogic.placeShipsRandomlyOnLeftBoard();
                gameLogic.enableShootingAfterRandom();
                randomButton.setEnabled(true);
                System.out.println("Random button clicked: ships placed and shooting enabled");
            });
        }

        // ▶️ Налаштування кнопки "Старт"
        if (startButton != null) {
            startButton.addActionListener(e -> {
                if (!gameLogic.isGameStarted) {
                    gameLogic.placeShipsRandomlyOnRightBoard(); // Комп'ютер розставляє кораблі
                    if (randomButton != null) {
                        randomButton.setEnabled(true);  // Активуємо "Рандом" для гравця
                    }
                    gameLogic.startGame(); // Запускаємо гру
                    startButton.setEnabled(false); // Забороняємо повторний старт
                }
            });
        }

        // 🔁 Налаштування кнопки "Скинути"
        if (resetButton != null) {
            resetButton.addActionListener(e -> {
                gameLogic.resetBoards(); // Скидаємо стан полів
                if (randomButton != null) {
                    randomButton.setEnabled(false); // Деактивуємо "Рандом" після скидання
                }
                startButton.setEnabled(true); // Дозволяємо знову натиснути "Старт"
            });
        }

        // 🔒 Додатково деактивуємо "Рандом", якщо гра ще не почалась
        if (!gameLogic.isGameStarted) {
            randomButton.setEnabled(false);
        }
    }
}
