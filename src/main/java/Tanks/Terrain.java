package Tanks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Represents the terrain for the game environment.
 * This class loads and manages the layout and features of the terrain.
 */
public class Terrain extends PApplet {

    protected String[][] terrainLayout;
    private int width;
    protected String foregroundColor;
    protected String background;
    private String tree;
    private int height;
    private int[][] heightMap;
    protected ArrayList<PVector> curvePoints;

    /**
     * Default Constructor
     */
    public Terrain() {
    }

    /**
     * Constructs a new Terrain object with the specified parameters.
     *
     * @param index           The index of the terrain layout within the game.
     * @param layoutFile      The file path to the terrain layout configuration.
     * @param backgroud      The background color or image for the terrain.
     * @param foregroudnColor The foreground color for the terrain.
     * @param tree            The type of trees or obstacles on the terrain.
     * @param width           The width of the terrain grid.
     * @param height          The height of the terrain grid.
     * @throws IOException If an I/O error occurs while loading the terrain layout.
     */
    public Terrain(int index, String layoutFile, String backgroud, String foregroudnColor, String tree, int width,
            int height) throws IOException {
        this.background = backgroud;
        this.foregroundColor = foregroudnColor;
        this.tree = tree;
        this.width = width;
        this.height = height;
        LoadData loadData = new LoadData(layoutFile);
        terrainLayout = loadData.loadTerrain(index);
        curvePoints = generateCurvePoints(terrainLayout, width, height);
    }

    /**
     * set a new width of the terrain
     * 
     * @param width set the current widht with the parameter
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * set a new height of the terrain
     * 
     * @param height set the current widht with the parameter
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Reterieve the cruvePoints
     * 
     * @return return the current curvePoints
     */
    public ArrayList<PVector> getCurvePoints() {
        return this.curvePoints;
    }

    /**
     * set a new Curve point
     * 
     * @param point PVector point
     */
    public void setCurvePoint(ArrayList<PVector> point) {
        this.curvePoints = point;
    }

    /**
     * Checks if a specified position on the terrain grid is considered a "hit".
     * A position is considered a hit if it corresponds to a specific terrain type.
     *
     * @param position The position vector (PVector) to check on the terrain grid.
     * @return {@code true} if the position is a hit, otherwise {@code false}.
     */
    public boolean isHit(PVector position) {
        // Convert position to grid coordinates
        int col = (int) (position.x / 32);
        int row = (int) (position.y / 32);

        // Check if the position is within the terrain bounds
        if (row >= 0 && row < terrainLayout.length && col >= 0 && col < terrainLayout[0].length) {
            String terrainType = terrainLayout[row][col];
            return terrainType == "X"; // Adjust based on your terrain type
        }

        return false; // Position is out of bounds or not part of the terrain
    }

    /**
     * Renders the background image on the PApplet canvas.
     * The background image is loaded from a specified path and scaled to fit the
     * specified width and height.
     *
     * @param app The PApplet instance where the graphic will be rendered.
     */
    public void renderGraphic(PApplet app) {
        PImage backgroundImg = app.loadImage("src/main/resources/Tanks/" + background);
        app.image(backgroundImg, 0, 0, width, width);
    }

    /**
     * Retrieves the RGB color components from the foreground color string.
     * The foreground color string is expected in the format "R,G,B" (e.g.,
     * "255,0,0" for red).
     *
     * @return An array of strings containing the RGB color components.
     */
    private String[] RGB() {
        String[] rgb = this.foregroundColor.split(",");
        return rgb;
    }

    /**
     * Draws a tree image on the PApplet canvas at the specified position (x, y).
     * The tree image is loaded from a specified path and rendered centered at the
     * given position.
     *
     * @param app The PApplet instance where the tree will be drawn.
     * @param x   The x-coordinate of the center of the cell where the tree will be
     *            drawn.
     * @param y   The y-coordinate of the center of the cell where the tree will be
     *            drawn.
     */
    public void drawTree(PApplet app, float x, float y) {
        // Load tree image (assuming 'treeImage' is a PImage attribute)

        PImage treeImage = app.loadImage("src/main/resources/Tanks/" + this.tree);

        // Calculate tree position based on cell center
        float treeWidth = 32; // Adjust tree width as needed
        float treeHeight = 32; // Adjust tree height as needed
        float treePosX = x - treeWidth / 2; // Center tree horizontally
        float treePosY = y - treeHeight / 7; // Align tree bottom with cell center vertically

        // Draw tree image at calculated position
        app.image(treeImage, treePosX, treePosY, treeWidth, treeHeight);
    }

