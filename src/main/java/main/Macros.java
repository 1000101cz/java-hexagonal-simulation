package main;

/** MACRO values to make code more readable */
public class Macros {
    public static final short NONE = 0;
    public static final short INVALID = 66;

    // entities
    public static final short FISH = 1;
    public static final short CHICKEN = 2;
    public static final short DEER = 3;
    public static final short WOLF = 4;
    public static final short EAGLE = 5;

    // food
    public static final short FRUIT = 1;
    public static final short MUSHROOMS = 2;

    // land types
    public static final short PLAINS = 0;
    public static final short FOREST = 1;
    public static final short WATER = 5;

    public String getEntity(short entity) {
        if (entity == FISH) {
            return "fish";
        } else if (entity == CHICKEN) {
            return "chicken";
        } else if (entity == DEER) {
            return "deer";
        } else if (entity == WOLF) {
            return "wolf";
        } else if (entity == EAGLE) {
            return "eagle";
        }
        return "";
    }
}
