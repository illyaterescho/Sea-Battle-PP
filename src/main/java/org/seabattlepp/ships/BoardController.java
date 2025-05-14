package org.seabattlepp.ships;

import org.seabattlepp.gui.BoardPanel;
import org.seabattlepp.gui.ShipButton;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.util.List;
import org.seabattlepp.logic.GameLogic;


public class BoardController implements BoardManager {
    private final BoardPanel boardPanel;
    private boolean isGameStarted = false;
    private GameLogic gameLogic;

    public BoardController(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    // 🔹 Рандомне розміщення кораблів на лівій (гравець) дошці
    @Override
    public void placeShipsRandomlyOnLeftBoard() {
        ShipValidator validator = new ShipValidator();
        ShipPlacer placer = new ShipPlacer(validator);
        List<Ship> placedShips = placer.placeShipsRandomly();

        if (gameLogic != null) {
            gameLogic.setPlayerShips(placedShips);
        }

        clearLeftBoardShips();
        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                ShipButton button = boardPanel.playerShipButtons[row][col];
                if (button != null) {
                    // Візуально позначаємо корабель ⚓
                    button.setIcon(null);
                    button.setText("⚓");
                    button.setFont(new Font("Inter", Font.BOLD, 50));
                    button.setForeground(Color.BLACK);
                    button.setEnabled(true);
                    button.setBackground(Color.WHITE);
                }
            }
        }
    }

    // 🔹 Рандомне розміщення кораблів на правій (комп’ютер) дошці
    @Override
    public void placeShipsRandomlyOnRightBoard() {
        ShipValidator validator = new ShipValidator();
        ShipPlacer placer = new ShipPlacer(validator);
        List<Ship> placedShips = placer.placeShipsRandomly();

        if (gameLogic != null) {
            gameLogic.setComputerShips(placedShips);
        }

        clearRightBoardShips(); // очищуємо перед розміщенням

        for (Ship ship : placedShips) {
            for (int[] coord : ship.getCoordinates()) {
                int row = coord[0];
                int col = coord[1];
                ShipButton button = boardPanel.computerShipButtons[row][col];
                if (button != null) {
                    // Візуально позначаємо корабель ⚓
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                    button.setFont(new Font("Inter", Font.BOLD, 25));
                    button.setIcon(null);
                    button.setOpaque(false);
                    button.setEnabled(true);
                }
            }
        }
    }

    // 🔄 Очистити ліву дошку гравця
    @Override
    public void clearLeftBoardShips() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = boardPanel.playerShipButtons[i][j];
                if (button != null) {
                    button.setIcon(null);
                    button.setText("");
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setUI(new BasicButtonUI());
                }
            }
        }
    }

    // 🔄 Очистити праву дошку комп’ютера
    @Override
    public void clearRightBoardShips() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = boardPanel.computerShipButtons[i][j];
                if (button != null) {
                    button.setIcon(null);
                    button.setText("");
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                    button.setOpaque(false);
                    button.setUI(new BasicButtonUI());
                }
            }
        }
    }

    // 🔁 Скинути гру до початкового стану
    @Override
    public void resetBoards() {
        clearLeftBoardShips();
        clearRightBoardShips();
        isGameStarted = false;

        gameLogic.resetGame();

        enablePlayerButtonsForPlacement();
        disableComputerButtons();
    }

    // 🔘 Позначити, що гра почалась (або закінчилась)
    @Override
    public void setGameStarted(boolean started) {
        isGameStarted = started;

        // Якщо гра щойно почалась і не хід гравця — починає комп’ютер
        if (started && !gameLogic.isPlayerTurn()) {
             gameLogic.startComputerTurn();
         }
    }

    @Override
    public boolean isGameStarted() {
        return isGameStarted;
    }

    // 🔓 Активувати всі клітинки гравця (для ручного розміщення)
    @Override
    public void enablePlayerButtonsForPlacement() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = boardPanel.playerShipButtons[i][j];
                if (button != null) {
                    button.setEnabled(true);
                }
            }
        }
    }

    // 🔓 Дозволити гравцю стріляти по комп’ютеру
    @Override
    public void enableComputerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = boardPanel.computerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED) {
                    button.setEnabled(true);
                }
            }
        }
    }

    // 🔒 Заборонити стріляти по комп’ютеру
    @Override
    public void disableComputerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = boardPanel.computerShipButtons[i][j];
                if (button != null) {
                    button.setEnabled(false);
                }
            }
        }
    }

    // 🔓 Дозволити взаємодію з клітинками гравця (після скидання)
    @Override
    public void enablePlayerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = boardPanel.playerShipButtons[i][j];
                if (button != null && button.getIcon() == null && button.getBackground() != Color.RED) {
                    button.setEnabled(true);
                }
            }
        }
    }

    // 🔒 Заборонити взаємодію з клітинками гравця
    @Override
    public void disablePlayerButtons() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                ShipButton button = boardPanel.playerShipButtons[i][j];
                if (button != null) {
                    button.setEnabled(false);
                }
            }
        }
    }
}
