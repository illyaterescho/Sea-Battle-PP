package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.logic.GameLogic;

public class AIPlayer {

    private final AIStrategy aiStrategy;

    public AIPlayer(GameLogic gameLogic) {
        this.aiStrategy = new AIStrategy();
    }

    public int[] makeTurn(ShipButton[][] playerButtons) {
        return aiStrategy.makeShot(playerButtons);
    }

    public void processShotResult(int[] coordinates, boolean hit, boolean sunk) {
        aiStrategy.processShotResult(coordinates, hit, sunk);
    }

    public void resetAI() {
        aiStrategy.reset();
    }
}