    /**
     * Finds and returns tank spawn points based on the terrain layout, where each
     * valid spawn point
     * is marked with a player label (A-L) in the terrain layout matrix.
     * The method calculates spawn points as PVector coordinates within the terrain
     * grid.
     *
     * @return A map containing tank spawn points mapped to their corresponding
     *         player labels.
     */
    public Map<String, PVector> findTankSpawnPointsTwo() {
        Map<String, PVector> spawnPoints = new HashMap<>();
        float cellSize = (float) width / terrainLayout[0].length;

        if (terrainLayout != null) {
            for (int i = 0; i < terrainLayout.length; i++) {
                for (int j = 0; j < terrainLayout[i].length; j++) {
                    String cell = terrainLayout[i][j];
                    // Define the regular expression pattern for valid player labels (A-L)
                    String validPlayerPattern = "[A-L]";

                    if (cell.matches(validPlayerPattern)) {
                        // Calculate tank spawn point coordinates
                        float tankX = j * cellSize + cellSize / 3;
                        float tankY = i * cellSize + cellSize + cellSize / 4;

                        // Create PVector for the tank spawn point
                        PVector point = new PVector(tankX, tankY);

                        // Store the spawn point with its corresponding player label in the map
                        spawnPoints.put(cell, point);
                    }
                }
            }
        }

        return spawnPoints;
    }

    /**
     * Generates and returns a list of curve points based on the positions of "X"
     * cells in the terrain layout.
     * Curve points are calculated as PVector coordinates representing the center of
     * each "X" cell.
     * The generated list of points is sorted from left to right based on the
     * x-coordinate.
     *
     * @param terrainLayout The 2D array representing the terrain layout.
     * @param width         The total width of the terrain grid.
     * @param height        The total height of the terrain grid.
     * @return An ArrayList of PVector points representing the center positions of
     *         "X" cells.
     */
    public ArrayList<PVector> generateCurvePoints(String[][] terrainLayout, float width, float height) {
        ArrayList<PVector> points = new ArrayList<>();
        float cellSize = width / terrainLayout[0].length; // Calculate cell size based on width

        // Collect positions of "X" cells as curve points
        for (int i = 0; i < terrainLayout.length; i++) {
            for (int j = 0; j < terrainLayout[i].length; j++) {
                String cell = terrainLayout[i][j];
                float x = j * cellSize + cellSize / 2; // Center of the cell horizontally
                float y = i * cellSize + cellSize / 2; // Center of the cell vertically

                if (cell.equals("X")) {
                    points.add(new PVector(x, y)); // Add point to list
                }
            }
        }
        // Sort points based on x-coordinate (left to right)
        points.sort(Comparator.comparing(p -> p.x));

        return points;
    }

    /**
     * Draws the terrain based on the provided terrain layout, including curves,
     * trees, and filled areas.
     *
     * @param app The PApplet instance used for drawing.
     */
    public void drawTerrain(PApplet app) {

        String[] rgb = RGB();
        int red = Integer.parseInt(rgb[0]);
        int green = Integer.parseInt(rgb[1]);
        int blue = Integer.parseInt(rgb[2]);

        app.noFill();

        app.stroke(0); // Blue curve color
        app.strokeWeight(0);

        app.beginShape();
        for (PVector point : curvePoints) {
            app.curveVertex(point.x, point.y);
        }
        app.endShape();

        if (terrainLayout != null) {

            float cellSize = (float) width / terrainLayout[0].length; // Calculate cellsize based on width

            // List to store the positions of points representing "X" cells
            ArrayList<PVector> points = new ArrayList<>();

            // Collect positions of "X" cells as points
            for (int i = 0; i < terrainLayout.length; i++) {
                for (int j = 0; j < terrainLayout[i].length; j++) {
                    String cell = terrainLayout[i][j];
                    float x = j * cellSize + cellSize / 2; // Center of the cell horizontally
                    float y = i * cellSize + cellSize / 2; // Center of the cell vertically
                    if (cell.equals("X")) {

                        points.add(new PVector(x, y)); // Add point to list
                    } else if (cell.equals("T")) {
                        drawTree(app, x, y); // Draw tree at the center of the cell
                    }
                }
            }

            // Sort points based on x-coordinate (left to right)
            points.sort(Comparator.comparing(p -> p.x));

            // Add leftmost and rightmost points to define start and end of the curve
            PVector start = new PVector(0, points.get(0).y); // Leftmost point, alignedwith left edge
            PVector end = new PVector(width, points.get(points.size() - 1).y); // Rightmost point, aligned with right
            // edge

            // Draw straight lines to connect start and end points to the border of theGUI
            app.stroke(red, green, blue); // Set line color to red
            app.strokeWeight(2); // Set line weight (adjust as needed)

            // Draw line from start to left edge (0, y)
            app.line(start.x, start.y, 0, start.y);

            // Draw line from end to right edge (width, y)
            app.line(end.x, end.y, width, end.y);

            // Draw the filled area under the curve
            app.beginShape(); // Begin defining the filled shape
            app.fill(red, green, blue); // Set background color (light gray)

            // Add vertices for the filled area
            app.vertex(0, height); // Bottom-left corner
            for (PVector point : points) {
                app.curveVertex(point.x, point.y); // Vertex at the curve point
            }
            app.vertex(width, height); // Bottom-right corner
            app.endShape(app.CLOSE); // End defining the filled shape (CLOSE to connectlast vertex to first)

            // Draw the curve passing through the points
            app.beginShape(); // Begin defining the curve
            app.noFill(); // No fill for the curve
            app.stroke(red, green, blue); // Set line color to red
            app.strokeWeight(2); // Set line weight (adjust as needed)

            // Iterate over points and use curveVertex to draw smooth curve

            app.endShape(); // End defining the curve
        } else {
            System.out.println("Terrain layout is null");
        }
    }

}
