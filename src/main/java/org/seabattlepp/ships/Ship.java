package org.seabattlepp.ships;

import java.util.List;

public class Ship {
    private final int length;
    private List<int[]> coordinates;
    private int hitsTaken;

    public Ship(int length) {
        this.length = length;
        this.hitsTaken = 0;
    }

    public int getLength() {
        return length;
    }

    // Повертає список координат, які займає корабель
    public List<int[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<int[]> coordinates) {
        this.coordinates = coordinates;
    }

    // Метод, який викликається при влучанні в корабель
    public void takeHit() {
        this.hitsTaken++;
    }

    public boolean isSunk() {
        return hitsTaken >= length;
    }
}
