package model;

/**
 * Storing board generator params
 */
public class GenerationParams {
    short InitFishProbability, InitChickenProbability, InitDeerProbability, InitWolfProbability, InitEagleProbability;
    short FishProbability, ChickenProbability, DeerProbability, WolfProbability, EagleProbability;
    short InitFruitProbability, InitMushroomsProbability;
    short FruitProbability, MushroomsProbability;

    // Initialize
    GenerationParams(short InitFruitProbability, short InitMushroomsProbability, short FruitProbability, short MushroomsProbability, short InitFishProbability, short InitChickenProbability, short InitDeerProbability, short InitWolfProbability, short InitEagleProbability, short FishProbability, short ChickenProbability, short DeerProbability, short WolfProbability, short EagleProbability) {
        this.InitFishProbability = InitFishProbability;
        this.InitChickenProbability = InitChickenProbability;
        this.InitDeerProbability = InitDeerProbability;
        this.InitWolfProbability = InitWolfProbability;
        this.InitEagleProbability = InitEagleProbability;

        this.FishProbability = FishProbability;
        this.ChickenProbability = ChickenProbability;
        this.DeerProbability = DeerProbability;
        this.WolfProbability = WolfProbability;
        this.EagleProbability = EagleProbability;

        this.InitFruitProbability = InitFruitProbability;
        this.InitMushroomsProbability = InitMushroomsProbability;

        this.FruitProbability = FruitProbability;
        this.MushroomsProbability = MushroomsProbability;
    }

    // Set entity generation
    public void setInitFishProbability(short InitFishProbability) {
        this.InitFishProbability = InitFishProbability;
    }
    public void setInitChickenProbability(short InitChickenProbability) { this.InitChickenProbability = InitChickenProbability;}
    public void setInitDeerProbability(short InitDeerProbability) {
        this.InitDeerProbability = InitDeerProbability;
    }
    public void setInitWolfProbability(short InitWolfProbability) {
        this.InitWolfProbability = InitWolfProbability;
    }
    public void setInitEagleProbability(short InitEagleProbability) { this.InitEagleProbability = InitEagleProbability; }
    public void setFishProbability(short FishProbability) {
        this.FishProbability = FishProbability;
    }
    public void setChickenProbability(short ChickenProbability) {
        this.ChickenProbability = ChickenProbability;
    }
    public void setDeerProbability(short DeerProbability) {
        this.DeerProbability = DeerProbability;
    }
    public void setWolfProbability(short WolfProbability) {
        this.WolfProbability = WolfProbability;
    }
    public void setEagleProbability(short EagleProbability) {
        this.EagleProbability = EagleProbability;
    }

    // Get entity generation
    public short getInitFishProbability() {
        return this.InitFishProbability;
    }
    public short getInitChickenProbability() {
        return this.InitChickenProbability;
    }
    public short getInitDeerProbability() {
        return this.InitDeerProbability;
    }
    public short getInitWolfProbability() {
        return this.InitWolfProbability;
    }
    public short getInitEagleProbability() {
        return this.InitEagleProbability;
    }
    public short getFishProbability() {
        return this.FishProbability;
    }
    public short getChickenProbability() {
        return this.ChickenProbability;
    }
    public short getDeerProbability() {
        return this.DeerProbability;
    }
    public short getWolfProbability() {
        return this.WolfProbability;
    }
    public short getEagleProbability() {
        return this.EagleProbability;
    }


    // Set food generation
    public void setInitFruitProbability(short FruitProbability) {
        this.InitFruitProbability = FruitProbability;
    }
    public void setInitMushroomsProbability(short MushroomsProbability) { this.InitMushroomsProbability = MushroomsProbability; }
    public void setFruitProbability(short FruitProbability) {
        this.FruitProbability = FruitProbability;
    }
    public void setMushroomsProbability(short MushroomsProbability) { this.MushroomsProbability = MushroomsProbability; }

    // Get food generation
    public short getInitFruitProbability() {
        return this.InitFruitProbability;
    }
    public short getInitMushroomsProbability() {
        return InitMushroomsProbability;
    }
    public short getFruitProbability() {
        return this.FruitProbability;
    }
    public short getMushroomsProbability() {
        return MushroomsProbability;
    }
}
