package org.seabattlepp.ships;

import java.util.List;

public class Ship {
    private String type;
    private int length;
    private List<int[]> coordinates; // координати клітинок корабля

    public Ship(String type, int length) {
        this.type = type;
        this.length = length;
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
}