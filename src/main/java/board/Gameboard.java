package board;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Random;
import java.io.FileWriter;
import java.util.Scanner;

import logger.SimLogger;
import main.Macros;
import model.Model;

/**
 * Class taking care of game board (board size, landscape, generator)
 */
public class Gameboard {

    public static int gameboardWidth; // width of game board (number of tiles)
    public static int gameboardHeight; // height of game board (number of tiles)
    public static int numberOfTiles;
    public static short maxForestRecursion = 12;
    public static short maxWaterRecursion = 12;
    public static short ForestQuantity = 6;
    public static short WaterQuantity = 3;

    public TileStructure[] GameboardArray; // array of game board tiles

    // singleton
    private static volatile Gameboard instance = null;
    public static Gameboard getInstance() {
        if (instance == null) {
            synchronized (Gameboard.class) {
                if (instance == null) {
                    instance = new Gameboard();
                }
            }
        }
        return instance;
    }

    /** Set default board size */
    public Gameboard() {
        gameboardWidth = 30;
        gameboardHeight = 30;
        numberOfTiles=gameboardWidth*gameboardHeight;
        SimLogger.LOGGER.info("Gameboard initialized");
    }


    /** Load game board from given file */
    public void LoadBoard(String file_dest) throws FileNotFoundException {
        SimLogger.LOGGER.info("loading saved gameboard from file");

        File f = new File(file_dest);
        if(!f.exists() || f.isDirectory()) {
            SimLogger.LOGGER.severe("save file does not exist");
            throw new FileNotFoundException();
        }

        try {
            Scanner scanner = new Scanner(new File(file_dest));
            int number;
            number = scanner.nextInt();
            gameboardWidth = number;
            number = scanner.nextInt();
            gameboardHeight = number;
            numberOfTiles = gameboardWidth * gameboardHeight;
            GameboardArray = new TileStructure[numberOfTiles];

            int number1, number2, number3, number4;
            for (int i = 0; i < numberOfTiles; i++) {
                number1 = scanner.nextInt();
                scanner.next();
                number2 = scanner.nextInt();
                scanner.next();
                number3 = scanner.nextInt();
                scanner.next();
                number4 = scanner.nextInt();

                GameboardArray[number1] = new TileStructure((short) number2, (short) number3, (short) number4);
            }
        } catch (IOException e) {
            SimLogger.LOGGER.severe("loading gameboard from file failed");
            throw new RuntimeException(e);
        }
        SimLogger.LOGGER.info("gameboard loaded");
    }

