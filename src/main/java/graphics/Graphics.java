package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;

import board.*;
import logger.SimLogger;
import main.Macros;
import model.*;

/** Class displaying graphics and rendering board */
public class Graphics {
    public static int editedHexagon = 0;
    public static final int hexagonWidth = 168;
    public static final int hexagonHeight = 194;

    private static final Gameboard board = Gameboard.getInstance();
    private static final Model model = Model.getInstance();
    private static final BigButtons bigButtons = new BigButtons();

    public static int pixelBoardWidth = Gameboard.gameboardWidth * hexagonWidth + hexagonWidth / 2;
    public static int pixelBoardHeight = (int) ((Gameboard.gameboardHeight - 1) * (0.75 * hexagonHeight) + hexagonHeight);

    public static int[] HexGrid = new int[pixelBoardHeight * pixelBoardWidth * 3];

    public static JPopupMenu popup;

    private static Image GameboardImage;
    private static ImageIcon GameboardImageIcon;

    private static final MoreWindows moreWindows = new MoreWindows();

    public static JFrame frame;
    public static final JPanel[] panel = new JPanel[10];
    private static final JLabel[] label = new JLabel[10];



    Color BackgroundColor;


    private static volatile Graphics instance = null;

    public static Graphics getInstance() {
        if (instance == null) {
            synchronized (Graphics.class) {
                if (instance == null) {
                    instance = new Graphics();
                }
            }
        }
        return instance;
    }

