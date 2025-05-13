package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;

public class AIPlayer {

    private AIStrategy strategy;

    public AIPlayer() {
        this.strategy = new AIStrategy();
    }

    public int[] makeTurn(ShipButton[][] playerButtons) {
        int[] shot = strategy.makeShot(playerButtons);
        return shot;
    }

    public void processShotResult(int[] shot, boolean hit, boolean sunk) {
        strategy.processShotResult(shot, hit, sunk);
    }

    public void resetAI() {
        strategy.resetAI();
    }
}