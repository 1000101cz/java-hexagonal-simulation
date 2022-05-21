package model;

import board.Gameboard;
import main.Macros;

import java.util.Random;

/**
 * Functions for finding targets for entities. Accessed only from Model.java.
 */
public class Moves {
    private final Gameboard board = Gameboard.getInstance();
    private static Moves instance = null;
    public static Moves getInstance() {
        if (instance == null) {
            instance = new Moves();
        }
        return instance;
    }


    /**
     * Find random move (distance 1) for entity
     * @param MovingEntity investigating entity
     * @return target position
     */
    public int FindRandomMove(Entity MovingEntity) {
        boolean CanMove = false;
        boolean[] DirectionsBool = new boolean[6];
        int[] DirectionsTile = new int[6];
        // left
        int nextTile;
        DirectionsTile[4] = nextTile = MovingEntity.getEntityPosition()-1;
        if (nextTile >= 0 && nextTile% Gameboard.gameboardWidth != Gameboard.gameboardWidth -1 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) { // left
            DirectionsBool[4] = CanMove = true;
        }
        // right
        DirectionsTile[5] = nextTile = MovingEntity.getEntityPosition()+1;
        if (nextTile < Gameboard.numberOfTiles && nextTile% Gameboard.gameboardWidth != 0 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) { // right
            DirectionsBool[5] = CanMove = true;
        }
        // odd rows
        if ((MovingEntity.getEntityPosition()/ Gameboard.gameboardWidth)%2 == 0) {
            // left up
            DirectionsTile[2] = nextTile = MovingEntity.getEntityPosition() - Gameboard.gameboardWidth - 1;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[2] = CanMove = true;
            }
            // right up
            DirectionsTile[3] = nextTile = MovingEntity.getEntityPosition() - Gameboard.gameboardWidth;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[3] = CanMove = true;
            }
            // left down
            DirectionsTile[0] = nextTile = MovingEntity.getEntityPosition() + Gameboard.gameboardWidth - 1;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[0] = CanMove = true;
            }
            // right down
            DirectionsTile[1] = nextTile = MovingEntity.getEntityPosition() + Gameboard.gameboardWidth;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[1] = CanMove = true;
            }
        }
        // even
        else {
            // left up
            DirectionsTile[2] = nextTile = MovingEntity.getEntityPosition() - Gameboard.gameboardWidth;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[2] = CanMove = true;
            }
            // right up
            DirectionsTile[3] = nextTile = MovingEntity.getEntityPosition() - Gameboard.gameboardWidth + 1;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[3] = CanMove = true;
            }
            // left down
            DirectionsTile[0] = nextTile = MovingEntity.getEntityPosition() + Gameboard.gameboardWidth;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[0] = CanMove = true;
            }
            // right down
            DirectionsTile[1] = nextTile = MovingEntity.getEntityPosition() + Gameboard.gameboardWidth + 1;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,MovingEntity) && board.GameboardArray[nextTile].getEntities() == 0) {
                DirectionsBool[1] = CanMove =  true;
            }
        }

        if (CanMove) {
            int Direction;
            while (true) {
                Random rand = new Random();
                Direction = rand.nextInt(6);
                if (DirectionsBool[Direction]) {
                    return DirectionsTile[Direction];
                }
            }
        }
        return MovingEntity.getEntityPosition();
    }


    /**
     *  Find any target (food or entity) within reach
     * @param MovingEntity investigating entity
     * @return target position or -1 if no target found
     */
    public int FindTargetForEntity(Entity MovingEntity) {
        int ReturnValue;
        if (MovingEntity.getTargetFood1() == Macros.FRUIT || MovingEntity.getTargetFood2() == Macros.FRUIT) {
            ReturnValue = TargetFoodNearby(MovingEntity.getEntityPosition(),0,MovingEntity,Macros.FRUIT);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        if (MovingEntity.getTargetFood1() == Macros.MUSHROOMS || MovingEntity.getTargetFood2() == Macros.MUSHROOMS) {
            ReturnValue = TargetFoodNearby(MovingEntity.getEntityPosition(),0,MovingEntity,Macros.MUSHROOMS);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        if (MovingEntity.getTargetAnimal1() == Macros.FISH || MovingEntity.getTargetAnimal2() == Macros.FISH) {
            ReturnValue = TargetEntityNearby(MovingEntity.getEntityPosition(),0,MovingEntity,Macros.FISH);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        if (MovingEntity.getTargetAnimal1() == Macros.CHICKEN || MovingEntity.getTargetAnimal2() == Macros.CHICKEN) {
            ReturnValue = TargetEntityNearby(MovingEntity.getEntityPosition(),0,MovingEntity,Macros.CHICKEN);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        if (MovingEntity.getTargetAnimal1() == Macros.DEER || MovingEntity.getTargetAnimal2() == Macros.DEER) {
            ReturnValue = TargetEntityNearby(MovingEntity.getEntityPosition(),0,MovingEntity,Macros.DEER);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        if (MovingEntity.getTargetAnimal1() == Macros.WOLF || MovingEntity.getTargetAnimal2() == Macros.WOLF) {
            ReturnValue = TargetEntityNearby(MovingEntity.getEntityPosition(),0,MovingEntity,Macros.WOLF);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        if (MovingEntity.getTargetAnimal1() == Macros.EAGLE || MovingEntity.getTargetAnimal2() == Macros.EAGLE) {
            ReturnValue = TargetEntityNearby(MovingEntity.getEntityPosition(),0,MovingEntity,Macros.EAGLE);
            return ReturnValue;
        }
        return -1;
    }

    /**
     * Check if HuntingEntity can enter nextTile
     * @param nextTile investigated tile
     * @param HuntingEntity moving entity
     * @return true if HuntingEntity can enter
     */
    private boolean TargetLandCond(int nextTile, Entity HuntingEntity) {
        return board.GameboardArray[nextTile].getLandType() == HuntingEntity.getAllowedLand1() || board.GameboardArray[nextTile].getLandType() == HuntingEntity.getAllowedLand2() || board.GameboardArray[nextTile].getLandType() == HuntingEntity.getAllowedLand3();
    }

    /**
     * Return Target position or continue recursion
     * @param nextTile investigated tile
     * @param Recursion current recursion
     * @param HuntingEntity moving entity
     * @param Target entity we are looking for
     * @return target position or -1 if no target found
     */
    private int TargetEntityNearbyRecursion(int nextTile, int Recursion, Entity HuntingEntity, short Target) {
        if (board.GameboardArray[nextTile].getEntities() == Target) {
            return nextTile;
        } else {
            return TargetEntityNearby(nextTile, Recursion+1,HuntingEntity,Target);
        }
    }

    /**
     * Recursive function to find target entity within reach
     * @param Tile investigated tile
     * @param Recursion current recursion
     * @param HuntingEntity moving entity
     * @param Target entity we are looking for
     * @return target position or -1 if no target found
     */
    private int TargetEntityNearby(int Tile, int Recursion, Entity HuntingEntity, short Target) {
        if (Recursion >= HuntingEntity.getReach()) {
            return -1;
        }
        int ReturnValue;
        int nextTile;
        // left
        nextTile = Tile-1;
        if (nextTile >= 0 && nextTile% Gameboard.gameboardWidth != Gameboard.gameboardWidth -1 && TargetLandCond(nextTile, HuntingEntity)) { // left
            ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        // right
        nextTile = Tile+1;
        if (nextTile < Gameboard.numberOfTiles && nextTile% Gameboard.gameboardWidth != 0 && TargetLandCond(nextTile, HuntingEntity)) { // right
            ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        // odd rows
        if ((Tile/ Gameboard.gameboardWidth)%2 == 0) {
            // left up
            nextTile = Tile - Gameboard.gameboardWidth - 1;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right up
            nextTile = Tile - Gameboard.gameboardWidth;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // left down
            nextTile = Tile + Gameboard.gameboardWidth - 1;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right down
            nextTile = Tile + Gameboard.gameboardWidth;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                return ReturnValue;
            }
        }
        // even
        else {
            // left up
            nextTile = Tile - Gameboard.gameboardWidth;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right up
            nextTile = Tile - Gameboard.gameboardWidth + 1;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // left down
            nextTile = Tile + Gameboard.gameboardWidth;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right down
            nextTile = Tile + Gameboard.gameboardWidth + 1;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile, HuntingEntity)) {
                ReturnValue = TargetEntityNearbyRecursion(nextTile,Recursion,HuntingEntity, Target);
                return ReturnValue;
            }
        }
        return -1;
    }


    /**
     * Return Target position or continue recursion
     * @param nextTile investigated tile
     * @param Recursion current recursion
     * @param HuntingEntity moving entity
     * @param Target food we are looking for
     * @return target position or -1 if no target found
     */
    private int TargetFoodNearbyRecursion(int nextTile, int Recursion, Entity HuntingEntity, short Target) {
        if (board.GameboardArray[nextTile].getFood() == Target && board.GameboardArray[nextTile].getEntities() == Macros.NONE) {
            return nextTile;
        } else {
            return TargetFoodNearby(nextTile, Recursion+1,HuntingEntity,Target);
        }
    }


    /**
     * Recursive function to find target food within reach
     * @param Tile investigated tile
     * @param Recursion current recursion
     * @param HuntingEntity moving entity
     * @param Target food we are looking for
     * @return target position or -1 if no target found
     */
    private int TargetFoodNearby(int Tile, int Recursion, Entity HuntingEntity, short Target) {
        if (Recursion == HuntingEntity.getReach()) {
            return -1;
        }
        int ReturnValue;
        int nextTile;
        // left
        nextTile = Tile-1;
        if (nextTile >= 0 && nextTile% Gameboard.gameboardWidth != Gameboard.gameboardWidth -1 && TargetLandCond(nextTile,HuntingEntity)) { // left
            ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        // right
        nextTile = Tile+1;
        if (nextTile < Gameboard.numberOfTiles && nextTile% Gameboard.gameboardWidth != 0 && TargetLandCond(nextTile,HuntingEntity)) { // right
            ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
            if (ReturnValue != -1) {
                return ReturnValue;
            }
        }
        // odd rows
        if ((Tile/ Gameboard.gameboardWidth)%2 == 0) {
            // left up
            nextTile = Tile - Gameboard.gameboardWidth - 1;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right up
            nextTile = Tile - Gameboard.gameboardWidth;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // left down
            nextTile = Tile + Gameboard.gameboardWidth - 1;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right down
            nextTile = Tile + Gameboard.gameboardWidth;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 1 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                return ReturnValue;
            }
        }
        // even
        else {
            // left up
            nextTile = Tile - Gameboard.gameboardWidth;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right up
            nextTile = Tile - Gameboard.gameboardWidth + 1;
            if (nextTile > 0 && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // left down
            nextTile = Tile + Gameboard.gameboardWidth;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                if (ReturnValue != -1) {
                    return ReturnValue;
                }
            }
            // right down
            nextTile = Tile + Gameboard.gameboardWidth + 1;
            if (nextTile < Gameboard.numberOfTiles && (nextTile/ Gameboard.gameboardWidth)%2 == 0 && TargetLandCond(nextTile,HuntingEntity)) {
                ReturnValue = TargetFoodNearbyRecursion(nextTile,Recursion,HuntingEntity,Target);
                return ReturnValue;
            }
        }
        return -1;
    }
}
