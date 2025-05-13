package org.seabattlepp.logic;

import org.seabattlepp.gui.BoardPanel;
import org.seabattlepp.gui.ShipButton;
import org.seabattlepp.ships.Ship;

public class PlayerLogic {

    private final GameLogic gameLogic; // Зв'язок з GameLogic
    private final BoardPanel boardPanel;
    private final ShipButton[][] computerShipButtons;

    public PlayerLogic(GameLogic gameLogic, BoardPanel boardPanel, ShipButton[][] computerShipButtons) {
        this.gameLogic = gameLogic;
        this.boardPanel = boardPanel;
        this.computerShipButtons = computerShipButtons;
    }

    public void startPlayerTurn() {
        gameLogic.setPlayerTurn(true);
        boardPanel.enablePlayerButtons();
    }

    public void processShot(int row, int col) {
        if (!gameLogic.isPlayerTurn()) {
            return;
        }
        Ship ship = gameLogic.getShipAt(row, col);
        boolean hit = ship != null;
        boolean sunk = false;
        if (hit) {
            sunk = gameLogic.markHit(row, col, ship);
            if (sunk) {
                gameLogic.checkGameEnd();
            }
        } else {
            gameLogic.markMiss(row, col);
            gameLogic.startComputerTurn(); // Викликається тільки після промаху гравця
        }
    }
}