package org.seabattlepp.ships;

import java.util.List;

public class Ship {
    private int length;
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

    // метод для фіксації влучання в корабель
    public void takeHit() {
        this.hitsTaken++;
    }

    // метод для перевірк, чи потоплено корабель
    public boolean isSunk() {
        return hitsTaken >= length;
    }
}
