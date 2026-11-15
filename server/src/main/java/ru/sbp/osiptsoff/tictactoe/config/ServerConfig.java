package ru.sbp.osiptsoff.tictactoe.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerConfig extends PeerConfig {
    /**
     * server-specific config section
     */
    private Server server;

    public ServerConfig(Thread thread, Port port, Server server) {
        super(thread, port);
        this.server = server;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Server {
        /**
         * max number of clients who can access server at the same time
         */
        private int maxClients;

        /**
         * max number of lobbies
         */
        private int maxLobbies;
    }
}
