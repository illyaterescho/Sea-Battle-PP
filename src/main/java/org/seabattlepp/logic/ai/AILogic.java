package org.seabattlepp.logic.ai;

import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.logic.GameLogic;
import org.seabattlepp.ships.Ship;
import org.seabattlepp.ships.BoardController;


import javax.swing.*;

public class AILogic {

    private AIPlayer aiPlayer;
    private final GameLogic gameLogic; // Зв'язок з GameLogic
    private final BoardController boardController; // ✅ Замість прямого доступу до BoardPanel
    private final ShipButton[][] playerShipButtons;

    public AILogic(GameLogic gameLogic, BoardController boardController, ShipButton[][] playerShipButtons) {
        this.gameLogic = gameLogic;
        this.boardController = boardController;
        this.playerShipButtons = playerShipButtons;
        this.aiPlayer = new AIPlayer();
    }

    public void resetAI() {
        aiPlayer.resetAI();
    }

    public void startComputerTurn() {
        if (!boardController.isGameStarted()) {
            return;
        }
        gameLogic.setPlayerTurn(false);
        boardController.disableComputerButtons();
        boardController.disablePlayerButtons();

        int[] shotCoordinates = aiPlayer.makeTurn(playerShipButtons);

        if (shotCoordinates == null) {
            gameLogic.startPlayerTurn();
            return;
        }


        Timer timer = new Timer(700, e -> {
            boolean hit = processComputerShot(shotCoordinates[0], shotCoordinates[1]);
            if (!hit) {
                gameLogic.startPlayerTurn();
            } else if (boardController.isGameStarted()) {
                startComputerTurn();
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
