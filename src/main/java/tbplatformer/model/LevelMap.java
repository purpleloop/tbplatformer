package tbplatformer.model;

/** Models a representation of map as an array of tiles numbers. */
public class LevelMap {

    /** Width of the map. */
    private int width;

    /** Height of the map. */
    private int height;

    /** Tiles ids for each cell of the map. */
    private int[][] tileId;

    /**
     * Creates a new empty map.
     * 
     * @param width width of the map
     * @param height height of the map
     */
    public LevelMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tileId = new int[height][width];
    }

    /** @return the map width */
    public int getWidth() {
        return width;
    }

    /** @return them map height */
    public int getHeight() {
        return height;
    }

    /**
     * @param row row
     * @param col column
     * @return true if cell is valid, false otherwise
     */
    public boolean isValid(int row, int col) {
        return (row >= 0) && (col >= 0) && (row < height) && (col < width);
    }

    /**
     * @param row row where to set the value
     * @param col column where to set the value
     * @param value the tile id value
     */
    public void setTile(int row, int col, int value) {
        this.tileId[row][col] = value;
    }

    /**
     * @param row row where to get the value
     * @param col column where to get the value
     * @return the tile id value
     */
    public int getTile(int row, int col) {
        return tileId[row][col];
    }

}
