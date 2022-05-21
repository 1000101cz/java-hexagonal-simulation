package main;

import board.*;
import graphics.Graphics;
import graphics.Intro;
import logger.*;

import javax.swing.*;
import java.io.IOException;

public class Main {


    public static void main(String[] args) {

        // setup logger
        try {
            SimLogger.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("problems creating log file");
        }

        // set Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        SimLogger.LOGGER.info("Look & Feel set");

        Intro intro = new Intro();
        Gameboard board = Gameboard.getInstance();
        Graphics GUI = new Graphics();
        GameboardTest gameboardTest = GameboardTest.getInstance();

        intro.ShowIntro(); // open intro window

        // gameboard unit test
        gameboardTest.prepare();
        gameboardTest.testInvalidLandType();
        gameboardTest.testInvalidTileNumber();
        gameboardTest.testInvalidBoardSize();
        gameboardTest.reset();

        board.GenerateBoard(); // generate board
        GUI.InitGameboardGUI(); // prepare main window
        intro.HideIntro(); // close intro window
        GUI.DisplayWindow(); // display main window
    }
}
