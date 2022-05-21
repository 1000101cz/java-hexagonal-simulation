package model;

import board.Gameboard;
import graphics.Graphics;
import logger.SimLogger;
import main.Macros;

import java.util.Random;

/**
 * Class handling simulation logic (moving creatures, eating creatures and food, ...)
 */
public class Model {

    private final Gameboard board = Gameboard.getInstance();

    private final Graphics graphics = Graphics.getInstance();

    private final Moves moves = Moves.getInstance();
    private final Macros macros = new Macros();

    public Entity[] EntityArray;
    public int EntityArrayLength;
    public int AnimationSpeed = 2;


    public GenerationParams GenParams = new GenerationParams((short)5,(short)5,(short)1,(short)1,(short)5,(short)5,(short)5,(short)1,(short)1,(short)1,(short)1,(short)1,(short)0,(short)0);

    private static volatile Model instance = null;
    public static Model getInstance() {
        if (instance == null) {
            synchronized (Model.class) {
                if (instance == null) {
                    instance = new Model();
                }
            }
        }
        return instance;
    }

    public Model() {

    }

    public void NextTurnAndUpdateImage() {
        NextTurn();
        graphics.UpdateWindow();
    }

    /** Move, kill or spawn creatures */
    public void NextTurn() {
        SimLogger.LOGGER.info("proceeding to next turn");
        // kill starving entities
        for (int i = 0; i < EntityArrayLength; i++) {
            if (EntityArray[i].getHunger() == EntityArray[i].getMaxHunger()) {
                SimLogger.LOGGER.info("entity (type: "+EntityArray[i].getEntityType()+" | position: "+EntityArray[i].getEntityPosition()+") is starving");
                board.GameboardArray[EntityArray[i].getEntityPosition()].setEntities(Macros.NONE);
                graphics.DrawField(EntityArray[i].getEntityPosition()/ Gameboard.gameboardWidth,EntityArray[i].getEntityPosition()% Gameboard.gameboardWidth);
                EntityArray = RemoveEntityFromArray(i,EntityArray,EntityArrayLength);
                i--;
            }
        }

        // move carnivores
        int newLocation;
        for (int i = 0; i < EntityArrayLength; i++) {
            if (EntityArray[i].getEntityType() == Macros.WOLF || EntityArray[i].getEntityType() == Macros.EAGLE) {
                newLocation = moves.FindTargetForEntity(EntityArray[i]);
                if (newLocation == -1) { // no target -> make random move
                    MakeRandomMove(i);
                } else { // found target -> eat other entity
                    SimLogger.LOGGER.info("found target for " + macros.getEntity(EntityArray[i].getEntityType()) + " (" + EntityArray[i].getEntityPosition()+") on tile " + newLocation + " ("+macros.getEntity(board.GameboardArray[newLocation].getEntities())+")");
                    EntityArray[i].setHunger((short)(EntityArray[i].getHunger()-5)); // reduce hunger
                    for (int target = 0; target < EntityArrayLength; target++) {
                        if (EntityArray[target].getEntityPosition() == newLocation) {
                            EntityArray[target].setEntityPosition(-1); // prepare entity for removing
                            break;
                        }
                    }
                    MoveEntity(i, newLocation);
                }
            }
        }

        // remove killed herbivores
        for (int i = 0; i < EntityArrayLength; i++) {
            if (EntityArray[i].getEntityPosition() == -1) {
                EntityArray = instance.RemoveEntityFromArray(i,EntityArray,EntityArrayLength);
                i--;
            }
        }

        // move herbivores
        for (int i = 0; i < EntityArrayLength; i++) {
            if (EntityArray[i].getEntityType() == Macros.FISH || EntityArray[i].getEntityType() == Macros.CHICKEN || EntityArray[i].getEntityType() == Macros.DEER) {
                newLocation = moves.FindTargetForEntity(EntityArray[i]);
                if (newLocation == -1) { // no target -> make random move
                    MakeRandomMove(i);
                } else { // found target -> eat food
                    EntityArray[i].setHunger((short)(EntityArray[i].getHunger()-5)); // reduce hunger
                    board.GameboardArray[newLocation].setFood(Macros.NONE); // remove food from board
                    MoveEntity(i, newLocation);
                }
            }
        }


        // spawn entities
        GenerateEntity(Macros.FISH,false);
        GenerateEntity(Macros.CHICKEN,false);
        GenerateEntity(Macros.DEER,false);
        GenerateEntity(Macros.WOLF,false);
        GenerateEntity(Macros.EAGLE,false);

        // spawn food
        GenerateFood(Macros.FRUIT,false); // fruit
        GenerateFood(Macros.MUSHROOMS,false); // mushrooms
    }