    /** Draw whole HexGrid (grid, all tiles) */
    public void InitGameboardGUI() {
        SimLogger.LOGGER.info("drawing whole HexGrid");

        // all pixels to match system color
        JPanel panelX = new JPanel();
        BackgroundColor = panelX.getBackground();
        for (int row = 0; row < pixelBoardHeight; row++) {
            for (int column = 0; column < pixelBoardWidth; column++) {
                SetPixel(row, column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
            }
        }

        // draw grid
        DrawGrid();

        // fill tiles
        for (int y = 0; y < Gameboard.gameboardHeight; y++) {
            for (int x = 0; x < Gameboard.gameboardWidth; x++) {
                DrawField(y, x);
            }
        }
    }

    /**
     * Create Java Image from pixel array
     * @return created image
     */
    public Image ImageFromArray() {
        SimLogger.LOGGER.info("creating Java Image from pixel array");
        BufferedImage image = new BufferedImage(pixelBoardWidth, pixelBoardHeight, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0, 0, pixelBoardWidth, pixelBoardHeight, HexGrid);
        image.setData(raster);
        return image;
    }


    /** Save current board image to .png file */
    public void SavePngImageAction() {
        SimLogger.LOGGER.info("saving current board to .png file");
        BufferedImage image = new BufferedImage(pixelBoardWidth, pixelBoardHeight, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0, 0, pixelBoardWidth, pixelBoardHeight, HexGrid);
        image.setData(raster);
        File outputFile = new File("savedGrid.png");
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            SimLogger.LOGGER.severe("error saving board image to file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Change size of game board
     * @param newSize new size of square board
     */
    private void ChangeBoardSizeAction(int newSize) {
        SimLogger.LOGGER.info("changing board size to "+newSize);
        model.EntityArray = null;
        model.EntityArrayLength = 0;
        Gameboard.gameboardWidth = Gameboard.gameboardHeight = newSize;
        Gameboard.numberOfTiles = newSize*newSize;
        pixelBoardWidth = Gameboard.gameboardWidth * hexagonWidth + hexagonWidth / 2;
        pixelBoardHeight = (int) ((Gameboard.gameboardHeight - 1) * (0.75 * hexagonHeight) + hexagonHeight);
        HexGrid = new int[pixelBoardHeight * pixelBoardWidth * 3];
        board.GenerateBoard();
        InitGameboardGUI();
        UpdateWindow();
    }

    /**
     * Remove all entities from board, clear EntityArray and redraw affected hexagons
     */
    private void RemoveEntitiesAction() {
        SimLogger.LOGGER.info("removing all entities from board");
        for (int i = 0; i < Gameboard.numberOfTiles; i++) {
            if (board.GameboardArray[i].getEntities() != Macros.NONE) {
                model.RemoveEntityFromArray(0, model.EntityArray, model.EntityArrayLength);
                board.GameboardArray[i].setEntities(Macros.NONE);
                DrawField(i/Gameboard.gameboardWidth,i%Gameboard.gameboardWidth);
            }
        }
        UpdateWindow();
    }

    /**
     * Remove all food from board and redraw affected hexagons
     */
    private void RemoveFoodAction() {
        SimLogger.LOGGER.info("removing all food board");
        for (int i = 0; i < Gameboard.numberOfTiles; i++) {
            if (board.GameboardArray[i].getFood() != Macros.NONE) {
                board.GameboardArray[i].setFood(Macros.NONE);
                DrawField(i/Gameboard.gameboardWidth,i%Gameboard.gameboardWidth);
            }
        }
        UpdateWindow();
    }

    /**
     * Change all hexagons to one selected land type
     * @param land applied land type
     */
    private void ChangeLandsAction(short land) {
        SimLogger.LOGGER.info("changing all board land to " + land);
        for (int i = 0; i < Gameboard.numberOfTiles; i++) {
            if (board.GameboardArray[i].getLandType() != land) {
                board.GameboardArray[i].setLandType(land);
                DrawField(i/Gameboard.gameboardWidth,i%Gameboard.gameboardWidth);
            }
        }
        UpdateWindow();
    }

    /** Structure of menu bar (board options, settings, ...) */
    class SimMenuBar extends JMenuBar {
        public SimMenuBar() {
            SimLogger.LOGGER.info("setting up menu bar");
            JMenu boardMenu = new JMenu("Board");
            JMenuItem saveImageItem = new JMenuItem("Save Image");
            saveImageItem.addActionListener(e -> SavePngImageAction());
            JMenu boardSizeSubMenu = new JMenu("Change Board Size");
            JMenuItem boardSize1 = new JMenuItem("15x15");
            JMenuItem boardSize2 = new JMenuItem("30x30");
            JMenuItem boardSize3 = new JMenuItem("40x40");
            boardSize1.addActionListener(e -> ChangeBoardSizeAction(15));
            boardSize2.addActionListener(e -> ChangeBoardSizeAction(30));
            boardSize3.addActionListener(e -> ChangeBoardSizeAction(40));
            boardSizeSubMenu.add(boardSize1);
            boardSizeSubMenu.add(boardSize2);
            boardSizeSubMenu.add(boardSize3);
            JMenuItem removeEntities = new JMenuItem("Remove All Entities");
            removeEntities.addActionListener(e -> RemoveEntitiesAction());
            JMenuItem removeFood = new JMenuItem("Remove All Food");
            removeFood.addActionListener(e -> RemoveFoodAction());
            JMenu changeLandsSubMenu = new JMenu("Change All Lands");
            JMenuItem changeLandsPlains = new JMenuItem("Plains");
            JMenuItem changeLandsForest = new JMenuItem("Forest");
            JMenuItem changeLandsWater = new JMenuItem("Water");
            changeLandsPlains.addActionListener(e -> ChangeLandsAction(Macros.PLAINS));
            changeLandsForest.addActionListener(e -> ChangeLandsAction(Macros.FOREST));
            changeLandsWater.addActionListener(e -> ChangeLandsAction(Macros.WATER));
            changeLandsSubMenu.add(changeLandsPlains);
            changeLandsSubMenu.add(changeLandsForest);
            changeLandsSubMenu.add(changeLandsWater);
            boardMenu.add(saveImageItem);
            boardMenu.add(boardSizeSubMenu);
            boardMenu.add(removeEntities);
            boardMenu.add(removeFood);
            boardMenu.add(changeLandsSubMenu);
            this.add(boardMenu);

            JMenu settingsMenu = new JMenu("Settings");
            JMenuItem generatorSettingsItem = new JMenuItem("Generator");
            generatorSettingsItem.addActionListener(e -> moreWindows.DisplaySettingsWindow());
            settingsMenu.add(generatorSettingsItem);
            this.add(settingsMenu);

            JMenu helpMenu = new JMenu("Help");
            JMenuItem aboutItem = new JMenuItem("About");
            JMenuItem controlsItem = new JMenuItem("Controls");
            aboutItem.addActionListener(e -> moreWindows.DisplayAboutWindow());
            controlsItem.addActionListener(e -> moreWindows.DisplayControlsWindow());
            helpMenu.add(aboutItem);
            helpMenu.add(controlsItem);
            this.add(helpMenu);
        }
    }

    /** Display main app window. Called only one time from main function. */
    public void DisplayWindow() {
        SimLogger.LOGGER.info("setting up main frame window");
        frame = new JFrame("Simulator 2077");
        frame.setSize(1340, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        // create pop-up menu
        popup = new PopUpMenuStructure();

        // create menu bar
        JMenuBar menuBar = new SimMenuBar();
        frame.setJMenuBar(menuBar);

        // set app icon
        Image icon = Toolkit.getDefaultToolkit().getImage("media/icon.png");
        frame.setIconImage(icon);

        panel[1] = new JPanel();
        panel[1].setBounds(0, 0, 1380, 1000);
        panel[1].setLayout(null);

        label[1] = new JLabel();
        label[1].setBounds(20, 20, 1080, 900);

        // prepare game board image icon
        GameboardImage = ImageFromArray().getScaledInstance(1080, 900, java.awt.Image.SCALE_SMOOTH);
        GameboardImageIcon = new ImageIcon(GameboardImage);
        label[1].setIcon(GameboardImageIcon);

        // set pop-up listener
        MouseListener popupListener = new PopupListener();
        label[1].addMouseListener(popupListener);

        panel[1].add(label[1]);

        // add buttons to the right part of frame
        bigButtons.AddButtonsToPanel();

        frame.add(panel[1]);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        BackgroundColor = panel[1].getBackground();
        SimLogger.LOGGER.info("main frame window set up");
    }

    /** Display loading icon under on-screen buttons */
    public void ShowLoadingIcon() {
        SimLogger.LOGGER.info("displaying loading picture");
        Toolkit t=Toolkit.getDefaultToolkit();
        Image loadingImage = t.getImage("media/loadingLogo.gif");
        ImageIcon loadingImageIcon = new ImageIcon(loadingImage);
        label[2] = new JLabel();
        label[2].setBounds(1170, 450, 100, 100);
        label[2].setIcon(loadingImageIcon);
        panel[1].add(label[2]);
        frame.repaint();
    }

    /** Remove loading icon from frame */
    public void HideLoadingIcon() {
        SimLogger.LOGGER.info("hiding loading picture");
        panel[1].remove(label[2]);
        frame.repaint();
    }

    /** Update displayed board image */
    public void UpdateWindow() {
        SimLogger.LOGGER.info("updating displayed board image");
        GameboardImage = ImageFromArray().getScaledInstance(1080, 900, java.awt.Image.SCALE_SMOOTH);
        GameboardImageIcon = new ImageIcon(GameboardImage);
        label[1].setIcon(GameboardImageIcon);
        panel[1].add(label[1]);
        frame.add(panel[1]);
        frame.repaint();
    }

    /** Draw whole grid to HexGrid pixel array */
    private void DrawGrid() {
        SimLogger.LOGGER.info("drawing grid to pixel array");
        // draw each hexagon
        for (int row = 0; row < Gameboard.gameboardHeight; row++) {
            for (int column = 0; column < Gameboard.gameboardWidth; column++) {
                DrawGridSingleHexagon(row, column);
            }
        }
    }

    /**
     * Draw single hexagon to HexGrid
     * @param row hexagon row
     * @param column hexagon column
     */
    private void DrawGridSingleHexagon(int row, int column) {
        double first_row = row * 0.75 * hexagonHeight;
        double first_column = column * hexagonWidth;
        int current_row;
        int current_column;
        if (row % 2 == 1) {
            first_column = first_column + hexagonWidth / 2.0;
        }
        for (int y_axis = 0; y_axis < hexagonHeight; y_axis++) {
            for (int x_axis = 0; x_axis < hexagonWidth; x_axis++) {
                current_row = (int) first_row + y_axis;
                current_column = (int) first_column + x_axis;

                boolean leftUp = (y_axis > (-7.0 / 12) * x_axis + 47.0 && y_axis < (-7.0 / 12) * x_axis + 51.0);
                boolean rightUp = (y_axis > (7.0 / 12) * x_axis - 51.0 && y_axis < (7.0 / 12) * x_axis - 47.0);
                boolean leftDown = y_axis > (7.0 / 12) * x_axis + 143.0 && y_axis < (7.0 / 12) * x_axis + 147.0;
                boolean rightDown = (y_axis > (-7.0 / 12) * x_axis + 241.0 && y_axis < (-7.0 / 12) * x_axis + 245.0);

                if (y_axis < 50) {
                    if (leftUp || rightUp) {
                        SetPixel(current_row, current_column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
                    }
                } else if (y_axis < 146) {
                    if (column != 0 && column != Gameboard.gameboardWidth - 1) {
                        if (x_axis < 2 || x_axis > hexagonWidth - 3) {
                            SetPixel(current_row, current_column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
                        }
                    } else if (column == 0) {
                        if (x_axis < 4 || x_axis > hexagonWidth - 3) {
                            SetPixel(current_row, current_column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
                        }
                    } else {
                        if (x_axis < 2 || x_axis > hexagonWidth - 5) {
                            SetPixel(current_row, current_column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
                        }
                    }
                } else {
                    if (row == Gameboard.gameboardHeight - 1) {
                        if (leftDown || rightDown) {
                            SetPixel(current_row, current_column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
                        }
                    } else {
                        if (row % 2 == 0) {
                            if (column == 0) {
                                if (leftDown) {
                                    SetPixel(current_row, current_column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
                                }
                            }
                        } else {
                            if (rightDown) {
                                SetPixel(current_row, current_column, BackgroundColor.getRed(), BackgroundColor.getGreen(), BackgroundColor.getBlue());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Set RGB values of selected pixel
     * @param row y pixel coordinate in HexGrid
     * @param column x pixel coordinate in HexGrid
     * @param r R value (0-255)
     * @param g G value (0-255)
     * @param b B value (0-255)
     */
    private void SetPixel(int row, int column, int r, int g, int b) {
        HexGrid[row * 3 * pixelBoardWidth + 3 * column] = (byte) r;
        HexGrid[row * 3 * pixelBoardWidth + 3 * column + 1] = (byte) g;
        HexGrid[row * 3 * pixelBoardWidth + 3 * column + 2] = (byte) b;
    }

    /**
     * Draw all textures (function reads them from GameboardArray) to selected tile
     * @param row tile row
     * @param column tile column
     */
    public void DrawField(int row, int column) {
        double first_row = row * 0.75 * hexagonHeight;
        double first_column = column * hexagonWidth;
        if (row % 2 == 1) {
            first_column = first_column + hexagonWidth / 2.0 + pixelBoardWidth / 2.0;
        }

        // render land
        File input;
        if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getLandType() == Macros.PLAINS) {
            input = new File("textures/hexagon_plain.ppm");
        } else if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getLandType() == Macros.FOREST) {
            input = new File("textures/hexagon_forest.ppm");
        } else if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getLandType() == Macros.WATER) {
            input = new File("textures/hexagon_water.ppm");
        } else {
            input = new File("textures/hexagon_plain.ppm");
        }
        WriteTexture(input, first_row, first_column);

        // render food
        if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getFood() != Macros.NONE) {
            if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getFood() == Macros.FRUIT) {
                input = new File("textures/hexagon_fruit.ppm");
            } else if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getFood() == Macros.MUSHROOMS) {
                input = new File("textures/hexagon_mushrooms.ppm");
            }
            WriteTexture(input, first_row, first_column);
        }

        // render entities
        if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getEntities() != Macros.NONE) {
            if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getEntities() == Macros.FISH) {
                input = new File("textures/hexagon_fish.ppm");
            } else if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getEntities() == Macros.CHICKEN) {
                input = new File("textures/hexagon_chicken.ppm");
            } else if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getEntities() == Macros.DEER) {
                input = new File("textures/hexagon_deer.ppm");
            } else if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getEntities() == Macros.WOLF) {
                input = new File("textures/hexagon_wolf.ppm");
            } else if (board.GameboardArray[column + row * Gameboard.gameboardWidth].getEntities() == Macros.EAGLE) {
                input = new File("textures/hexagon_eagle.ppm");
            }
            WriteTexture(input, first_row, first_column);
        }
    }

    /**
     * Draw single texture (from selected texture file) to selected hexagon
     * @param input file with texture
     * @param first_row first pixel row
     * @param first_column first pixel column
     */
    private void WriteTexture(File input, double first_row, double first_column) {
        byte[][][] TexturePlain;
        TexturePlain = new byte[194][168][3];

        try {
            BufferedReader br = new BufferedReader(new FileReader(input));  //creates a buffering character input stream
            br.readLine();
            br.readLine();
            br.readLine();
            for (int maxRow = 0; maxRow < 194; maxRow++) {
                for (int maxColumn = 0; maxColumn < 168; maxColumn++) {
                    TexturePlain[maxRow][maxColumn][0] = (byte) Integer.parseInt(br.readLine());
                    TexturePlain[maxRow][maxColumn][1] = (byte) Integer.parseInt(br.readLine());
                    TexturePlain[maxRow][maxColumn][2] = (byte) Integer.parseInt(br.readLine());
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int y_axis = 0; y_axis < hexagonHeight; y_axis++) {
            for (int x_axis = 0; x_axis < hexagonWidth; x_axis++) {
                if (!(TexturePlain[y_axis][x_axis][0] == (byte) 254 && TexturePlain[y_axis][x_axis][1] == (byte) 255 && TexturePlain[y_axis][x_axis][2] == (byte) 255)) {
                    double v = (first_row + y_axis) * 3 * pixelBoardWidth + (first_column + x_axis) * 3;
                    HexGrid[(int) v] = 0xff & TexturePlain[y_axis][x_axis][0];
                    HexGrid[(int) v + 1] = 0xff & TexturePlain[y_axis][x_axis][1];
                    HexGrid[(int) v + 2] = 0xff & TexturePlain[y_axis][x_axis][2];
                }
            }
        }
    }
}

