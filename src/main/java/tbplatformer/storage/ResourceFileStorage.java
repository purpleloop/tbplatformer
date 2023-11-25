package tbplatformer.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import tbplatformer.GameException;
import tbplatformer.model.LevelMap;

/** Game resources storage. */
public class ResourceFileStorage implements ResourceStorage {

    /** File extensions for maps. */
    private static final String MAP_EXTENSION = ".txt";

    /** Delimiters between cell values in the map file. */
    private static final String CELL_DELIMITER = "\\s+";

    /** FilesystempPath where maps are stored. */
    private Path mapStoragePath = Paths.get("maps/");

    @Override
    public LevelMap getMapByName(String mapName) {

        File mapFile = getFileForMapName(mapName);

        try (BufferedReader mapBufferedReader = new BufferedReader(new FileReader(mapFile));) {

            // File begins with the width and height of the map
            int mapWidth = Integer.parseInt(mapBufferedReader.readLine());
            int mapHeight = Integer.parseInt(mapBufferedReader.readLine());

            // Next we have the level map structure given by the value of each
            // tiles in a 2D array.
            LevelMap levelMap = new LevelMap(mapWidth, mapHeight);

            for (int row = 0; row < mapHeight; row++) {
                String line = mapBufferedReader.readLine();
                String[] tokens = line.split(CELL_DELIMITER);

                for (int col = 0; col < mapWidth; col++) {
                    levelMap.setTile(row, col, Integer.parseInt(tokens[col]));
                }

            }

            return levelMap;

        } catch (IOException e) {
            throw new GameException("Error while reading the level map " + mapName, e);
        }
    }

    /**
     * Get a the file corresponding for a given map name.
     * 
     * @param mapName the map name
     * @return the file used to store the map
     */
    private File getFileForMapName(String mapName) {
        if (!mapName.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("Invalid map name");
        }

        return mapStoragePath.resolve(mapName + MAP_EXTENSION).toFile();
    }
}
