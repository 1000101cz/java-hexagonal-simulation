package graphics;

import board.Gameboard;
import logger.SimLogger;
import model.Model;

import javax.swing.*;
import java.awt.*;

/** Class describing windows for settings, controls etc. */
public class MoreWindows {
    private static final Model model = Model.getInstance();
    public static JFrame aboutFrame;
    public static JFrame controlsFrame;
    public static JFrame settingsFrame;

    /** Display window with info about this application */
    public void DisplayAboutWindow() {
        SimLogger.LOGGER.info("Displaying About window");
        if (aboutFrame == null) {
            aboutFrame = new JFrame("About");
            aboutFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            aboutFrame.setSize(400, 170);
            aboutFrame.setResizable(false);
            String aboutWindowText;
            aboutWindowText = "<html><center><h1>Simulator 2077</h1><br>Graphical Java Maven application<br>Stepan Marousek<br>FEE CTU  |  PJV  |  2022</center></html>";
            JLabel textLabel = new JLabel(aboutWindowText, SwingConstants.CENTER);
            textLabel.setPreferredSize(new Dimension(400, 170));
            aboutFrame.getContentPane().add(textLabel);
            aboutFrame.setLocationRelativeTo(null);
            aboutFrame.pack();
            aboutFrame.setVisible(true);
        } else {
            aboutFrame.setVisible(true);
        }
    }

    /** Display window with application control info */
    public void DisplayControlsWindow() {
        SimLogger.LOGGER.info("Displaying Controls window");
        if (controlsFrame == null) {
            controlsFrame = new JFrame("Controls");
            controlsFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            controlsFrame.setSize(300, 330);
            controlsFrame.setResizable(false);
            String aboutWindowText;
            aboutWindowText = """
                    <html>
                    <center>
                    <h1>Buttons</h1>
                    <br>
                    <table cellspacing="3" cellpadding="5">
                    <tr align="left">
                    \t<th>Button</th>
                    \t<th>Action</th>
                    
                    <tr><td><b>Next Turn:</b></td>   <td>Proceed to next day</td></tr>
                    <tr><td><b>Play:</b></td>        <td>Start animation</td></tr>
                    <tr><td><b>Speed:</b></td>       <td>Change animation speed</td></tr>
                    <tr><td><b>Load:</b></td>        <td>Load last saved game</td></tr>
                    <tr><td><b>Save:</b></td>        <td>Save current map</td></tr>
                    <tr><td><b>Generate:</b></td>    <td>Generate new map</td></tr>
                    <tr><td><b>Exit:</b></td>        <td>Close application</td></tr>
                    </table>
                    </center>
                    </html>""";
            JLabel textLabel = new JLabel(aboutWindowText);
            textLabel.setPreferredSize(new Dimension(300, 330));
            controlsFrame.getContentPane().add(textLabel);
            controlsFrame.setLocationRelativeTo(null);
            controlsFrame.pack();
            controlsFrame.setVisible(true);
        } else {
            controlsFrame.setVisible(true);
        }
    }

