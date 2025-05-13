/*
package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIStrategy {

    private final Random random = new Random();
    private final List<int[]> previousShots = new ArrayList<>();
    private boolean lastShotHit = false;
    private int[] lastHitCoords = null;
    private AIShotType currentShotType = AIShotType.RANDOM;

    private List<int[]> targetingDirections; // напрямки для перевірки після влучання
    private int currentDirectionIndex; // індекс поточного напрямку
    private List<int[]> targetedShipSegments; // список клітинок корабля в які вже влучив коли цілимось
    private int consecutiveHitsInDirection = 0; // лічильник послідовних влучань в одному напрямку

    public AIStrategy() {
        resetAI();
    }

    public int[] makeShot(ShipButton[][] playerButtons) {
        int[] shot = null;
        switch (currentShotType) {
            case RANDOM:
                shot = getRandomShot(playerButtons);
                break;
            case TARGETED:
                shot = getTargetedShot(playerButtons);
                if (shot == null) {
                    currentShotType = AIShotType.RANDOM;
                    shot = getRandomShot(playerButtons);
                }
                break;
        }
        if (shot != null && isValidCell(shot[0], shot[1])) {
            previousShots.add(shot);
            return shot;
        } else {
            return getRandomShot(playerButtons); // якщо висріл по кораблю не вдався переходимо до рандомного методу
        }
    }


    public int[] getRandomShot(ShipButton[][] playerButtons) {
        List<int[]> availableShots = getAvailableShots(playerButtons);
        if (availableShots.isEmpty()) {
            return null;
        }
        if (!availableShots.isEmpty()) { // Змінено умову, щоб уникнути помилки IndexOutOfBoundsException
            int shotIndex = random.nextInt(availableShots.size());
            int[] shot = availableShots.get(shotIndex);
            return shot;
        } else {
            return null;
        }
    }


    private int[] getTargetedShot(ShipButton[][] playerButtons) {
        if (lastHitCoords != null) {
            // якщо вже є напрямки для прицілювання використовуємо їх
            if (targetingDirections != null && !targetingDirections.isEmpty()) {
                int[] targetDirection = targetingDirections.get(currentDirectionIndex);
                int[] targetShot;

                if (targetedShipSegments != null && !targetedShipSegments.isEmpty()) { // стріляємо від останнього влучання в напрямку
                    int[] lastSegment = targetedShipSegments.get(targetedShipSegments.size() - 1);
                    targetShot = new int[]{lastSegment[0] + targetDirection[0], lastSegment[1] + targetDirection[1]};
                } else {
                    targetShot = new int[]{lastHitCoords[0] + targetDirection[0], lastHitCoords[1] + targetDirection[1]};
                }


                if (isCellValidTarget(targetShot[0], targetShot[1], playerButtons)) {
                    return targetShot;
                } else {
                    // якщо клітинка в поточному напрямку не валідна переходимо до наступного напрямку
                    consecutiveHitsInDirection = 0; // Reset consecutive hits when changing direction
                    currentDirectionIndex++;
                    if (currentDirectionIndex < targetingDirections.size()) {
                        return getTargetedShot(playerButtons);
                    } else {
                        return null; // якшо всі напрямки вичерпано повертаємо null і переходимо до рандомного пострілу
                    }
                }
            }
        }
        return null;
    }


    private boolean isCellValidTarget(int row, int col, ShipButton[][] playerButtons) {
        return isValidCell(row, col) && playerButtons[row][col].isEnabled(); // спрощена перевірка, isAlreadyShot непотрібна
    }


    private List<int[]> getAvailableShots(ShipButton[][] playerButtons) {
        List<int[]> availableShots = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (playerButtons[i][j] != null && playerButtons[i][j].isEnabled() && playerButtons[i][j].getIcon() == null && !isAlreadyShot(i, j)) { // **Додано isAlreadyShot тут для точності**
                    availableShots.add(new int[]{i, j});
                }
            }
        }
        if (availableShots.isEmpty()) {
        }
        return availableShots;
    }


    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= 10 && col >= 1 && col <= 10;
    }

    private boolean isAlreadyShot(int row, int col) {
        for (int[] shot : previousShots) {
            if (shot[0] == row && shot[1] == col) {
                return true;
            }
        }
        return false;
    }

    public void processShotResult(int[] shotCoords, boolean hit, boolean sunk) {
        lastShotHit = hit;
        if (hit) {
            consecutiveHitsInDirection++; // збільшуємо лічильник послідовних влучань
            lastHitCoords = shotCoords;
            if (currentShotType == AIShotType.RANDOM) {
                currentShotType = AIShotType.TARGETED;
                // ініціалізація напрямків при першому влучанні
                targetingDirections = new ArrayList<>(List.of(
                        new int[]{-1, 0}, // вгору
                        new int[]{0, 1},  // вправо
                        new int[]{1, 0},  // вниз
                        new int[]{0, -1}  // вліво
                ));
                currentDirectionIndex = 0;
                targetedShipSegments = new ArrayList<>();
                targetedShipSegments.add(shotCoords); // додаємо перше влучання до списку кораблів
            } else if (currentShotType == AIShotType.TARGETED) {
                targetedShipSegments.add(shotCoords); // додаємо кожне наступне влучання
                // якщо влучили, залишаємось в поточному напрямку, не переходимо до наступного напрямку поки не промахнемось
                currentDirectionIndex = 0; // залишаємо індекс напрямку на 0, щоб продовжити в поточному напрямку


                if (consecutiveHitsInDirection >= 3 && targetingDirections.size() >= 2) {
                    currentDirectionIndex = 2; // переключаємось на протилежний напрямок (вниз або вліво, в залежності від початкового)
                }
            }
        } else {
            consecutiveHitsInDirection = 0; // скидаємо лічильник послідовних влучань при промаху
            if (currentShotType == AIShotType.TARGETED) {
                currentDirectionIndex++; // перехід до наступного напрямку при промаху в TARGETED режимі
                if (currentDirectionIndex >= targetingDirections.size()) {
                    currentShotType = AIShotType.RANDOM; // якщо всі напрямки вичерпано повертаємось до рандомного пострілу
                    targetingDirections = null; // очищуємо напрямки
                    currentDirectionIndex = 0;
                    lastHitCoords = null; // скидаємо останнє влучання бо не змогли знайти корабель
                    targetedShipSegments = null;
                    consecutiveHitsInDirection = 0;
                } else {
                    lastHitCoords = targetedShipSegments.get(0); // як останнє влучання використовуємо початковий постріл
                }
            }
        }

        if (sunk) {
            lastHitCoords = null; // забуваємо останнє влучання бо корабель потоплено
            currentShotType = AIShotType.RANDOM; // повернення до випадкового режиму після потоплення
            targetingDirections = null; // очищуємо напрямки
            currentDirectionIndex = 0;
            targetedShipSegments = null;
            consecutiveHitsInDirection = 0;
        }
    }

    private enum AIShotType {
        RANDOM,
        TARGETED
    }

    public void resetAI() {
        previousShots.clear();
        lastShotHit = false;
        lastHitCoords = null;
        currentShotType = AIShotType.RANDOM;
        targetingDirections = null;
        currentDirectionIndex = 0;
        targetedShipSegments = null;
        consecutiveHitsInDirection = 0; 
    }
}*/
