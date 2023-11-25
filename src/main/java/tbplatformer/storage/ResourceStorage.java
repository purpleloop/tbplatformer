package tbplatformer.storage;

import tbplatformer.model.LevelMap;

/** Interface for game resources store. */
public interface ResourceStorage {

    /** Get a level map for the given name.
     * @param mapName the map name
     * @return the level map
     */
    LevelMap getMapByName(String mapName);

}
