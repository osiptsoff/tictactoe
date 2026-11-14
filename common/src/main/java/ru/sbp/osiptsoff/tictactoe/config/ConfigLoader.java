package ru.sbp.osiptsoff.tictactoe.config;

/**
 * ConfigLoader is responsible for loading app's config.
 */
public interface ConfigLoader {
    /**
     * Load config.
     * 
     * @param config class
     * @return config
     */
    Config load(Class<? extends Config> clazz);
}
