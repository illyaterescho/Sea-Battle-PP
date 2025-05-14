package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.logic.GameLogic;
import org.seabattlepp.ships.Ship;

import javax.swing.*;

public class AILogic {

    private AIPlayer aiPlayer;
    private final GameLogic gameLogic; // Зв'язок з GameLogic
    private final ShipButton[][] playerShipButtons;

    public AILogic(
            GameLogic gameLogic,
            ShipButton[][] playerShipButtons
    ) {
        this.gameLogic = gameLogic;
        this.playerShipButtons = playerShipButtons;
        this.aiPlayer = new AIPlayer();
    }

    public void resetAI() {
        aiPlayer.resetAI();
    }

    public void startComputerTurn() {
        if (!gameLogic.isGameStarted()) {
            return;
        }
        gameLogic.setPlayerTurn(false);
        gameLogic.disableComputerButtons();
        gameLogic.disablePlayerButtons();

        int[] shotCoordinates = aiPlayer.makeTurn(playerShipButtons);

        if (shotCoordinates == null) {
            gameLogic.startPlayerTurn();
            return;
        }



        Timer timer = new Timer(700, e -> {
            boolean hit = processComputerShot(shotCoordinates[0], shotCoordinates[1]);
            if (!hit) {
                // повертаємо хід гравцю
                gameLogic.setPlayerTurn(true);
                gameLogic.enableComputerButtons(); // дозволяємо натискати
            } else if (gameLogic.isGameStarted()) {
                startComputerTurn(); // якщо влучив — продовжує
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public boolean processComputerShot(int row, int col) {
        ShipButton button = playerShipButtons[row][col];
        if (button == null) {
            return false;
        }
        Ship ship = gameLogic.getPlayerShipAt(row, col);
        boolean hit = ship != null;
        boolean sunk = false;

        if (hit) {
            sunk = gameLogic.markHitPlayerBoard(row, col, ship);
        } else {
            gameLogic.markMissPlayerBoard(row, col);
        }

        aiPlayer.processShotResult(new int[]{row, col}, hit, sunk);


        if (sunk) {
            gameLogic.checkGameEnd();
        }
        return hit;
    }
}
// тут баги