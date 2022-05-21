package model;
/*
Animals:
 1 - fish
 2 - chic
 3 - deer
 4 - wolf
 5 - eagle
*/


import main.Macros;

/**
 * Describes entity structure and sets default values
 */
public class Entity {
    int EntityPosition;
    short EntityType; // fish / chicken / deer / wolf / eagle
    short Hunger; // current entity hunger
    short MaxHunger; // max hunger - entity die when hunger reaches this value
    short Reach; // max distance for finding target
    short TargetAnimal1, TargetAnimal2;
    short TargetFood1, TargetFood2;
    short AllowedLand1, AllowedLand2, AllowedLand3; // land types entity can enter

    // Initialize
    public Entity(int EntityPosition, short EntityType) {
        this.EntityPosition = EntityPosition;
        this.EntityType = EntityType;
        this.Hunger = 0;
        if (EntityType == Macros.FISH) {
            this.MaxHunger = 20;
            this.Reach = 1;
            this.TargetAnimal1 = Macros.NONE;
            this.TargetAnimal2 = Macros.NONE;
            this.TargetFood1   = Macros.NONE;
            this.TargetFood2   = Macros.NONE;
            this.AllowedLand1  = Macros.WATER;
            this.AllowedLand2  = Macros.WATER;
            this.AllowedLand3  = Macros.WATER;
        } else if (EntityType == Macros.CHICKEN) {
            this.MaxHunger = 20;
            this.Reach = 3;
            this.TargetAnimal1 = Macros.NONE;
            this.TargetAnimal2 = Macros.NONE;
            this.TargetFood1   = Macros.FRUIT;
            this.TargetFood2   = Macros.MUSHROOMS;
            this.AllowedLand1  = Macros.PLAINS;
            this.AllowedLand2  = Macros.FOREST;
            this.AllowedLand3  = Macros.FOREST;
        } else if (EntityType == Macros.DEER) {
            this.MaxHunger = 10;
            this.Reach = 3;
            this.TargetAnimal1 = Macros.NONE;
            this.TargetAnimal2 = Macros.NONE;
            this.TargetFood1   = Macros.FRUIT;
            this.TargetFood2   = Macros.MUSHROOMS;
            this.AllowedLand1  = Macros.PLAINS;
            this.AllowedLand2  = Macros.FOREST;
            this.AllowedLand3  = Macros.FOREST;
        } else if (EntityType == Macros.WOLF) {
            this.MaxHunger = 10;
            this.Reach = 3;
            this.TargetAnimal1 = Macros.CHICKEN;
            this.TargetAnimal2 = Macros.DEER;
            this.TargetFood1   = Macros.NONE;
            this.TargetFood2   = Macros.NONE;
            this.AllowedLand1  = Macros.PLAINS;
            this.AllowedLand2  = Macros.FOREST;
            this.AllowedLand3  = Macros.FOREST;
        } else if (EntityType == Macros.EAGLE) {
            this.MaxHunger = 10;
            this.Reach = 2;
            this.TargetAnimal1 = Macros.FISH;
            this.TargetAnimal2 = Macros.CHICKEN;
            this.TargetFood1   = Macros.NONE;
            this.TargetFood2   = Macros.NONE;
            this.AllowedLand1  = Macros.PLAINS;
            this.AllowedLand2  = Macros.FOREST;
            this.AllowedLand3  = Macros.WATER;
        }
    }

    // Change entity position
    public void setEntityPosition(int NewPosition) {
        this.EntityPosition = NewPosition;
    }

    // Change entity hunger
    public void setHunger(short NewHunger) {
        this.Hunger = NewHunger;
    }


    public int getEntityPosition() {
        return this.EntityPosition;
    }
    public short getEntityType() {
        return this.EntityType;
    }
    public short getHunger() {
        return this.Hunger;
    }
    public short getMaxHunger() { return this.MaxHunger; }

    public short getReach() {
        return this.Reach;
    }

    public short getTargetAnimal1() {
        return this.TargetAnimal1;
    }

    public short getTargetAnimal2() {
        return this.TargetAnimal2;
    }

    public short getTargetFood1() {
        return this.TargetFood1;
    }

    public short getTargetFood2() {
        return this.TargetFood2;
    }

    public short getAllowedLand1() {
        return this.AllowedLand1;
    }

    public short getAllowedLand2() {
        return this.AllowedLand2;
    }

    public short getAllowedLand3() {
        return this.AllowedLand3;
    }
}
