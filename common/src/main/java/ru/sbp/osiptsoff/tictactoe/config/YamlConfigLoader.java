package ru.sbp.osiptsoff.tictactoe.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * YamlConfigLoader is responsible for parsing and loading yaml configuration.
 */
public final class YamlConfigLoader implements ConfigLoader {
    private static final String DEFAULT_CONFIG_PATH = "config.yml";

    /**
     * Load config from file, path to which is given in env variable {@code CONFIG_PATH},
     * or local {@code config.yml} if no env variable is set.
     * 
     * @param config class
     * @return config
     */
    @Override
    public Config load(Class<? extends Config> clazz) {
        try (InputStream stream = openConfigFile()) {
            LoaderOptions loaderOptions = new LoaderOptions();

            loaderOptions.setProcessComments(true);

            Yaml yaml = new Yaml(new Constructor(clazz, loaderOptions));
            yaml.setBeanAccess(BeanAccess.FIELD);
            return yaml.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private InputStream openConfigFile() throws IOException {
        // check local fs
        Path local = Paths.get(DEFAULT_CONFIG_PATH);
        if (Files.isRegularFile(local)) {
            return Files.newInputStream(local);
        }

        // check classpath
        InputStream configFile = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH);
        if (configFile != null) {
            return configFile;
        }

        // nothing to be done
        throw new IllegalStateException("no config found");
    }
    
}
