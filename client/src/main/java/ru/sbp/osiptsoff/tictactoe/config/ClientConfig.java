package ru.sbp.osiptsoff.tictactoe.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientConfig extends PeerConfig {
    /**
     * client-specific config section
     */
    private Client client;

    public ClientConfig(Thread thread, Port port, Client client) {
        super(thread, port);
        this.client = client;
    }
    
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Client {
        /**
         * server address
         */
        private String host;
    }
}
