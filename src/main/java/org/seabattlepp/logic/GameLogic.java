package org.seabattlepp.logic;

import org.seabattlepp.logic.ai.AILogic;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.gui.MainFrame;
import org.seabattlepp.gui.BoardPanel;
import org.seabattlepp.ships.ShipPlacer;
import org.seabattlepp.ships.ShipValidator;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.List;

public class GameLogic {

    private final MainFrame mainFrame;
    private final BoardPanel boardPanel;

    private final ShipButton[][] computerShipButtons;
    private final ShipButton[][] playerShipButtons;

    private Ship[][] computerShipsLocations;
    private Ship[][] playerShipsLocations;

    private boolean isGameStarted;
    private boolean isPlayerTurn;

    private final AILogic aiLogic;
    private final UIMarkingLogic uiMarkingLogic;

    public GameLogic(
            MainFrame mainFrame,
            BoardPanel boardPanel,
            ShipButton[][] computerShipButtons,
            ShipButton[][] playerShipButtons
    ) {
        this.boardPanel = boardPanel;
        this.mainFrame = mainFrame;

        this.computerShipButtons = computerShipButtons;
        this.playerShipButtons = playerShipButtons;

        this.aiLogic = new AILogic(this, boardPanel, playerShipButtons);
        this.uiMarkingLogic = new UIMarkingLogic(this, boardPanel, computerShipButtons, playerShipButtons);
        this.isPlayerTurn = true;
        this.isGameStarted = false;

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
        resetGame();
        enablePlayerButtonsForPlacement();
        disableComputerButtons();
    }

    public void setGameStarted(boolean started) {
        isGameStarted = started;
        if (started && !isPlayerTurn()) startComputerTurn();
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
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED) {
                    button.setEnabled(true);
                }
            }
        }
    }

    public void enablePlayerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = playerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED) {
                    button.setEnabled(true);
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
                    button.setEnabled(false);
                }
            }
        }
    }


    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    // встановлення черги гравця використовуємо AILogic та PlayerLogic
    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public void processShot(int row, int col) {
        if (!isPlayerTurn) return;

        Ship ship = getShipAt(row, col);
        boolean hit = ship != null;
        boolean sunk = false;

        if (hit) {
            sunk = markHit(row, col, ship);
            checkGameEnd(); // додай завжди
            if (!sunk) {
                enableComputerButtons(); // дозвіл стріляти далі
            }
        } else {
            markMiss(row, col);
            setPlayerTurn(false);
            disableComputerButtons();
            enablePlayerButtons();
            startComputerTurn();
            // AI починає хід
        }

        if (hit && !sunk) {
            // якщо просто влучив — гравець продовжує
            enableComputerButtons();
        }
    }

    public void startPlayerTurn() {
        setPlayerTurn(true);
        enableComputerButtons();
    }

    // ▶️ Старт гри
    public void startGame() {
        isPlayerTurn = true;
        setGameStarted(true);
        enableComputerButtons();
        disablePlayerButtons();
        aiLogic.resetAI();
        startPlayerTurn();

        addComputerBoardListeners();
    }

    private void addComputerBoardListeners() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int row = i;
                int col = j;
                ShipButton button = computerShipButtons[row][col];

                if (button != null) {
                    // очищення попередніх слухачів
                    for (var l : button.getActionListeners()) {
                        button.removeActionListener(l);
                    }

                    // додаємо слухача
                    button.addActionListener(e -> {
                        if (button.isEnabled()) {
                            processShot(row, col); // Гравець стріляє
                            button.setEnabled(false); // блокуємо після кліку
                        }
                    });
                }
            }
        }
    }

    // ⏩ Хід комп'ютера
    public void startComputerTurn() {
        aiLogic.startComputerTurn();
    }

    // отримання корабля комп'ютера за координатами використовується PlayerLogic
    public Ship getShipAt(int row, int col) {
        return computerShipsLocations[row][col];
    }

    // Отримати корабель гравця за координатами
    public Ship getPlayerShipAt(int row, int col) {
        return playerShipsLocations[row][col];
    }

    // ✅ Обробка попадання гравця по кораблю комп’ютера
    public boolean markHit(int row, int col, Ship ship) {
        boolean sunk = false;
        if (computerShipButtons[row][col] != null) {
            ship.takeHit();           // зафіксували влучання
            sunk = ship.isSunk();     // чи потоплено

            if (sunk) {
                uiMarkingLogic.markSunkShip(ship); // показати потоплення
            } else {
                uiMarkingLogic.markHitSymbol(computerShipButtons[row][col]); // звичайне влучання
            }
        }
        return sunk;
    }

    // ✅ Обробка попадання ШІ по кораблю гравця
    public boolean markHitPlayerBoard(int row, int col, Ship ship) {
        boolean sunk = false;
        if (playerShipButtons[row][col] != null) {
            ship.takeHit();
            sunk = ship.isSunk();

            if (sunk) {
                uiMarkingLogic.markSunkShipPlayerBoard(ship); // Використовуємо UIMarkingLogic
            } else {
                uiMarkingLogic.markHitSymbolPlayerBoard(playerShipButtons[row][col]);
            }
        }
        return sunk;
    }

    // ❌ Промах гравця
    void markMiss(int row, int col) {
        if (computerShipButtons[row][col] != null) {
            uiMarkingLogic.markMiss(row, col); // Використовуємо UIMarkingLogic
        }
    }

    // ❌ Промах комп’ютера
    public void markMissPlayerBoard(int row, int col) {
        if (playerShipButtons[row][col] != null) {
            uiMarkingLogic.markMissPlayerBoard(row, col); // Використовуємо UIMarkingLogic
        }
    }

    // 🔄 Скидання координат кораблів комп’ютера
    public void resetComputerShipsLocations() {
        computerShipsLocations = new Ship[11][11];
    }

    // 🔄 Скидання координат кораблів гравця
    public void resetPlayerShipsLocations() {
        playerShipsLocations = new Ship[11][11];
    }

    // ↩️ Завантажити розміщення кораблів комп’ютера
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

    // ↩️ Завантажити розміщення кораблів гравця
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


    // 📉 Чи всі кораблі комп’ютера потоплено?
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

    // 📉 Чи всі кораблі гравця потоплено?
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


    // 🧠 Перевірка завершення гри
    public void checkGameEnd() {
        boolean computerSunk = computerShipsLocationsIsEmpty();
        boolean playerSunk = playerShipsLocationsIsEmpty();

        if (computerSunk) {
            endGame(true);
        } else if (playerSunk) {
            endGame(false);
        }
    }

    // 🎯 Завершення гри: показ повідомлення, скидання стану
    private void endGame(boolean playerWon) {
        JOptionPane.showMessageDialog(
                mainFrame,
                playerWon ? "Ви перемогли!" : "Комп'ютер переміг!",
                "Кінець гри!",
                JOptionPane.INFORMATION_MESSAGE
        );
        setGameStarted(false);
        mainFrame.disableRandomButton();
        resetBoards();
        aiLogic.resetAI();
    }

    // 🔄 Повне скидання гри
    public void resetGame() {
        resetComputerShipsLocations();
        resetPlayerShipsLocations();
        aiLogic.resetAI();
        isPlayerTurn = true;
    }
}
