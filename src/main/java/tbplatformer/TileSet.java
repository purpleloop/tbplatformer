package tbplatformer;

import java.awt.image.BufferedImage;

/** Models a tileset. */
public class TileSet {

    /** Tile set size. */
    private static final int TILE_SIZE = 32;

    /** Available tiles to build a level map. */
    private Tile[][] tiles;

    /** Number of tiles per line in the image. */
    private int numTilesAcross;

    /**
     * Creates a tileSet from a given image.
     * 
     * @param tileSetImage the source image
     */
    public TileSet(BufferedImage tileSetImage) {

        numTilesAcross = (tileSetImage.getWidth() + 1) / (TILE_SIZE + 1);
        tiles = new Tile[2][numTilesAcross];

        BufferedImage subimage;
        for (int col = 0; col < numTilesAcross; col++) {

            // Get non blocking tiles images
            subimage = tileSetImage.getSubimage(col * TILE_SIZE + col, 0, TILE_SIZE, TILE_SIZE);
            tiles[0][col] = new Tile(subimage, false);

            // Get blocking tiles images
            subimage = tileSetImage.getSubimage(col * TILE_SIZE + col, TILE_SIZE + 1, TILE_SIZE,
                    TILE_SIZE);
            tiles[1][col] = new Tile(subimage, true);
        }

    }

    /** @return the tile size */
    public int getTileSize() {
        return TILE_SIZE;
    }

    /**
     * Get the tile for a given tile index.
     * 
     * @param tileIndex the tile index
     * @return the requested tile
     */
    public Tile getTileByIndex(int tileIndex) {
        int spriteRow = tileIndex / numTilesAcross;
        int spriteColumn = tileIndex % numTilesAcross;

        return tiles[spriteRow][spriteColumn];
    }

}
