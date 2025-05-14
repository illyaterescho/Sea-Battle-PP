package org.seabattlepp.ships;

import java.util.*;
import java.util.Random;

public class ShipPlacer {
    private ShipValidator validator;
    private Random random = new Random();

    public ShipPlacer(ShipValidator validator) {
        this.validator = validator;
    }

    // Основний метод, який розміщує кораблі випадковим чином
    public List<Ship> placeShipsRandomly() {
        List<Ship> ships = createShipList(); // для створення списку кораблів для розміщення
        List<Ship> placedShips = new ArrayList<>();
        Set<String> occupiedCoordinates = new HashSet<>(); // для зайнятих координат

        for (Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                int startRow = random.nextInt(10) + 1; // рядки від 1 до 10
                int startCol = random.nextInt(10) + 1; // стовпці від 1 до 10
                boolean isHorizontal = random.nextBoolean();

                List<int[]> potentialCoordinates = generateCoordinates(ship, startRow, startCol, isHorizontal);

                // перевірка на валідність розміщення корабля
                if (validator.isValidPlacement(potentialCoordinates, occupiedCoordinates)) {
                    ship.setCoordinates(potentialCoordinates);
                    placedShips.add(ship);
                    occupiedCoordinates.addAll(coordinatesToStringSet(potentialCoordinates));
                    placed = true;
                }
            }
        }
        return placedShips;
    }

    private List<Ship> createShipList() {
        List<org.seabattlepp.ships.Ship> ships = new ArrayList<>();

        ships.add(new Ship(5));
        ships.add(new Ship(4));
        ships.add(new Ship(4));
        ships.add(new Ship(3));
        ships.add(new Ship(3));
        ships.add(new Ship(3));
        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(2));

        return ships;
    }


    // генерує координати корбля залежно від орієнтації
    private List<int[]> generateCoordinates(Ship ship, int startRow, int startCol, boolean isHorizontal) {
        List<int[]> coordinates = new ArrayList<>();
        if (isHorizontal) {
            // Якщо виходить за межі праворуч — зсуваємо вліво
            if (startCol + ship.getLength() > 11) {
                startCol = 11 - ship.getLength();
            }
            for (int i = 0; i < ship.getLength(); i++) {
                coordinates.add(new int[]{startRow, startCol + i});
            }
        } else { // Вертикальне розташування
            // Якщо виходить за межі вниз — зсуваємо вгору
            if (startRow + ship.getLength() > 11) {
                startRow = 11 - ship.getLength(); // зсув вгору якщо виходить за межі
            }
            for (int i = 0; i < ship.getLength(); i++) {
                coordinates.add(new int[]{startRow + i, startCol});
            }
        }
        return coordinates;
    }

    // Конвертує список координат у множину рядків "row,col" для перевірки зайнятості
    private Set<String> coordinatesToStringSet(List<int[]> coordinatesList) {
        Set<String> coordinateStrings = new HashSet<>();
        for (int[] coord : coordinatesList) {
            coordinateStrings.add(coord[0] + "," + coord[1]);
        }
        return coordinateStrings;
    }
}
