package org.seabattlepp.ships;

import java.util.List;
import java.util.Set;

public class ShipValidator {

    // Перевіряє, чи правильне розміщення корабля:
    // 1) не виходить за межі поля
    // 2) не перетинається з іншими
    // 3) не прилягає до інших (включно з діагоналлю)
    public boolean isValidPlacement(List<int[]> potentialCoordinates, Set<String> occupiedCoordinates) {
        for (int[] coord : potentialCoordinates) {
            int row = coord[0];
            int col = coord[1];

            // Перевірка: чи координата в межах дошки 1..10
            if (row < 1 || row > 10 || col < 1 || col > 10) {
                return false;
            }

            // Перевірка: чи ця координата вже не зайнята
            if (isCoordinateOccupied(row, col, occupiedCoordinates)) { // перевірка накладання один на одного бутєрбродом
                return false;
            }

            // 🔸 Перевірка, чи навколо немає інших кораблів
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue; // пропускаємо саму клітинку
                    if (isCoordinateOccupied(row + dr, col + dc, occupiedCoordinates)) {
                        return false; // якщо сусідня клітинка зайнята - не канон
                    }
                }
            }
        }
        return true;
    }

    // Перевірка, чи координата зайнята (використовується множина у форматі "row,col")
    private boolean isCoordinateOccupied(int row, int col, Set<String> occupiedCoordinates) {
        return occupiedCoordinates.contains(row + "," + col);
    }
}