    /**
     * Move entity to new location
     * @param i EntityArray index
     * @param newLocation hexagon index of new position
     */
    private void MoveEntity(int i, int newLocation) {
        SimLogger.LOGGER.info("moving entity "+macros.getEntity(EntityArray[i].getEntityType())+" from tile "+EntityArray[i].getEntityPosition()+" to "+newLocation);
        board.GameboardArray[EntityArray[i].getEntityPosition()].setEntities(Macros.NONE); // remove carnivore from previous position on board
        graphics.DrawField(EntityArray[i].getEntityPosition()/ Gameboard.gameboardWidth,EntityArray[i].getEntityPosition()% Gameboard.gameboardWidth);
        EntityArray[i].setEntityPosition(newLocation); // change carnivore position in entity array
        board.GameboardArray[newLocation].setEntities(EntityArray[i].getEntityType()); // write entity to new position on board
        graphics.DrawField(newLocation/ Gameboard.gameboardWidth,newLocation% Gameboard.gameboardWidth);
    }

    /**
     * find random move for entity and do it
     * @param i EntityArray index
     */
    private void MakeRandomMove(int i) {
        int newLocation = moves.FindRandomMove(EntityArray[i]);
        if (newLocation != -1) { // can move
            MoveEntity(i, newLocation);
        }
        EntityArray[i].setHunger((short)(EntityArray[i].getHunger()+1)); //  increase hunger
    }

    /**
     * add entity to array of entities
     */
    public Entity[] AddEntityToArray(Entity NewEntity, Entity[] OldArray, int OldArrayLength) {
        SimLogger.LOGGER.info("adding " + macros.getEntity(NewEntity.getEntityType()) + " to tile "+NewEntity.getEntityPosition());
        Entity[] NewEntityArray;
        if (OldArray == null) {
            NewEntityArray= new Entity[1];
        } else {
            NewEntityArray = new Entity[OldArrayLength+1];
            if (OldArrayLength >= 0) System.arraycopy(OldArray, 0, NewEntityArray, 0, OldArrayLength);
        }
        NewEntityArray[OldArrayLength] = NewEntity;
        EntityArrayLength++;
        return NewEntityArray;
    }

    /**
     * Remove entity from array of entities
     * @param EntityNumber entity index in EntityArray
     * @param OldArray current EntityArray
     * @param OldArrayNumber current EntityArray length
     * @return new EntityArray
     */
    public Entity[] RemoveEntityFromArray(int EntityNumber, Entity[] OldArray, int OldArrayNumber) {
        SimLogger.LOGGER.info("removing " + macros.getEntity(OldArray[EntityNumber].getEntityType()));
        if (EntityNumber >= OldArrayNumber) {
            return OldArray;
        }

        Entity[] NewEntityArray;
        NewEntityArray = new Entity[OldArrayNumber-1];

        int pointer = 0;
        for (int i = 0; i < OldArrayNumber; i++) {
            if (i != EntityNumber) {
                NewEntityArray[pointer] = OldArray[i];
                pointer++;
            }
        }
        EntityArrayLength--;
        return NewEntityArray;
    }


