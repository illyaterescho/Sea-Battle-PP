package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.logic.GameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIStrategy {

    private static final Random random = new Random();
    private Mode currentMode = Mode.RANDOM;
    private List<int[]> hits = new ArrayList<>(); // Список влучань для поточного корабля
    private int[][] targetedArea = new int[11][11]; // Відстеження пострілів (0 - не стріляно, 1 - стріляно)
    private List<int[]> availableShots = new ArrayList<>(); // Доступні клітинки для пострілу
    private GameLogic gameLogic; // Додаємо посилання на GameLogic для синхронізації

    public enum Mode {
        RANDOM, HUNT, TARGET
    }

    // Оновлений конструктор для передачі GameLogic
    public AIStrategy(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public AIStrategy() {
        this(null); // Конструктор без параметрів для сумісності
    }

    public int[] makeShot(ShipButton[][] playerButtons) {
        updateAvailableShots(playerButtons);

        if (availableShots.isEmpty()) {
            System.out.println("No available shots for computer!");
            return null;
        }

        int[] shot = null;
        int attempts = 0;
        final int maxAttempts = 100; // Захист від нескінченного циклу

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
            if (shot != null && gameLogic != null && gameLogic.isComputerShotAt(shot[0], shot[1])) {
                shot = null; // Ігноруємо, якщо клітинка вже обстріляна згідно з GameLogic
            }

            attempts++;
        }

        if (shot != null && isValidCell(shot[0], shot[1]) && !isAlreadyShot(shot[0], shot[1]) && !isMissSymbol(playerButtons, shot[0], shot[1])) {
            markAsShot(shot[0], shot[1]);
            System.out.println("AI selected shot: row=" + shot[0] + ", col=" + shot[1]);
            return shot;
        }
        System.out.println("AI could not find a valid shot after " + maxAttempts + " attempts");
        return null;
    }

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
            // Після потоплення корабля виключаємо сусідні клітинки
            markSurroundingCellsAsShot();
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

    private void markSurroundingCellsAsShot() {
        for (int[] hit : hits) {
            int row = hit[0];
            int col = hit[1];
            // Перевіряємо всі сусідні клітинки (включаючи діагоналі)
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int newRow = row + dr;
                    int newCol = col + dc;
                    if (isValidCell(newRow, newCol) && !isAlreadyShot(newRow, newCol)) {
                        markAsShot(newRow, newCol);
                        System.out.println("Marked surrounding cell as shot: row=" + newRow + ", col=" + newCol);
                    }
                }
            }
        }
    }

    private void updateAvailableShots(ShipButton[][] playerButtons) {
        availableShots.clear();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerButtons[i][j] != null && !isAlreadyShot(i, j) && !isMissSymbol(playerButtons, i, j)) {
                    // Додаткова перевірка через GameLogic, якщо він доступний
                    if (gameLogic == null || (!gameLogic.isComputerShotAt(i, j) && gameLogic.isCellAvailableForShot(i, j))) {
                        availableShots.add(new int[]{i, j});
                    }
                }
            }
        }
        if (availableShots.isEmpty()) {
            System.out.println("No available shots for computer after filtering!");
        }
    }

    private boolean isMissSymbol(ShipButton[][] playerButtons, int row, int col) {
        ShipButton button = playerButtons[row][col];
        return button != null && "•".equals(button.getText());
    }

    private boolean isAlreadyShot(int row, int col) {
        return targetedArea[row][col] == 1;
    }

    private void markAsShot(int row, int col) {
        if (isValidCell(row, col)) {
            targetedArea[row][col] = 1;
            // Синхронізуємо з GameLogic, якщо він доступний
            if (gameLogic != null) {
                gameLogic.markComputerShot(row, col);
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }

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
