package org.seabattlepp.ships;

import java.util.List;

public class Ship {
    private String type;
    private int length;
    private List<int[]> coordinates; // координати клітинок корабля
    private int hitsTaken; // кількість влучань отриманих кораблем

    public Ship(String type, int length) {
        this.type = type;
        this.length = length;
        this.hitsTaken = 0; // ініціалізуємо кількість влучань нулем при створенні корабля
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public List<int[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<int[]> coordinates) {
        this.coordinates = coordinates;
    }

    public int getHitsTaken() {
        return hitsTaken;
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