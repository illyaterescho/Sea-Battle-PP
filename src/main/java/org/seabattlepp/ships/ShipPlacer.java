/*
package org.seabattlepp.ships;

import java.util.*;
import java.util.List;
import java.util.Random;

public class ShipPlacer {
    private ShipValidator validator;
    private Random random = new Random();

    public ShipPlacer(ShipValidator validator) {
        this.validator = validator;
    }

    public List<org.seabattlepp.ships.Ship> placeShipsRandomly() {
        List<org.seabattlepp.ships.Ship> ships = createShipList(); // для створення списку кораблів для розміщення
        List<org.seabattlepp.ships.Ship> placedShips = new ArrayList<>();
        Set<String> occupiedCoordinates = new HashSet<>(); // для зайнятих координат

        for (org.seabattlepp.ships.Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                int startRow = random.nextInt(10) + 1; // рядки від 1 до 10
                int startCol = random.nextInt(10) + 1; // стовпці від 1 до 10
                boolean isHorizontal = random.nextBoolean(); // рандом

                List<int[]> potentialCoordinates = generateCoordinates(ship, startRow, startCol, isHorizontal);

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

    private List<org.seabattlepp.ships.Ship> createShipList() {
        List<org.seabattlepp.ships.Ship> ships = new ArrayList<>();
        ships.add(new org.seabattlepp.ships.Ship("Авіаносець", 5));
        ships.add(new org.seabattlepp.ships.Ship("Броненосець", 4));
        ships.add(new org.seabattlepp.ships.Ship("Броненосець", 4));
        ships.add(new org.seabattlepp.ships.Ship("Крейсер", 3));
        ships.add(new org.seabattlepp.ships.Ship("Крейсер", 3));
        ships.add(new org.seabattlepp.ships.Ship("Крейсер", 3));
        ships.add(new org.seabattlepp.ships.Ship("Руйнівник", 2));
        ships.add(new org.seabattlepp.ships.Ship("Руйнівник", 2));
        ships.add(new org.seabattlepp.ships.Ship("Руйнівник", 2));
        ships.add(new org.seabattlepp.ships.Ship("Руйнівник", 2));
        return ships;
    }


    private List<int[]> generateCoordinates(org.seabattlepp.ships.Ship ship, int startRow, int startCol, boolean isHorizontal) {
        List<int[]> coordinates = new ArrayList<>();
        if (isHorizontal) {
            if (startCol + ship.getLength() > 11) { // перевірка виходу за межі дошки
                startCol = 11 - ship.getLength(); // зсув вліво якщо виходить за межі
            }
            for (int i = 0; i < ship.getLength(); i++) {
                coordinates.add(new int[]{startRow, startCol + i});
            }
        } else { // Вертикальне розташування
            if (startRow + ship.getLength() > 11) { // перевірка чи виходить корабель за межі дошки
                startRow = 11 - ship.getLength(); // зсув вгору якщо виходить за межі
            }
            for (int i = 0; i < ship.getLength(); i++) {
                coordinates.add(new int[]{startRow + i, startCol});
            }
        }
        return coordinates;
    }

    private Set<String> coordinatesToStringSet(List<int[]> coordinatesList) {
        Set<String> coordinateStrings = new HashSet<>();
        for (int[] coord : coordinatesList) {
            coordinateStrings.add(coord[0] + "," + coord[1]);
        }
        return coordinateStrings;
    }
}*/
