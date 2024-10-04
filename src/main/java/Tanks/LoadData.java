package Tanks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for loading terrain data from a file and creating terrain
 * arrays.
 */
public class LoadData {

    private String[][] terrainLayout;
    private String layoutFile;
    private int[][] heightMap;

    /**
     * Constructs a LoadData object with the specified layout file path.
     *
     * @param layoutFile The path to the file containing terrain layout data.
     */
    LoadData(String layoutFile) {
        this.layoutFile = layoutFile;
    }

    /**
     * Loads terrain data from the layout file.
     *
     * @param index The index of the terrain data to load (not currently used).
     * @return A 2D array representing the loaded terrain layout.
     */
    public String[][] loadTerrain(int index) {

        try {
            List<String> lines = readFileLines(layoutFile);
            if (lines != null && !lines.isEmpty()) {
                terrainLayout = createTerrainArray(lines);
                return terrainLayout;

            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return null;

    }

    /**
     * Creates a 2D array representing the terrain layout from a list of lines.
     *
     * @param lines The list of strings representing each line of terrain data.
     * @return A 2D array containing the terrain layout data.
     */
    public static String[][] createTerrainArray(List<String> lines) {
        int numRows = lines.size();
        int maxCols = lines.stream().mapToInt(String::length).max().orElse(0);

        String[][] terrain = new String[numRows][maxCols];

        for (int row = 0; row < numRows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < maxCols; col++) {
                if (col < line.length()) {
                    terrain[row][col] = String.valueOf(line.charAt(col));
                } else {
                    terrain[row][col] = " "; // Fill with space if end of line reached
                }
            }
        }

        return terrain;
    }

    /**
     * Loads terrain data from the layout file.
     *
     * @param fileName The index of the terrain data to load (not currently used).
     * @return A list representing the loaded terrain layout.
     * @throws IOException If an error occurs while reading the layout file.
     */
    public static List<String> readFileLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

}
