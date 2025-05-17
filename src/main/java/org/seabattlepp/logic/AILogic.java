package org.seabattlepp.logic;

import org.seabattlepp.gui.MainFrame;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.ships.Ship;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AILogic {

    private final AIStrategy aiStrategy;
    public  MainFrame mainFrame;
    private Timer timer; // Поле для Timer

    public AILogic(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.aiStrategy = new AIStrategy();
    }

    public void resetBot() {
        aiStrategy.reset();
        if (timer != null) {
            timer.stop();
        }
    }

    // точка входу в хід комп'ютера
    public void startComputerTurn() {
        if (!mainFrame.gameLogic.isGameStarted || mainFrame.gameLogic.isGameEnded) {
        }
        mainFrame.gameLogic.setPlayerTurn(false);
        mainFrame.boardManager.disableComputerButtons();
        mainFrame.boardManager.disablePlayerButtons();

        int[] shotCoordinates = getValidShotCoordinates();

        if (shotCoordinates == null) {
            mainFrame.gameLogic.startPlayerTurn();
            return;
        }

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, e -> {
            boolean hit = processComputerShot(shotCoordinates[0], shotCoordinates[1]);
            if (!hit && !mainFrame.gameLogic.isGameEnded) {
                mainFrame.gameLogic.setPlayerTurn(true);
                mainFrame.boardManager.enableComputerButtons();
            } else if (hit && mainFrame.gameLogic.isGameStarted && !mainFrame.gameLogic.isGameEnded && !mainFrame.gameLogic.isPlayerTurn) {
                startComputerTurn();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // обирає допустиму клітинку для пострілу
    private int[] getValidShotCoordinates() {
        int[] coordinates;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            coordinates = aiStrategy.chooseShot(mainFrame.boardManager.playerShipButtons);
            attempts++;
            if (coordinates == null || attempts > MAX_ATTEMPTS) {
                return null;
            }

            // Перевіряємо, чи клітинка доступна через GameLogic
            boolean isAvailable = mainFrame.boardManager.isCellAvailableForShot(coordinates[0], coordinates[1]);
            boolean isNotShot = !mainFrame.boardManager.isComputerShotAt(coordinates[0], coordinates[1]);
            String cellText = mainFrame.boardManager.playerShipButtons[coordinates[0]][coordinates[1]] != null ?
                    mainFrame.boardManager.playerShipButtons[coordinates[0]][coordinates[1]].getText() : "null";

            if (!isAvailable || !isNotShot || "•".equals(cellText)) {
                coordinates = null; // Скидаємо координати для нової спроби
                continue;
            }

            // Перевіряємо валідність клітинки лише після всіх перевірок
            if (coordinates != null && !isValidCell(coordinates[0], coordinates[1])) {
                coordinates = null; // Скидаємо, якщо клітинка невалідна
            }
        } while (coordinates == null);

        return coordinates;
    }

    // виконує постріл комп'ютера в гравця
    public boolean processComputerShot(int row, int col) {
        if (!isValidCell(row, col)) {
            return false;
        }

        if (mainFrame.gameLogic.isGameEnded) {
            if (timer != null) {
                timer.stop();
            }
            return false;
        }

        ShipButton button = mainFrame.boardManager.playerShipButtons[row][col];
        if (button == null) {
            return false;
        }

        // Додаткова перевірка перед пострілом
        if (!mainFrame.boardManager.isCellAvailableForShot(row, col) || mainFrame.boardManager.isComputerShotAt(row, col)) {
            if (!mainFrame.gameLogic.isGameEnded) {
                startComputerTurn(); // Повторна спроба лише якщо гра не закінчена
            }
            return false;
        }

        Ship ship = mainFrame.boardManager.playerShipsLocations[row][col];
        boolean hit = ship != null;
        boolean sunk = false;

        if (hit) {
            sunk = mainFrame.uiMarkingLogic.markPlayerBoardHit(row, col, ship);
        } else {
            mainFrame.uiMarkingLogic.markPlayerBoardMiss(row, col);
        }

        aiStrategy.processShotResult(new int[]{row, col}, hit, sunk);

        if (sunk && !mainFrame.gameLogic.isGameEnded) {
            mainFrame.gameLogic.checkGameEnd();
        }
        return hit;
    }

    // перевірка на коректність координат
    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }

    // Зупиняє роботу таймера
    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}

class AIStrategy {

    private static final Random random = new Random();
    private Mode currentMode = Mode.RANDOM;
    private final List<int[]> hits = new ArrayList<>();
    private final int[][] targetedArea = new int[11][11];
    private final List<int[]> availableShots = new ArrayList<>();
    private GameLogic gameLogic;
    private MainFrame mainFrame;

    public enum Mode {
        RANDOM, HUNT, TARGET
    }

    public AIStrategy(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public AIStrategy() {}

    // метод для вибору пострілу
    public int[] chooseShot(ShipButton[][] playerButtons) {
        updateAvailableShots(playerButtons);

        if (availableShots.isEmpty()) {
            return null;
        }

        int[] shot = null;
        int attempts = 0;
        final int maxAttempts = 100;

        while (shot == null && attempts < maxAttempts) {
            switch (currentMode) {
                case RANDOM:
                    shot = getRandomShot();
                    break;
                case HUNT:
                    shot = getHuntShot();
                    break;
                case TARGET:
                    shot = getTargetShot();
                    break;
                default:
                    shot = getRandomShot();
            }

            if (shot != null && (isAlreadyShot(shot[0], shot[1]) || isMissSymbol(playerButtons, shot[0], shot[1]))) {
                shot = null; // Ігноруємо, якщо клітинка вже використана або містить "•"
            }

            // Додаткова перевірка через GameLogic, якщо він доступний
            if (shot != null && gameLogic != null && mainFrame.boardManager.isComputerShotAt(shot[0], shot[1])) {
                shot = null; // Ігноруємо, якщо клітинка вже обстріляна згідно з GameLogic
            }

            attempts++;
        }

        if (shot != null && isValidCell(shot[0], shot[1]) && !isAlreadyShot(shot[0], shot[1]) && !isMissSymbol(playerButtons, shot[0], shot[1])) {
            markAsShot(shot[0], shot[1]);
            return shot;
        }
        return null;
    }

    /*
    Методи вибору пострілу:
     */
    private int[] getRandomShot() {
        if (availableShots.isEmpty()) {
            return null;
        }
        int index = random.nextInt(availableShots.size());
        return availableShots.get(index);
    }

    private int[] getHuntShot() {
        if (hits.isEmpty()) {
            currentMode = Mode.RANDOM;
            return getRandomShot();
        }

        for (int[] hit : hits) {
            int row = hit[0];
            int col = hit[1];
            int[][] directions = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Вгору, вниз, ліворуч, праворуч
            };
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (isValidCell(newRow, newCol) && !isAlreadyShot(newRow, newCol)) {
                    return new int[]{newRow, newCol};
                }
            }
        }

        currentMode = Mode.RANDOM;
        return getRandomShot();
    }

    private int[] getTargetShot() {
        if (hits.size() < 2) {
            currentMode = Mode.HUNT;
            return getHuntShot();
        }

        int[] firstHit = hits.get(0);
        int[] lastHit = hits.get(hits.size() - 1);
        boolean isHorizontal = firstHit[0] == lastHit[0];

        int row = lastHit[0];
        int col = lastHit[1];
        int step = isHorizontal ? 1 : 0;
        int dir = (lastHit[step] > firstHit[step]) ? 1 : -1;
        int newRow = row + (isHorizontal ? 0 : dir);
        int newCol = col + (isHorizontal ? dir : 0);

        if (isValidCell(newRow, newCol) && !isAlreadyShot(newRow, newCol)) {
            return new int[]{newRow, newCol};
        }

        newRow = row + (isHorizontal ? 0 : -dir);
        newCol = col + (isHorizontal ? -dir : 0);
        if (isValidCell(newRow, newCol) && !isAlreadyShot(newRow, newCol)) {
            return new int[]{newRow, newCol};
        }

        currentMode = Mode.HUNT;
        return getHuntShot();
    }

    // реакція на результат пострілу
    public void processShotResult(int[] coordinates, boolean hit, boolean sunk) {
        int row = coordinates[0];
        int col = coordinates[1];
        markAsShot(row, col);

        if (hit && !sunk) {
            hits.add(coordinates);
            if (hits.size() == 1) {
                currentMode = Mode.HUNT;
            } else if (hits.size() >= 2) {
                boolean inLine = false;
                if (hits.size() >= 2) {
                    int firstRow = hits.get(0)[0];
                    int firstCol = hits.get(0)[1];
                    boolean allSameRow = true, allSameCol = true;
                    for (int[] h : hits) {
                        if (h[0] != firstRow) allSameRow = false;
                        if (h[1] != firstCol) allSameCol = false;
                    }
                    inLine = allSameRow || allSameCol;
                }
                currentMode = inLine ? Mode.TARGET : Mode.HUNT;
            }
        } else if (sunk) {
            for (int[] h : hits) {
                int r = h[0];
                int c = h[1];
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int newRow = r + dr;
                        int newCol = c + dc;
                        if (isValidCell(newRow, newCol) && !isAlreadyShot(newRow, newCol)) {
                            markAsShot(newRow, newCol);
                        }
                    }
                }
            }
            hits.clear();
            currentMode = Mode.RANDOM;
        } else {
            if (currentMode == Mode.TARGET) {
                currentMode = Mode.HUNT;
            } else if (currentMode == Mode.HUNT && hits.isEmpty()) {
                currentMode = Mode.RANDOM;
            }
        }
    }

    // оновлює список доступних клітинок у які комп'ютер може стріляти
    private void updateAvailableShots(ShipButton[][] playerButtons) {
        availableShots.clear();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerButtons[i][j] != null && !isAlreadyShot(i, j) && !isMissSymbol(playerButtons, i, j)) {
                    if (gameLogic == null || (!mainFrame.boardManager.isComputerShotAt(i, j) && mainFrame.boardManager.isCellAvailableForShot(i, j))) {
                        availableShots.add(new int[]{i, j});
                    }
                }
            }
        }

    }

    // перевіряє чи є в клітинці символ, який позначає промах
    private boolean isMissSymbol(ShipButton[][] playerButtons, int row, int col) {
        ShipButton button = playerButtons[row][col];
        return button != null && "•".equals(button.getText());
    }

    // перевіряє чи в клітинку вже попадали
    private boolean isAlreadyShot(int row, int col) {
        return targetedArea[row][col] == 1;
    }

    // позначає клітинку як обістрялну
    private void markAsShot(int row, int col) {
        if (isValidCell(row, col)) {
            targetedArea[row][col] = 1;
            if (gameLogic != null) {
                if (row >= 1 && row <= 10 && col >= 1 && col <= 10) {
                    mainFrame.boardManager.computerTargetedArea[row][col] = 1;
                }
            }
        }
    }

    // перевірка чи належить координата ігровому полю
    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }

    // скидає стан бота перед початком нової гри
    public void reset() {
        hits.clear();
        currentMode = Mode.RANDOM;
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                targetedArea[i][j] = 0;
            }
        }
        availableShots.clear();
    }
}