    /** Display generator settings window */
    public void DisplaySettingsWindow() {
        SimLogger.LOGGER.info("Displaying Generator Settings window");
        if (settingsFrame == null) {
            settingsFrame = new JFrame("Generator Settings");
            settingsFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            settingsFrame.setSize(600, 600);
            settingsFrame.setResizable(false);

            GridLayout experimentLayout = new GridLayout(13, 2);

            JTabbedPane panel = new JTabbedPane();

            // Food generator
            JPanel foodGeneratorTab = new JPanel();
            foodGeneratorTab.setLayout(experimentLayout);
            foodGeneratorTab.add(new JLabel("INIT GENERATION:"));
            foodGeneratorTab.add(new JLabel(""));
            String stringInitFruit = "       Fruit generation probability";
            JLabel textInitFruitProbability = new JLabel(stringInitFruit);
            foodGeneratorTab.add(textInitFruitProbability);
            JSlider sliderInitFruitProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getInitFruitProbability());
            sliderInitFruitProbability.addChangeListener(e -> model.GenParams.setInitFruitProbability((short) sliderInitFruitProbability.getValue()));
            foodGeneratorTab.add(sliderInitFruitProbability);
            String stringInitMushrooms = "       Mushrooms generation probability";
            JLabel textInitMushroomsProbability = new JLabel(stringInitMushrooms);
            foodGeneratorTab.add(textInitMushroomsProbability);
            JSlider sliderInitMushroomsProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getInitMushroomsProbability());
            sliderInitMushroomsProbability.addChangeListener(e -> model.GenParams.setInitMushroomsProbability((short) sliderInitMushroomsProbability.getValue()));
            foodGeneratorTab.add(sliderInitMushroomsProbability);
            foodGeneratorTab.add(new JLabel(""));
            foodGeneratorTab.add(new JLabel(""));
            foodGeneratorTab.add(new JLabel("IN-TURN GENERATION:"));
            foodGeneratorTab.add(new JLabel(""));
            String stringTurnFruit = "       Fruit generation probability";
            JLabel textTurnFruitProbability = new JLabel(stringTurnFruit);
            foodGeneratorTab.add(textTurnFruitProbability);
            JSlider sliderTurnFruitProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getFruitProbability());
            sliderTurnFruitProbability.addChangeListener(e -> model.GenParams.setFruitProbability((short) sliderTurnFruitProbability.getValue()));
            foodGeneratorTab.add(sliderTurnFruitProbability);
            String stringTurnMushrooms = "       Mushrooms generation probability";
            JLabel textTurnMushroomsProbability = new JLabel(stringTurnMushrooms);
            foodGeneratorTab.add(textTurnMushroomsProbability);
            JSlider sliderTurnMushroomsProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getMushroomsProbability());
            sliderTurnMushroomsProbability.addChangeListener(e -> model.GenParams.setMushroomsProbability((short) sliderTurnMushroomsProbability.getValue()));
            foodGeneratorTab.add(sliderTurnMushroomsProbability);

            // Entity generator
            JPanel entityGeneratorTab = new JPanel();
            entityGeneratorTab.setLayout(experimentLayout);
            entityGeneratorTab.add(new JLabel("INIT GENERATION:"));
            entityGeneratorTab.add(new JLabel(""));
            JLabel textInitFishProbability = new JLabel("       Fish generation probability");
            entityGeneratorTab.add(textInitFishProbability);
            JSlider sliderInitFishProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getInitFishProbability());
            sliderInitFishProbability.addChangeListener(e -> model.GenParams.setInitFishProbability((short) sliderInitFishProbability.getValue()));
            entityGeneratorTab.add(sliderInitFishProbability);
            JLabel textInitChickenProbability = new JLabel("       Chicken generation probability");
            entityGeneratorTab.add(textInitChickenProbability);
            JSlider sliderInitChickenProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getInitChickenProbability());
            sliderInitChickenProbability.addChangeListener(e -> model.GenParams.setInitChickenProbability((short) sliderInitChickenProbability.getValue()));
            entityGeneratorTab.add(sliderInitChickenProbability);
            JLabel textInitDeerProbability = new JLabel("       Deer generation probability");
            entityGeneratorTab.add(textInitDeerProbability);
            JSlider sliderInitDeerProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getInitDeerProbability());
            sliderInitDeerProbability.addChangeListener(e -> model.GenParams.setInitDeerProbability((short) sliderInitDeerProbability.getValue()));
            entityGeneratorTab.add(sliderInitDeerProbability);
            JLabel textInitWolfProbability = new JLabel("       Wolf generation probability");
            entityGeneratorTab.add(textInitWolfProbability);
            JSlider sliderInitWolfProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getInitWolfProbability());
            sliderInitWolfProbability.addChangeListener(e -> model.GenParams.setInitWolfProbability((short) sliderInitWolfProbability.getValue()));
            entityGeneratorTab.add(sliderInitWolfProbability);
            JLabel textInitEagleProbability = new JLabel("       Eagle generation probability");
            entityGeneratorTab.add(textInitEagleProbability);
            JSlider sliderInitEagleProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getInitEagleProbability());
            sliderInitEagleProbability.addChangeListener(e -> model.GenParams.setInitEagleProbability((short) sliderInitEagleProbability.getValue()));
            entityGeneratorTab.add(sliderInitEagleProbability);
            entityGeneratorTab.add(new JLabel(""));
            entityGeneratorTab.add(new JLabel(""));
            entityGeneratorTab.add(new JLabel("IN-TURN GENERATION:"));
            entityGeneratorTab.add(new JLabel(""));
            JLabel textTurnFishProbability = new JLabel("       Fish generation probability");
            entityGeneratorTab.add(textTurnFishProbability);
            JSlider sliderTurnFishProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getFishProbability());
            sliderTurnFishProbability.addChangeListener(e -> model.GenParams.setFishProbability((short) sliderTurnFishProbability.getValue()));
            entityGeneratorTab.add(sliderTurnFishProbability);
            JLabel textTurnChickenProbability = new JLabel("       Chicken generation probability");
            entityGeneratorTab.add(textTurnChickenProbability);
            JSlider sliderTurnChickenProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getChickenProbability());
            sliderTurnChickenProbability.addChangeListener(e -> model.GenParams.setChickenProbability((short) sliderTurnChickenProbability.getValue()));
            entityGeneratorTab.add(sliderTurnChickenProbability);
            JLabel textTurnDeerProbability = new JLabel("       Deer generation probability");
            entityGeneratorTab.add(textTurnDeerProbability);
            JSlider sliderTurnDeerProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getDeerProbability());
            sliderTurnDeerProbability.addChangeListener(e -> model.GenParams.setDeerProbability((short) sliderTurnDeerProbability.getValue()));
            entityGeneratorTab.add(sliderTurnDeerProbability);
            JLabel textTurnWolfProbability = new JLabel("       Wolf generation probability");
            entityGeneratorTab.add(textTurnWolfProbability);
            JSlider sliderTurnWolfProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getWolfProbability());
            sliderTurnWolfProbability.addChangeListener(e -> model.GenParams.setWolfProbability((short) sliderTurnWolfProbability.getValue()));
            entityGeneratorTab.add(sliderTurnWolfProbability);
            JLabel textTurnEagleProbability = new JLabel("       Eagle generation probability");
            entityGeneratorTab.add(textTurnEagleProbability);
            JSlider sliderTurnEagleProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, model.GenParams.getEagleProbability());
            sliderTurnEagleProbability.addChangeListener(e -> model.GenParams.setEagleProbability((short) sliderTurnEagleProbability.getValue()));
            entityGeneratorTab.add(sliderTurnEagleProbability);

            // Land-Type generator
            JPanel landGeneratorTab = new JPanel();
            landGeneratorTab.setLayout(experimentLayout);
            JLabel textForestSize = new JLabel("       Forest generation size");
            landGeneratorTab.add(textForestSize);
            JSlider sliderForestSize = new JSlider(JSlider.HORIZONTAL, 0, 40, Gameboard.maxForestRecursion);
            sliderForestSize.addChangeListener(e -> Gameboard.maxForestRecursion = (short) sliderForestSize.getValue());
            landGeneratorTab.add(sliderForestSize);
            JLabel textForestQuantity = new JLabel("       Forest generation quantity");
            landGeneratorTab.add(textForestQuantity);
            JSlider sliderForestQuantity = new JSlider(JSlider.HORIZONTAL, 0, 20, Gameboard.ForestQuantity);
            sliderForestQuantity.addChangeListener(e -> Gameboard.ForestQuantity = (short) sliderForestQuantity.getValue());
            landGeneratorTab.add(sliderForestQuantity);
            landGeneratorTab.add(new JLabel(""));
            landGeneratorTab.add(new JLabel(""));
            JLabel textWaterSize = new JLabel("       Water generation size");
            landGeneratorTab.add(textWaterSize);
            JSlider sliderWaterSize = new JSlider(JSlider.HORIZONTAL, 0, 40, Gameboard.maxWaterRecursion);
            sliderWaterSize.addChangeListener(e -> Gameboard.maxWaterRecursion = (short) sliderWaterSize.getValue());
            landGeneratorTab.add(sliderWaterSize);
            JLabel textWaterQuantity = new JLabel("       Water generation quantity");
            landGeneratorTab.add(textWaterQuantity);
            JSlider sliderWaterQuantity = new JSlider(JSlider.HORIZONTAL, 0, 20, Gameboard.WaterQuantity);
            sliderWaterQuantity.addChangeListener(e -> Gameboard.WaterQuantity = (short) sliderWaterQuantity.getValue());
            landGeneratorTab.add(sliderWaterQuantity);
            landGeneratorTab.add(new JLabel(""));
            landGeneratorTab.add(new JLabel(""));
            landGeneratorTab.add(new JLabel(""));
            landGeneratorTab.add(new JLabel(""));

            panel.add("Food Generator", foodGeneratorTab);
            panel.add("Entity Generator", entityGeneratorTab);
            panel.add("Land Generator", landGeneratorTab);

            settingsFrame.add(panel);
            settingsFrame.setResizable(false);
            settingsFrame.setLocationRelativeTo(null);
            settingsFrame.pack();
            settingsFrame.setVisible(true);
        } else {
            settingsFrame.setVisible(true);
        }
    }
}
