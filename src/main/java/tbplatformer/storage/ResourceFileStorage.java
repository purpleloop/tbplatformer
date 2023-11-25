package tbplatformer.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import tbplatformer.GameException;
import tbplatformer.TileSet;
import tbplatformer.model.LevelMap;

/** Game resources storage. */
public class ResourceFileStorage implements ResourceStorage {

    /** File extensions for maps. */
    private static final String MAP_EXTENSION = ".txt";

    /** File extensions for images. */
    private static final String IMAGE_EXTENSION = ".gif";

    /** Delimiters between cell values in the map file. */
    private static final String CELL_DELIMITER = "\\s+";

    /** Filesystem path where maps are stored. */
    private Path mapStoragePath = Paths.get("maps/");

    /** Filesystem path where tilesets are stored. */
    private Path tilesSetStoragePath = Paths.get("graphics/");
    
    @Override
    public LevelMap getMapByName(String mapName) {

        File mapFile = getFileForResource(mapStoragePath, mapName, MAP_EXTENSION);

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

    @Override
    public TileSet getTileSetByName(String tileSetImageName) {
        
        File tileSetFile = getFileForResource(tilesSetStoragePath, tileSetImageName, IMAGE_EXTENSION);
        
        try {
            return new TileSet(ImageIO.read(tileSetFile));
    
        } catch (IOException e) {
            throw new GameException("Error while loading the tileset", e);
        }
    
    }

    /**
     * Get a the file corresponding to a given resource name.
     * @param path path for the resource
     * @param resourceName the resource name
     * @param extension file extension
     * 
     * @return the file used to store resource
     */
    private File getFileForResource(Path path, String resourceName, String extension) {
        if (!resourceName.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("Invalid resource name");
        }

        return path.resolve(resourceName + extension).toFile();
    }

}