    /**
     * Find EntityArray index of entity on selected hexagon
     * @param position selected hexagon
     * @return EntityArray index of entity
     */
    public int EntityNumberFromPosition(int position) {
        for (int i = 0; i < EntityArrayLength; i++) {
            if (EntityArray[i].getEntityPosition() == position) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Lands condition for entity generation
     * @param entityType entity type
     * @param i game board tile index
     * @return true if entity can be generated on this tile, false otherwise
     */
    private boolean GenerateEntityCondition(short entityType, int i) {
        if (entityType == Macros.FISH) {
            return board.GameboardArray[i].getLandType() == Macros.WATER;
        } else if (entityType == Macros.CHICKEN) {
            return board.GameboardArray[i].getLandType() == Macros.PLAINS;
        } else if (entityType == Macros.DEER) {
            return board.GameboardArray[i].getLandType() == Macros.FOREST;
        } else if (entityType == Macros.WOLF) {
            return (board.GameboardArray[i].getLandType() == Macros.PLAINS || board.GameboardArray[i].getLandType() == Macros.FOREST);
        }
        return true;
    }

    /**
     * Go through all map hexagons and decide if entity is spawned
     * @param entityType currently spawning entity type
     * @param init true if init spawn probability is used
     */
    public void GenerateEntity(short entityType, boolean init) {
        short spawnProbability = -1;
        if (entityType == Macros.FISH) {
            if (init) {
                spawnProbability = GenParams.getInitFishProbability();
            } else {
                spawnProbability = GenParams.getFishProbability();
            }
        } else if (entityType == Macros.CHICKEN) {
            if (init) {
                spawnProbability = GenParams.getInitChickenProbability();
            } else {
                spawnProbability = GenParams.getChickenProbability();
            }
        } else if (entityType == Macros.DEER) {
            if (init) {
                spawnProbability = GenParams.getInitDeerProbability();
            } else {
                spawnProbability = GenParams.getDeerProbability();
            }
        } else if (entityType == Macros.WOLF) {
            if (init) {
                spawnProbability = GenParams.getInitWolfProbability();
            } else {
                spawnProbability = GenParams.getWolfProbability();
            }
        } else if (entityType == Macros.EAGLE) {
            if (init) {
                spawnProbability = GenParams.getInitEagleProbability();
            } else {
                spawnProbability = GenParams.getEagleProbability();
            }
        }

        Random rand = new Random();
        int randNumber;
        int randNextInt = 100;
        if (!init) {
            randNextInt = 4000;
        }
        for (int i = 0; i < Gameboard.numberOfTiles; i++) {
            if (GenerateEntityCondition(entityType,i) && board.GameboardArray[i].getEntities() == Macros.NONE) {
                randNumber = rand.nextInt(randNextInt);
                if (randNumber < spawnProbability) {
                    board.GameboardArray[i].setEntities(entityType);
                    if (!init) {
                        graphics.DrawField(i/ Gameboard.gameboardWidth,i% Gameboard.gameboardWidth);
                    }
                    EntityArray = AddEntityToArray(new Entity(i, entityType), EntityArray, EntityArrayLength);
                }
            }
        }
    }

    /**
     * Go through all map hexagons and decide if food is spawned
     * @param foodType currently spawning food type
     * @param init true if init spawn probability is used
     */
    public void GenerateFood(short foodType, boolean init) {
        short conditionLand = -1;
        short spawnProbability = -1;
        if (foodType == Macros.FRUIT) {
            conditionLand = Macros.PLAINS;
            if (init) {
                spawnProbability = GenParams.getInitFruitProbability();
            } else {
                spawnProbability = GenParams.getFruitProbability();
            }
        } else if (foodType == Macros.MUSHROOMS) {
            conditionLand = Macros.FOREST;
            if (init) {
                spawnProbability = GenParams.getInitMushroomsProbability();
            } else {
                spawnProbability = GenParams.getMushroomsProbability();
            }
        }

        Random rand = new Random();
        int randNumber;
        for (int i = 0; i < Gameboard.numberOfTiles; i++) {
            if (board.GameboardArray[i].getLandType() == conditionLand) {
                randNumber = rand.nextInt(1000);
                if (randNumber < spawnProbability) {
                    board.GameboardArray[i].setFood(foodType);
                    if (!init) {
                        graphics.DrawField(i / Gameboard.gameboardWidth, i % Gameboard.gameboardWidth);
                    }
                }
            }
        }
    }
}

