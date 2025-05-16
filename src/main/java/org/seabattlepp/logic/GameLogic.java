package org.seabattlepp.logic;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.List;

import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.gui.MainFrame;
import org.seabattlepp.ships.ShipPlacer;
import org.seabattlepp.ships.ShipValidator;


public class GameLogic {

    private final MainFrame mainFrame;
    // Двовимірні масиви кнопок, що представляють ігрові поля для кораблів комп'ютера та гравця.
    private final ShipButton[][] computerShipButtons;
    private final ShipButton[][] playerShipButtons;
    // Двовимірні масиви для зберігання об'єктів кораблів, що відстежують їх позиції на полях.
    public Ship[][] computerShipsLocations;
    public Ship[][] playerShipsLocations;
    public boolean isGameStarted;
    public boolean isPlayerTurn;
    private boolean isRandomButtonPressed;
    private final AILogic aiLogic;
    private final UIMarkingLogic uiMarkingLogic;
    private final int[][] playerTargetedArea;
    private final int[][] computerTargetedArea;
    public boolean isGameEnded; // Прапорець для контролю закінчення гри

    public GameLogic(MainFrame mainFrame, ShipButton[][] computerShipButtons, ShipButton[][] playerShipButtons) {
        this.mainFrame = mainFrame;
        this.computerShipButtons = computerShipButtons;
        this.playerShipButtons = playerShipButtons;
        this.aiLogic = new AILogic(this, playerShipButtons);
        this.uiMarkingLogic = new UIMarkingLogic(this, computerShipButtons, playerShipButtons);
        this.isPlayerTurn = true;
        this.isGameStarted = false;
        this.isRandomButtonPressed = false;
        this.isGameEnded = false;
        this.playerTargetedArea = new int[11][11];
        this.computerTargetedArea = new int[11][11];
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
    }

    // Розміщує кораблі випадковим чином на лівому полі (гравця).
    public void placeShipsRandomlyOnLeftBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        resetPlayerShipsLocations();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                playerShipsLocations[row][col] = ship;
            }
        }
        clearLeftBoardShips();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0], col = coord[1];
                ShipButton button = playerShipButtons[row][col];
                if (button != null) {
                    button.setText("⚓");
                    button.setFont(new Font("Inter", Font.BOLD, 50));
                    button.setForeground(Color.BLACK);
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setIcon(null);
                }
            }
        }
    }

    public void placeShipsRandomlyOnRightBoard() {
        ShipPlacer placer = new ShipPlacer(new ShipValidator());
        List<Ship> placedShips = placer.placeShipsRandomly();
        resetComputerShipsLocations();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                computerShipsLocations[row][col] = ship;
            }
        }
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
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
        aiLogic.resetAI();
        isPlayerTurn = true;
        isRandomButtonPressed = false;
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                playerTargetedArea[i][j] = 0;
                computerTargetedArea[i][j] = 0;
            }
        }
        enablePlayerButtonsForPlacement();
        disableComputerButtons();
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

        if (row >= 1 && row <= 10 && col >= 1 && col <= 10) {
            playerTargetedArea[row][col] = 1;
        }

        Ship ship = computerShipsLocations[row][col];
        boolean hit = ship != null;
        boolean sunk = false;

        System.out.println("Player shot: row=" + row + ", col=" + col + ", hit=" + hit);

        if (hit) {
            sunk = markHit(row, col, ship);
            checkGameEnd();
        } else {
            System.out.println("Player missed, marking miss at row=" + row + ", col=" + col);
            if (computerShipButtons[row][col] != null) {
                uiMarkingLogic.markMiss(row, col);
            }
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
        System.out.println("Game started, player turn: " + isPlayerTurn);
    }

    public void setGameStarted(boolean started) {
        isGameStarted = started;
        if (started && !this.isPlayerTurn && !isGameEnded) {
            this.startComputerTurn();
        }
    }

    public void startComputerTurn() {
        if (isGameEnded) return;
        aiLogic.startComputerTurn();
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
                for (int[] coord : ship.getCoordinates()) {
                    int r = coord[0];
                    int c = coord[1];
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int newRow = r + dr;
                            int newCol = c + dc;
                            if (newRow >= 1 && newRow <= 10 && newCol >= 1 && newCol <= 10 && !isComputerShotAt(newRow, newCol)) {
                                markComputerShot(newRow, newCol);
                                System.out.println("Marked surrounding cell as shot in computerTargetedArea: row=" + newRow + ", col=" + newCol);
                            }
                        }
                    }
                }
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


    public void checkGameEnd() {
        if (isGameEnded) return;

        // Перевіряємо, чи всі кораблі гравця потоплені
        boolean playerSunk = true; // Припускаємо, що всі потоплені
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerShipsLocations[i][j] != null && !playerShipsLocations[i][j].isSunk()) {
                    playerSunk = false; // Знайдено непотоплений корабель
                    break; // Виходимо з внутрішнього циклу
                }
            }
            if (!playerSunk) break; // Виходимо з зовнішнього циклу
        }
        if (playerSunk) {
            endGame(false); // Комп'ютер переміг
            return;
        }

        // Перевіряємо, чи всі кораблі комп'ютера потоплені
        boolean computerSunk = true; // Припускаємо, що всі потоплені
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (computerShipsLocations[i][j] != null && !computerShipsLocations[i][j].isSunk()) {
                    computerSunk = false; // Знайдено непотоплений корабель
                    break; // Виходимо з внутрішнього циклу
                }
            }
            if (!computerSunk) break; // Виходимо з зовнішнього циклу
        }
        if (computerSunk) {
            endGame(true); // Гравець переміг
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

    public boolean isPlayerShotAt(int row, int col) {
        return playerTargetedArea[row][col] == 1;
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
}
