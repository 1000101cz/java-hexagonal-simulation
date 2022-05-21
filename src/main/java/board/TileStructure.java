package board;

/**
 * structure of tile, holds data about land type, entity and food on tile
 */
public class TileStructure {
    short LandType;
    short Food;
    short Entities;

    TileStructure(short Land, short Food, short Entities) {
        this.LandType = Land;
        this.Food = Food;
        this.Entities = Entities;
    }

    public void setLandType(short landType) {
        this.LandType = landType;
    }

    public void setFood(short food) {
        this.Food = food;
    }

    public void setEntities(short entities) {
        this.Entities = entities;
    }

    public short getEntities() {
        return this.Entities;
    }

    public short getFood() {
        return this.Food;
    }

    public short getLandType() {
        return this.LandType;
    }
}
