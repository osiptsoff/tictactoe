package ru.sbp.osiptsoff.tictactoe.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

class YamlConfigLoaderTest {

    private static final Path LOCAL_CONFIG = Paths.get("config.yml");

    @BeforeEach
    void setUp() {
        deleteQuietly(LOCAL_CONFIG);
    }

    @AfterEach
    void tearDown() {
        deleteQuietly(LOCAL_CONFIG);
    }

    @Test
    void loadsFromLocalFile() throws IOException {
        Files.writeString(LOCAL_CONFIG, "value: from-local");

        TestConfig config = (TestConfig) new YamlConfigLoader().load(TestConfig.class);

        assertThat(config.value).isEqualTo("from-local");
    }

    @Test
    void loadsFromClasspathWhenNoLocalFile() {
        TestConfig config = (TestConfig) new YamlConfigLoader().load(TestConfig.class);

        assertThat(config.value).isEqualTo("from-classpath");
    }

    @Test
    void propagatesYamlExceptionForMalformedConfig() throws IOException {
        Files.writeString(LOCAL_CONFIG, "value: [unclosed");

        YamlConfigLoader loader = new YamlConfigLoader();

        assertThatThrownBy(() -> loader.load(TestConfig.class))
                .isInstanceOf(YAMLException.class);
    }

    @Test
    void throwsWhenNoConfigFound() throws Exception {
        URL classesUrl = YamlConfigLoader.class.getProtectionDomain()
                .getCodeSource().getLocation();
        URL snakeYamlUrl = org.yaml.snakeyaml.Yaml.class.getProtectionDomain()
                .getCodeSource().getLocation();

        try (URLClassLoader isolated =
                     new URLClassLoader(new URL[]{classesUrl, snakeYamlUrl}, null)) {

            Class<?> loaderClass = isolated.loadClass(YamlConfigLoader.class.getName());
            Class<?> configClass = isolated.loadClass(Config.class.getName());
            Object instance = loaderClass.getDeclaredConstructor().newInstance();
            Method load = loaderClass.getMethod("load", Class.class);

            Throwable thrown = catchThrowable(() -> load.invoke(instance, configClass));

            assertThat(thrown)
                    .isInstanceOf(InvocationTargetException.class)
                    .cause()
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("no config found");
        }
    }

    private static void deleteQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException _) {
            // best-effort cleanup
        }
    }

    public static class TestConfig implements Config {
        private String value;
    }
}