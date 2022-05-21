package graphics;

import board.Gameboard;
import logger.SimLogger;
import main.Macros;
import model.Entity;
import model.Model;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class BigButtons {
    private static Thread PlayThread;
    Graphics GUI = new Graphics();
    private static final Model model = Model.getInstance();
    Gameboard board = Gameboard.getInstance();
    public Font MyFont = new Font("Cantarell", Font.PLAIN, 18);

    private boolean Play = false;

    private final JButton NextTurnButton = new JButton("Next Turn");
    JButton PlayStopButton = new JButton("Play");
    JButton SpeedButton = new JButton("Speed 1x ");
    JButton LoadButton = new JButton("Load");
    JButton SaveButton = new JButton("Save");
    JButton GenerateButton = new JButton("Generate");
    JButton ExitButton = new JButton("EXIT");


    /** Action to perform when Play/Stop button is pressed */
    public void PlayButtonAction() {
        SimLogger.LOGGER.info("play button pressed");
        if (!Play) {
            SimLogger.LOGGER.info("starting simulation");
            SimLogger.LOGGER.info("starting loading animation");
            GUI.ShowLoadingIcon();
            NextTurnButton.setEnabled(false);
            LoadButton.setEnabled(false);
            SaveButton.setEnabled(false);
            GenerateButton.setEnabled(false);
            PlayThread = new Thread(() -> {
                PlayStopButton.setText("Stop");
                Play = true;
                while (model.EntityArrayLength > 0 && Play) {
                    model.NextTurnAndUpdateImage();
                    if (!Play) {
                        break;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(model.AnimationSpeed);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            PlayThread.start();
        } else {
            SimLogger.LOGGER.info("stopping simulation");
            SimLogger.LOGGER.info("terminating loading animation");
            GUI.HideLoadingIcon();
            NextTurnButton.setEnabled(true);
            LoadButton.setEnabled(true);
            SaveButton.setEnabled(true);
            GenerateButton.setEnabled(true);
            PlayStopButton.setText("Play");
            Play = false;
            try {
                PlayThread.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /** Action to perform when Next Turn button is pressed */
    public void NextTurnButtonAction() {
        if (!Play) {
            SimLogger.LOGGER.info("next turn button pressed");
            model.NextTurnAndUpdateImage();
        }
    }

    /** Action to perform when Generate button is pressed */
    public void GenerateButtonAction() {
        SimLogger.LOGGER.info("generate button pressed");
        if (!Play) {
            SimLogger.LOGGER.info("removing old entities and food from board");
            for (int i = 0; i < Gameboard.numberOfTiles; i++) {
                board.GameboardArray[i].setEntities(Macros.NONE);
                board.GameboardArray[i].setFood(Macros.NONE);
            }
            SimLogger.LOGGER.info("removing old entities from EntityArray");
            int repeat = model.EntityArrayLength;
            for (int i = 0; i < repeat; i++) {
                model.RemoveEntityFromArray(0, model.EntityArray, model.EntityArrayLength);
            }
            board.GenerateBoard();
            for (int i = 0; i < Gameboard.numberOfTiles; i++) {
                GUI.DrawField(i / Gameboard.gameboardWidth, i % Gameboard.gameboardWidth);
            }
            GUI.UpdateWindow();
        }
    }

    /** Action to perform when Speed button is pressed */
    public void SpeedButtonAction() {
        SimLogger.LOGGER.info("speed button pressed");
        if (model.AnimationSpeed == 2) {
            SimLogger.LOGGER.info("setting animation speed to normal speed");
            model.AnimationSpeed = 1;
            SpeedButton.setText("Speed 2x ");
        } else if (model.AnimationSpeed == 1) {
            SimLogger.LOGGER.info("setting animation speed to max speed");
            model.AnimationSpeed = 0;
            SpeedButton.setText("Speed MAX");
        } else if (model.AnimationSpeed == 0) {
            SimLogger.LOGGER.info("setting animation speed to min speed");
            model.AnimationSpeed = 2;
            SpeedButton.setText("Speed 1x ");
        }
    }

    /** Action to perform when Load button is pressed */
    public void LoadButtonAction() {
        SimLogger.LOGGER.info("load button pressed");
        if (!Play) {
            // remove old entities
            int repeat = model.EntityArrayLength;
            for (int i = 0; i < repeat; i++) {
                model.RemoveEntityFromArray(0, model.EntityArray, model.EntityArrayLength);
            }

            // load and display Board
            try {
                board.LoadBoard("saves/board.save");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Graphics.pixelBoardWidth = Gameboard.gameboardWidth * Graphics.hexagonWidth + Graphics.hexagonWidth / 2;
            Graphics.pixelBoardHeight = (int) ((Gameboard.gameboardHeight - 1) * (0.75 * Graphics.hexagonHeight) + Graphics.hexagonHeight);
            Graphics.HexGrid = new int[Graphics.pixelBoardHeight * Graphics.pixelBoardWidth * 3];
            GUI.InitGameboardGUI();
            for (int i = 0; i < Gameboard.numberOfTiles; i++) {
                GUI.DrawField(i / Gameboard.gameboardWidth, i % Gameboard.gameboardWidth);
                if (board.GameboardArray[i].getEntities() != 0) {
                    model.EntityArray = model.AddEntityToArray(new Entity(i, board.GameboardArray[i].getEntities()), model.EntityArray, model.EntityArrayLength);
                }
            }
            GUI.UpdateWindow();
        }
    }

    /** Action to perform when Save button is pressed */
    public void SaveButtonAction() {
        SimLogger.LOGGER.info("save button pressed");
        board.SaveBoard();
    }

    /** Action to perform when Exit button is pressed */
    public void ExitButtonAction() {
        SimLogger.LOGGER.info("exit button pressed");
        Graphics.frame.dispose();
        if (Play) {
            Play = false;
            try {
                PlayThread.join();
            } catch (InterruptedException ex) {
                SimLogger.LOGGER.severe("joining PlayThread failed");
                throw new RuntimeException(ex);
            }
        }
        if (MoreWindows.settingsFrame != null) {
            MoreWindows.settingsFrame.dispose();
        }
        if (MoreWindows.aboutFrame != null) {
            MoreWindows.aboutFrame.dispose();
        }
        if (MoreWindows.controlsFrame != null) {
            MoreWindows.controlsFrame.dispose();
        }
    }

    /**
     * setup on-screen buttons and add them to panel
     */
    public void AddButtonsToPanel() {
        SimLogger.LOGGER.info("adding on-screen buttons to main frame");
        // Next turn button
        NextTurnButton.setFont(MyFont);
        NextTurnButton.setBounds(1120, 20, 200, 60);
        NextTurnButton.setFocusPainted(false);
        NextTurnButton.addActionListener(e -> NextTurnButtonAction());
        Graphics.panel[1].add(NextTurnButton);

        // Speed button
        SpeedButton.setFont(MyFont);
        SpeedButton.setBounds(1120, 150, 200, 60);
        SpeedButton.setFocusPainted(false);
        SpeedButton.addActionListener(e -> SpeedButtonAction());
        Graphics.panel[1].add(SpeedButton);

        // Load button
        LoadButton.setFont(MyFont);
        LoadButton.setBounds(1120, 215, 200, 60);
        LoadButton.setFocusPainted(false);
        LoadButton.addActionListener(e -> LoadButtonAction());
        Graphics.panel[1].add(LoadButton);

        // Save button
        SaveButton.setFont(MyFont);
        SaveButton.setBounds(1120, 280, 200, 60);
        SaveButton.setFocusPainted(false);
        SaveButton.addActionListener(e -> SaveButtonAction());
        Graphics.panel[1].add(SaveButton);

        // Generate button
        GenerateButton.setFont(MyFont);
        GenerateButton.setBounds(1120, 345, 200, 60);
        GenerateButton.setFocusPainted(false);
        GenerateButton.addActionListener(e -> GenerateButtonAction());
        Graphics.panel[1].add(GenerateButton);

        // Play button
        PlayStopButton.setFont(MyFont);
        PlayStopButton.setBounds(1120, 85, 200, 60);
        PlayStopButton.setFocusPainted(false);
        PlayStopButton.addActionListener(e -> PlayButtonAction());
        Graphics.panel[1].add(PlayStopButton);

        // Exit button
        ExitButton.setFont(MyFont);
        ExitButton.setBounds(1120, 860, 200, 60);
        GenerateButton.setFocusPainted(false);
        ExitButton.addActionListener(e -> ExitButtonAction());
        Graphics.panel[1].add(ExitButton);
    }
}
