package ru.sbp.osiptsoff.tictactoe.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ServerConfig extends PeerConfig {
    /**
     * server-specific config section
     */
    private final Server server;

    public ServerConfig(Thread thread, Port port, Server server) {
        super(thread, port);
        this.server = server;
    }

    @Getter
    @RequiredArgsConstructor 
    public class Server {
        /**
         * max number of clients who can access server at the same time
         */
        private final int maxClients;

        /**
         * max number of lobbies
         */
        private final int maxLobbies;
    }
}
