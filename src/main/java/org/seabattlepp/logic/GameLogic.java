package org.seabattlepp.logic;

import org.seabattlepp.logic.ai.AILogic;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.gui.MainFrame;
import org.seabattlepp.ships.ShipPlacer;
import org.seabattlepp.ships.ShipValidator;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.io.File;
import java.util.List;

public class GameLogic {

    private final MainFrame mainFrame;
    private final ShipButton[][] computerShipButtons;
    private final ShipButton[][] playerShipButtons;
    private Ship[][] computerShipsLocations;
    private Ship[][] playerShipsLocations;
    private boolean isGameStarted;
    private boolean isPlayerTurn;
    private boolean isRandomButtonPressed;
    private final AILogic aiLogic;
    private final UIMarkingLogic uiMarkingLogic;
    private int[][] playerTargetedArea;
    private int[][] computerTargetedArea;
    private boolean isGameEnded; // Прапорець для контролю закінчення гри

    public GameLogic(MainFrame mainFrame, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.mainFrame = mainFrame;
        this.computerShipButtons = computerShipButtons;
        this.playerShipButtons = playerShipButtons;
        this.aiLogic = new AILogic(this, playerShipButtons);
        this.uiMarkingLogic = new UIMarkingLogic(this, computerShipButtons, playerShipButtons);
        this.isPlayerTurn = true;
        this.isGameStarted = false;
        this.isRandomButtonPressed = false;
        this.isGameEnded = false; // Ініціалізація прапорця
        this.playerTargetedArea = new int[11][11];
        this.computerTargetedArea = new int[11][11];
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
    }

    public void placeShipsRandomlyOnLeftBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        setPlayerShips(placedShips);
        clearLeftBoardShips();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0], col = coord[1];
                ShipButton btn = playerShipButtons[row][col];
                if (btn != null) {
                    btn.setText("⚓");
                    btn.setFont(new Font("Inter", Font.BOLD, 50));
                    btn.setForeground(Color.BLACK);
                    btn.setBackground(Color.WHITE);
                    btn.setEnabled(true);
                    btn.setIcon(null);
                }
            }
        }
    }

    public void placeShipsRandomlyOnRightBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        setComputerShips(placedShips);
        clearRightBoardShips();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0], col = coord[1];
                ShipButton button = computerShipButtons[row][col];
                if (button != null) {
                    button.setBackground(Color.WHITE);
                    button.setFont(new Font("Inter", Font.BOLD, 25));
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setIcon(null);
                }
            }
        }
    }

    public void clearLeftBoardShips() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null) {
                    button.setText("");
                    button.setIcon(null);
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setUI(new BasicButtonUI());
                }
            }
        }
    }

    public void clearRightBoardShips() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = computerShipButtons[i][j];
                if (button != null) {
                    button.setText("");
                    button.setIcon(null);
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setUI(new BasicButtonUI());
                }
            }
        }
    }

    public void resetBoards() {
        clearLeftBoardShips();
        clearRightBoardShips();
        isGameStarted = false;
        isRandomButtonPressed = false;
        // Не скидаємо isGameEnded тут, щоб уникнути повторного виклику endGame
        resetGame();
        enablePlayerButtonsForPlacement();
        disableComputerButtons();
    }

    public void setGameStarted(boolean started) {
        isGameStarted = started;
        if (started && !isPlayerTurn() && !isGameEnded) {
            this.startComputerTurn();
        }
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void enablePlayerButtonsForPlacement() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null) {
                    button.setEnabled(true);
                }
            }
        }
    }

    public void enableComputerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = computerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED && !isPlayerShotAt(i, j)) {
                    if (isRandomButtonPressed) {
                        button.setEnabled(true);
                    } else {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }

    public void enablePlayerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED) {
                    if (button.getText() == null || !button.getText().equals("⚓")) {
                        button.setEnabled(true);
                    }
                }
            }
        }
    }

    public void disableComputerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = computerShipButtons[i][j];
                if (button != null) {
                    button.setEnabled(false);
                }
            }
        }
    }

    public void disablePlayerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null) {
                    if (button.getText() == null || !button.getText().equals("⚓")) {
                        button.setEnabled(false);
                    }
                }
            }
        }
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
        System.out.println("Turn set to: " + (playerTurn ? "Player" : "Computer"));
    }

    public void processPlayerShot(int row, int col) {
        if (!isPlayerTurn || isGameEnded) return;

        if (!isRandomButtonPressed) {
            System.out.println("Player tried to shoot before pressing Random button: row=" + row + ", col=" + col);
            return;
        }

        if (isPlayerShotAt(row, col)) {
            System.out.println("Player tried to shoot at already used cell: row=" + row + ", col=" + col);
            return;
        }

        markPlayerShot(row, col);

        Ship ship = getShipAt(row, col);
        boolean hit = ship != null;
        boolean sunk = false;

        System.out.println("Player shot: row=" + row + ", col=" + col + ", hit=" + hit);

        if (hit) {
            sunk = markHit(row, col, ship);
            checkGameEnd();
        } else {
            System.out.println("Player missed, marking miss at row=" + row + ", col=" + col);
            markMiss(row, col);
            setPlayerTurn(false);
            disableComputerButtons();
            enablePlayerButtons();
            if (isGameStarted && !isGameEnded) {
                this.startComputerTurn();
            }
        }

        ShipButton button = computerShipButtons[row][col];
        if (button != null) {
            button.setEnabled(false);
        }

        if (hit && !sunk && isGameStarted && !isGameEnded) {
            enableComputerButtons();
        }
    }

    public void startPlayerTurn() {
        if (isGameEnded) return;
        setPlayerTurn(true);
        enableComputerButtons();
        System.out.println("Player turn started");
    }

    public void startGame() {
        if (isGameEnded) {
            isGameEnded = false; // Скидаємо прапорець перед новою грою
        }
        isPlayerTurn = true;
        setGameStarted(true);
        disableComputerButtons();
        disablePlayerButtons();
        aiLogic.resetAI();
        startPlayerTurn();
        addComputerBoardListeners();
        System.out.println("Game started, player turn: " + isPlayerTurn);
    }

    private void addComputerBoardListeners() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int row = i;
                int col = j;
                ShipButton button = computerShipButtons[row][col];

                if (button != null) {
                    for (var l : button.getActionListeners()) {
                        button.removeActionListener(l);
                    }

                    button.addActionListener(e -> {
                        if (button.isEnabled()) {
                            System.out.println("Player clicked: row=" + row + ", col=" + col);
                            processPlayerShot(row, col);
                        }
                    });
                } else {
                    System.err.println("Button is null at computerShipButtons[" + row + "][" + col + "]");
                }
            }
        }
    }

    public void startComputerTurn() {
        if (isGameEnded) return;
        aiLogic.startComputerTurn();
    }

    public Ship getShipAt(int row, int col) {
        return computerShipsLocations[row][col];
    }

    public Ship getPlayerShipAt(int row, int col) {
        return playerShipsLocations[row][col];
    }

    public boolean markHit(int row, int col, Ship ship) {
        boolean sunk = false;
        if (computerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();
            if (sunk) {
                uiMarkingLogic.markSunkShip(ship);
            } else {
                uiMarkingLogic.markHitSymbol(computerShipButtons[row][col]);
            }
        }
        return sunk;
    }

    public boolean markHitPlayerBoard(int row, int col, Ship ship) {
        if (isGameEnded || !isCellAvailableForShot(row, col) || isComputerShotAt(row, col)) {
            System.out.println("Computer tried to shoot at already used cell or game ended: row=" + row + ", col=" + col);
            return false;
        }

        markComputerShot(row, col);

        boolean sunk = false;
        if (playerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();
            if (sunk) {
                uiMarkingLogic.markSunkShipPlayerBoard(ship);
                markSurroundingCellsAsShot(ship);
                checkGameEnd();
                if (isGameEnded) {
                    aiLogic.stopAI(); // Зупиняємо AI після завершення гри
                }
            } else {
                uiMarkingLogic.markHitSymbolPlayerBoard(playerShipButtons[row][col]);
            }
        }
        return sunk;
    }

    private void markSurroundingCellsAsShot(Ship ship) {
        for (int[] coord : ship.getCoordinates()) {
            int row = coord[0];
            int col = coord[1];
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int newRow = row + dr;
                    int newCol = col + dc;
                    if (isValidCell(newRow, newCol) && !isComputerShotAt(newRow, newCol)) {
                        markComputerShot(newRow, newCol);
                        System.out.println("Marked surrounding cell as shot in computerTargetedArea: row=" + newRow + ", col=" + newCol);
                    }
                }
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }

    void markMiss(int row, int col) {
        if (computerShipButtons[row][col] != null) {
            uiMarkingLogic.markMiss(row, col);
        }
    }

    public void markMissPlayerBoard(int row, int col) {
        if (isGameEnded || !isCellAvailableForShot(row, col) || isComputerShotAt(row, col)) {
            System.out.println("Computer tried to shoot at already used cell or game ended: row=" + row + ", col=" + col);
            return;
        }

        markComputerShot(row, col);

        if (playerShipButtons[row][col] != null) {
            uiMarkingLogic.markMissPlayerBoard(row, col);
        }
    }

    public void resetComputerShipsLocations() {
        computerShipsLocations = new Ship[11][11];
    }

    public void resetPlayerShipsLocations() {
        playerShipsLocations = new Ship[11][11];
    }

    public void setComputerShips(List<Ship> ships) {
        resetComputerShipsLocations();
        for (Ship ship : ships) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                computerShipsLocations[row][col] = ship;
            }
        }
    }

    public void setPlayerShips(List<Ship> ships) {
        resetPlayerShipsLocations();
        for (Ship ship : ships) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                playerShipsLocations[row][col] = ship;
            }
        }
    }

    private boolean computerShipsLocationsIsEmpty() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (computerShipsLocations[i][j] != null && !computerShipsLocations[i][j].isSunk()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean playerShipsLocationsIsEmpty() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerShipsLocations[i][j] != null && !playerShipsLocations[i][j].isSunk()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkGameEnd() {
        if (isGameEnded) return;

        boolean playerSunk = playerShipsLocationsIsEmpty();
        if (playerSunk) {
            endGame(false);
            return;
        }

        boolean computerSunk = computerShipsLocationsIsEmpty();
        if (computerSunk) {
            endGame(true);
        }
    }

    private void endGame(boolean playerWon) {
        if (isGameEnded) return;
        isGameEnded = true;
        System.out.println("Ending game with playerWon=" + playerWon + " at " + new java.util.Date());
        SwingUtilities.invokeLater(() -> {
            // Створюємо кастомний JDialog
            JDialog dialog = new JDialog(mainFrame, "Кінець гри!", true);
            dialog.setSize(400, 200);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setLayout(new BorderLayout());

            // Панель без синього градієнтного фону
            JPanel backgroundPanel = new JPanel();
            backgroundPanel.setLayout(new BorderLayout());
            backgroundPanel.setBackground(Color.WHITE); // Встановлюємо білий фон
            dialog.add(backgroundPanel);

            // Текст повідомлення
            JLabel messageLabel = new JLabel(playerWon ? "<html><h1 style='color:gold; font-family:Old English Text MT, serif;'>Ви перемогли!</h1></html>" :
                    "<html><h1 style='color:gold; font-family:Old English Text MT, serif;'>Комп'ютер переміг!</h1></html>");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            backgroundPanel.add(messageLabel, BorderLayout.CENTER);

            // Панель для кнопок
            JPanel buttonPanel = new JPanel(new FlowLayout());
            RoundedButton newGameButton = new RoundedButton("Нова гра");
            RoundedButton closeButton = new RoundedButton("Вийти");

            // Стилізація кнопок
            newGameButton.setForeground(new Color(0, 100, 0)); // Зелене текстове забарвлення
            closeButton.setForeground(new Color(139, 0, 0)); // Червоне текстове забарвлення
            newGameButton.setFont(new Font("SansSerif", Font.BOLD, 18)); // Стандартний шрифт
            closeButton.setFont(new Font("SansSerif", Font.BOLD, 18)); // Стандартний шрифт
            newGameButton.setOpaque(false); // Прибрати фон
            closeButton.setOpaque(false); // Прибрати фон
            newGameButton.setFocusPainted(false);
            closeButton.setFocusPainted(false);
            newGameButton.setContentAreaFilled(false); // Прибрати заповнення
            closeButton.setContentAreaFilled(false); // Прибрати заповнення
            newGameButton.setBorderPainted(false); // Прибрати стандартну рамку
            closeButton.setBorderPainted(false); // Прибрати стандартну рамку

            // Збільшуємо розміри кнопок
            newGameButton.setPreferredSize(new Dimension(120, 40)); // Більший розмір
            closeButton.setPreferredSize(new Dimension(120, 40)); // Більший розмір

            // Дії для кнопок
            newGameButton.addActionListener(e -> {
                dialog.dispose();
                resetBoards();
                startGame();
            });
            closeButton.addActionListener(e -> {
                dialog.dispose();
                System.exit(0);
            });

            buttonPanel.add(newGameButton);
            buttonPanel.add(closeButton);
            backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Відображення діалогу
            dialog.setVisible(true);
            System.out.println("Dialog shown for playerWon=" + playerWon + " at " + new java.util.Date());
            setGameStarted(false);
            mainFrame.randomButton.setEnabled(false);
            mainFrame.startButton.setEnabled(true);
            aiLogic.stopAI(); // Зупиняємо AI перед скиданням
            resetBoards();
            aiLogic.resetAI();
            isGameEnded = false; // Скидаємо після завершення всіх дій
            System.out.println("Game reset after end with playerWon=" + playerWon + " at " + new java.util.Date());
        });
    }

    // Внутрішній клас для заокруглених кнопок
    private static class RoundedButton extends JButton {
        private static final int ARC_WIDTH = 20;
        private static final int ARC_HEIGHT = 20;

        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            g2.dispose();
        }
    }

    // Метод для отримання кастомного шрифту (залишив для сумісності, хоча більше не використовується в endGame)
    private Font getCustomFont() {
        try {
            // Спроба завантажити шрифт із файлу (замініть шлях на реальний)
            File fontFile = new File("src/main/resources/fonts/PiratesBay.ttf"); // Приклад шляху
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 16);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;
        } catch (Exception e) {
            // Якщо шрифт не знайдено, використовуємо доступний системний шрифт
            System.err.println("Custom font not found, using Old English Text MT or default");
            return new Font("Old English Text MT", Font.BOLD, 16);
        }
    }

    public void resetGame() {
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
        aiLogic.resetAI();
        isPlayerTurn = true;
        isRandomButtonPressed = false;
        // Не скидаємо isGameEnded тут
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                playerTargetedArea[i][j] = 0;
                computerTargetedArea[i][j] = 0;
            }
        }
    }

    public boolean isPlayerShotAt(int row, int col) {
        return playerTargetedArea[row][col] == 1;
    }

    private void markPlayerShot(int row, int col) {
        if (row >= 1 && row <= 10 && col >= 1 && col <= 10) {
            playerTargetedArea[row][col] = 1;
        }
    }

    public boolean isComputerShotAt(int row, int col) {
        return computerTargetedArea[row][col] == 1;
    }

    public void markComputerShot(int row, int col) {
        if (row >= 1 && row <= 10 && col >= 1 && col <= 10) {
            computerTargetedArea[row][col] = 1;
        }
    }

    public void enableShootingAfterRandom() {
        isRandomButtonPressed = true;
        System.out.println("Shooting enabled after Random button press");
        enableComputerButtons();
    }

    public boolean isCellAvailableForShot(int row, int col) {
        ShipButton button = playerShipButtons[row][col];
        if (button != null) {
            String text = button.getText();
            if ("•".equals(text)) {
                return false;
            }
            return button.getIcon() == null;
        }
        return false;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }
}