    /** Save current board to 'saves/board.save' file */
    public void SaveBoard() {
        SimLogger.LOGGER.info("saving gameboard to file");
        String file_dest = "saves/board.save";
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(file_dest);
            String str = gameboardWidth + " " + gameboardHeight + "\n";
            myWriter.write(str);
            for (int i = 0; i < numberOfTiles; i++) {
                str = i + " : " + GameboardArray[i].getLandType() + " | " + GameboardArray[i].getFood() + " | " + GameboardArray[i].getEntities() + "\n";
                myWriter.write(str);
            }
            myWriter.close();
        } catch (IOException e) {
            SimLogger.LOGGER.severe("saving gameboard to file failed");
            throw new RuntimeException(e);
        }
        SimLogger.LOGGER.info("gameboard saved to file");
    }

    /** Generate new map with food and entities on it */
    public void GenerateBoard() throws InvalidParameterException {

        if (gameboardHeight < 1 || gameboardWidth < 1) {
            SimLogger.LOGGER.severe("invalid board size ("+gameboardWidth+"x"+gameboardHeight+") , cannot generate");
            throw new InvalidParameterException();
        }

        SimLogger.LOGGER.info("generating gameboard");
        // add plains
        GameboardArray = new TileStructure[numberOfTiles];
        for (int i = 0; i < numberOfTiles; i++) {
            GameboardArray[i] = new TileStructure(Macros.PLAINS, Macros.NONE, Macros.NONE);
        }
        // add water tiles
        int waterGenTiles = WaterQuantity + numberOfTiles / 250;
        Random rand = new Random();
        for (int i = 0; i < waterGenTiles; i++) {
            int tileNum = rand.nextInt(numberOfTiles);
            GameboardArray[tileNum].setLandType(Macros.WATER);
            SpreadTile(tileNum,Macros.WATER,(short )0, maxWaterRecursion);
        }
        // add forest tiles
        int forestGenTiles = ForestQuantity + numberOfTiles / 250;
        for (int i = 0; i < forestGenTiles; i++) {
            int tileNum = rand.nextInt(numberOfTiles);
            if (GameboardArray[tileNum].getLandType() == Macros.PLAINS) {
                GameboardArray[tileNum].setLandType(Macros.FOREST);
                SpreadTile(tileNum,Macros.FOREST,(short )0, maxForestRecursion);
            }
        }

        // add food
        Model model = Model.getInstance();
        model.GenerateFood(Macros.FRUIT,true);
        model.GenerateFood(Macros.MUSHROOMS,true);

        // generate entities
        model.GenerateEntity(Macros.FISH,true);
        model.GenerateEntity(Macros.CHICKEN,true);
        model.GenerateEntity(Macros.DEER,true);
        model.GenerateEntity(Macros.WOLF,true);
        model.GenerateEntity(Macros.EAGLE,true);

        SimLogger.LOGGER.info("gameboard generated");
    }

    /**
     * Check if tile number is valid
     * @param tileNumber number of investigated tile
     */
    private void TileNumberCheck(int tileNumber) throws IllegalArgumentException {
        if (tileNumber < 0 || tileNumber >= Gameboard.numberOfTiles) {
            SimLogger.LOGGER.severe("tile number " + tileNumber + " out of range");
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check if land type is valid
     * @param landType input land type
     */
    private void LandTypeCheck(short landType) throws IllegalArgumentException {
        if (landType != Macros.PLAINS && landType != Macros.FOREST && landType != Macros.WATER) {
            SimLogger.LOGGER.severe("invalid land type "+landType);
            throw new IllegalArgumentException();
        }
    }


    /**
     * recursive generator function to spread generated land type from selected tile to its neighbours
     *
     * @param tileNum number of origin tile
     * @param LandType land type to spread
     * @param recursion current recursion
     * @param maxRecursion maximal recursion
     */
    public void SpreadTile(int tileNum, short LandType, short recursion, short maxRecursion) throws IllegalArgumentException {
        TileNumberCheck(tileNum);
        LandTypeCheck(LandType);

        if (maxRecursion != 0) {
            int nextTile;
            // left
            nextTile = tileNum - 1;
            if (nextTile >= 0 && nextTile % gameboardWidth != gameboardWidth - 1) { // left
                SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
            }
            // right
            nextTile = tileNum + 1;
            if (nextTile < numberOfTiles && nextTile % gameboardWidth != 0) { // right
                SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
            }
            // odd rows
            if ((tileNum / gameboardWidth) % 2 == 0) {
                // left up
                nextTile = tileNum - gameboardWidth - 1;
                if (nextTile > 0 && (nextTile / gameboardWidth) % 2 == 1) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
                // right up
                nextTile = tileNum - gameboardWidth;
                if (nextTile > 0 && (nextTile / gameboardWidth) % 2 == 1) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
                // left down
                nextTile = tileNum + gameboardWidth - 1;
                if (nextTile < numberOfTiles && (nextTile / gameboardWidth) % 2 == 1) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
                // right down
                nextTile = tileNum + gameboardWidth;
                if (nextTile < numberOfTiles && (nextTile / gameboardWidth) % 2 == 1) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
            }
            // even
            else {
                // left up
                nextTile = tileNum - gameboardWidth;
                if (nextTile > 0 && (nextTile / gameboardWidth) % 2 == 0) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
                // right up
                nextTile = tileNum - gameboardWidth + 1;
                if (nextTile > 0 && (nextTile / gameboardWidth) % 2 == 0) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
                // left down
                nextTile = tileNum + gameboardWidth;
                if (nextTile < numberOfTiles && (nextTile / gameboardWidth) % 2 == 0) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
                // right down
                nextTile = tileNum + gameboardWidth + 1;
                if (nextTile < numberOfTiles && (nextTile / gameboardWidth) % 2 == 0) {
                    SetTileAndStartRecursion(nextTile, LandType, recursion, maxRecursion);
                }
            }
        }
    }

    /**
     * Function deciding if the spreading continues or not and storing new land type of tile to GameboardArray
     * @param nextTile tile to set new land type
     * @param LandType new land type
     * @param recursion current recursion
     * @param maxRecursion maximal recursion
     */
    private void SetTileAndStartRecursion(int nextTile, short LandType, short recursion, short maxRecursion) throws IllegalArgumentException {
        TileNumberCheck(nextTile);
        LandTypeCheck(LandType);

        double RecursionProbability = 1.0 - (1.0*recursion)/maxRecursion; // probability connected with number of recursion
        Random rand = new Random();
        double probability = (1.0*rand.nextInt(100))/100.0; // random number
        probability = (3*RecursionProbability + probability)/4;
        recursion++;
        if (GameboardArray[nextTile].getLandType() == Macros.PLAINS) {
            GameboardArray[nextTile].setLandType(LandType);
        }
        if (GameboardArray[nextTile].getLandType() == LandType) {
            if (recursion < maxRecursion) {
                if (probability > 0.8) {
                    SpreadTile(nextTile, LandType, recursion, maxRecursion);
                }
            }
        }
    }
}

