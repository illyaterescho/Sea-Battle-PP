package org.seabattlepp.ships;

import org.seabattlepp.logic.GameLogic;

public interface BoardManager {
    void placeShipsRandomlyOnLeftBoard();
    void placeShipsRandomlyOnRightBoard();
    void clearLeftBoardShips();
    void clearRightBoardShips();
    void resetBoards();
    void setGameLogic(GameLogic gameLogic);
    void setGameStarted(boolean started);
    boolean isGameStarted();

    void enablePlayerButtonsForPlacement();
    void enableComputerButtons();
    void disableComputerButtons();
    void enablePlayerButtons();
    void disablePlayerButtons();
}
