package ru.sbp.osiptsoff.tictactoe.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ru.sbp.osiptsoff.tictactoe.config.ClientConfig;
import ru.sbp.osiptsoff.tictactoe.config.YamlConfigLoader;
import ru.sbp.osiptsoff.tictactoe.message.event.Hello;

// todo mock server
class ClientTest {

    @Test
    @Disabled("working server is needed")
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

    @Test
    @Disabled("working server is needed")
    void clientSendsMessage() {
        ClientConfig config = (ClientConfig) (new YamlConfigLoader().load(ClientConfig.class));

        Client client = new Client(config);

        assertDoesNotThrow(() -> {
            client.start();
            client.getConnection().send(new Hello()).addListener(
                ConnectionCallback.of(
                    e -> System.out.println("success"),
                    e -> System.out.println("fail"),
                    e -> client.stop()
                ).toListener()
            );
            client.sync();
        });
    }
}
