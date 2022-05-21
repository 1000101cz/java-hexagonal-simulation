package board;

import logger.SimLogger;
import main.Macros;
import model.Model;

import java.security.InvalidParameterException;

public class GameboardTest {
    private static final Gameboard toTest = Gameboard.getInstance();
    private static final Model model = Model.getInstance();

    private static volatile GameboardTest instance = null;
    public static GameboardTest getInstance() {
        if (instance == null) {
            synchronized (GameboardTest.class) {
                if (instance == null) {
                    instance = new GameboardTest();
                }
            }
        }
        return instance;
    }


    public void prepare() {
        toTest.GenerateBoard();
    }

    public void reset() {
        model.EntityArrayLength = 0;
        model.EntityArray = null;
        toTest.GameboardArray = null;
        Gameboard.gameboardWidth = 30;
        Gameboard.gameboardHeight = 30;
        Gameboard.numberOfTiles = 30*30;
    }

    public void testInvalidLandType() {
        try {
            toTest.SpreadTile(12, Macros.INVALID, (short) 0, (short) 1);
            SimLogger.LOGGER.severe("gameboard spread land type unittest FAILED");
        } catch (IllegalArgumentException e) {
            SimLogger.LOGGER.info("gameboard spread land type unittest SUCCESSFUL");
        }
    }

    public void testInvalidTileNumber() {
        try {
            toTest.SpreadTile(-1, Macros.WATER, (short) 0, (short) 1);
            SimLogger.LOGGER.severe("gameboard spread tile num unittest FAILED");
        } catch (IllegalArgumentException e) {
            SimLogger.LOGGER.info("gameboard spread tile num unittest SUCCESSFUL");
        }
    }

    public void testInvalidBoardSize() {
        try {
            Gameboard.gameboardWidth = 0;
            toTest.GenerateBoard();
            SimLogger.LOGGER.severe("gameboard invalid size unittest FAILED");
        } catch (InvalidParameterException e) {
            SimLogger.LOGGER.info("gameboard invalid size unittest SUCCESSFUL");
        }
    }
}
