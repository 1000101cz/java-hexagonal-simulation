package graphics;

import board.Gameboard;
import logger.SimLogger;
import main.Macros;
import model.Entity;
import model.Model;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Class describing MouseListener and displaying pop-up menu */
class PopupListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    /**
     * Get number of selected hexagon
     * @param x x board pixel coordinate
     * @param y y board pixel coordinate
     * @return index of selected tile
     */
    private int getHexagonNumber(int x, int y) {
        int row;
        int column;
        double hexagonWidth = (1080.0/(Gameboard.gameboardWidth+0.5));
        double hexagonHeight = (900.0/(Gameboard.gameboardHeight));
        row = (int) (y/hexagonHeight);
        if (row%2==1) {
            column = (int)((x-hexagonWidth/2)/hexagonWidth);
        } else {
            column = (int)(x/hexagonWidth);
        }
        Graphics.editedHexagon = column+row* Gameboard.gameboardWidth;
        return column+row* Gameboard.gameboardWidth;
    }

    /**
     * Show pop-up menu
     * @param e mouse event
     */
    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            SimLogger.LOGGER.info("hexagon "+getHexagonNumber(e.getX(),e.getY())+" selected");
            Graphics.popup.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    }
}

/**
 * Structure of pop-up menu. Pop-up menu is used for adding and removing objects from board.
 */
class PopUpMenuStructure extends JPopupMenu {
    private static final Model model = Model.getInstance();
    private static final Gameboard board = Gameboard.getInstance();
    private static final Graphics GUI = Graphics.getInstance();

    /**
     * Action performed when adding/removing entity to board
     * @param entityType new entity type (0 for none)
     */
    private void EntityAction(short entityType) {
        SimLogger.LOGGER.info("popup entity action");
        if (board.GameboardArray[Graphics.editedHexagon].getEntities() != Macros.NONE) {
            model.EntityArray = model.RemoveEntityFromArray(model.EntityNumberFromPosition(Graphics.editedHexagon), model.EntityArray, model.EntityArrayLength);
        }
        board.GameboardArray[Graphics.editedHexagon].setEntities(entityType);
        if (entityType != Macros.NONE) {
            model.EntityArray = model.AddEntityToArray(new Entity(Graphics.editedHexagon, entityType), model.EntityArray, model.EntityArrayLength);
        }
        GUI.DrawField(Graphics.editedHexagon / Gameboard.gameboardWidth, Graphics.editedHexagon % Gameboard.gameboardWidth);
        GUI.UpdateWindow();
    }

    /**
     * Action performed when adding/removing food to board
     * @param foodType new food type (0 for none)
     */
    private void FoodAction(short foodType) {
        SimLogger.LOGGER.info("popup food action");
        board.GameboardArray[Graphics.editedHexagon].setFood(foodType);
        GUI.DrawField(Graphics.editedHexagon / Gameboard.gameboardWidth, Graphics.editedHexagon % Gameboard.gameboardWidth);
        GUI.UpdateWindow();
    }

    /**
     * Action performed when changing land type
     * @param landType new land type
     */
    private void LandTypeAction(short landType) {
        SimLogger.LOGGER.info("popup land type action");
        board.GameboardArray[Graphics.editedHexagon].setLandType(landType);
        GUI.DrawField(Graphics.editedHexagon / Gameboard.gameboardWidth, Graphics.editedHexagon % Gameboard.gameboardWidth);
        GUI.UpdateWindow();
    }

    /** Setup pop-up menu */
    public PopUpMenuStructure() {
        SimLogger.LOGGER.info("setting up popup menu");

        // Entity section
        JMenu popupEntityMenu = new JMenu("Entity");
        JMenuItem popupNoEntity = new JMenuItem("\uD83D\uDEAB");
        JMenuItem popupFish = new JMenuItem("Fish");
        JMenuItem popupChicken = new JMenuItem("Chicken");
        JMenuItem popupDeer = new JMenuItem("Deer");
        JMenuItem popupWolf = new JMenuItem("Wolf");
        JMenuItem popupEagle = new JMenuItem("Eagle");
        popupNoEntity.addActionListener(e -> EntityAction(Macros.NONE));
        popupFish.addActionListener(e -> EntityAction(Macros.FISH));
        popupChicken.addActionListener(e -> EntityAction(Macros.CHICKEN));
        popupDeer.addActionListener(e -> EntityAction(Macros.DEER));
        popupWolf.addActionListener(e -> EntityAction(Macros.WOLF));
        popupEagle.addActionListener(e -> EntityAction(Macros.EAGLE));
        popupEntityMenu.add(popupNoEntity);
        popupEntityMenu.add(popupFish);
        popupEntityMenu.add(popupChicken);
        popupEntityMenu.add(popupDeer);
        popupEntityMenu.add(popupWolf);
        popupEntityMenu.add(popupEagle);

        // Food section
        JMenu popupFoodMenu = new JMenu("Food");
        JMenuItem popupNoFood = new JMenuItem("\uD83D\uDEAB");
        JMenuItem popupFruit = new JMenuItem("Fruit");
        JMenuItem popupMushrooms = new JMenuItem("Mushrooms");
        popupNoFood.addActionListener(e -> FoodAction(Macros.NONE));
        popupFruit.addActionListener(e -> FoodAction(Macros.FRUIT));
        popupMushrooms.addActionListener(e -> FoodAction(Macros.MUSHROOMS));
        popupFoodMenu.add(popupNoFood);
        popupFoodMenu.add(popupFruit);
        popupFoodMenu.add(popupMushrooms);

        // Land type section
        JMenu popupLandTypeMenu = new JMenu("Land Type");
        JMenuItem popupPlains = new JMenuItem("Plains");
        JMenuItem popupForest = new JMenuItem("Forest");
        JMenuItem popupWater = new JMenuItem("Water");
        popupPlains.addActionListener(e -> LandTypeAction(Macros.PLAINS));
        popupForest.addActionListener(e -> LandTypeAction(Macros.FOREST));
        popupWater.addActionListener(e -> LandTypeAction(Macros.WATER));
        popupLandTypeMenu.add(popupPlains);
        popupLandTypeMenu.add(popupForest);
        popupLandTypeMenu.add(popupWater);
        this.add(popupEntityMenu);
        this.add(popupFoodMenu);
        this.add(popupLandTypeMenu);
    }
}