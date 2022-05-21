package graphics;

import logger.SimLogger;

import javax.swing.*;
import java.awt.*;

/** Class controlling intro window */
public class Intro {
    public JWindow introFrame;

    /** Display intro loading window */
    public void ShowIntro() {
        SimLogger.LOGGER.info("Displaying Intro window");
        introFrame = new JWindow();
        introFrame.setSize(854, 480);
        introFrame.setLayout(null);

        Image icon = Toolkit.getDefaultToolkit().getImage("media/icon.png");
        introFrame.setIconImage(icon);

        Image introImage = Toolkit.getDefaultToolkit().getImage("media/intro.gif");
        ImageIcon introImageIcon = new ImageIcon(introImage);
        JLabel label = new JLabel();
        label.setSize(854,480);
        label.setIcon(introImageIcon);
        introFrame.add(label);

        introFrame.setLocationRelativeTo(null);

        introFrame.setVisible(true);
        SimLogger.LOGGER.info("Intro window displayed");
    }

    public void HideIntro() {
        introFrame.dispose();
        SimLogger.LOGGER.info("Intro window closed");
    }
}
