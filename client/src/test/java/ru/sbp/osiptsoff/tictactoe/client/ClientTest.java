package ru.sbp.osiptsoff.tictactoe.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ru.sbp.osiptsoff.tictactoe.config.ClientConfig;
import ru.sbp.osiptsoff.tictactoe.config.YamlConfigLoader;

class ClientTest {

    @Test
    @Disabled("working server is needed")
    // todo mock server
    void clientStarts() {
        ClientConfig config = (ClientConfig) (new YamlConfigLoader().load(ClientConfig.class));

        Client client = new Client(config);

        assertDoesNotThrow(() -> {
            try {
                client.start();
            } finally {
                client.stop();
            }
        });
    }
}
