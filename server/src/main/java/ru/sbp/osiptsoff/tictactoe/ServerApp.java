package ru.sbp.osiptsoff.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sbp.osiptsoff.tictactoe.config.ConfigLoader;
import ru.sbp.osiptsoff.tictactoe.config.ServerConfig;
import ru.sbp.osiptsoff.tictactoe.config.YamlConfigLoader;
import ru.sbp.osiptsoff.tictactoe.server.Server;

public class ServerApp {
    private static final Logger LOG = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) throws InterruptedException {
        ConfigLoader configLoader = new YamlConfigLoader();
        ServerConfig serverConfig = (ServerConfig) configLoader.load(ServerConfig.class);

        LOG.info("config read");
        LOG.info("starting server on ports {}, {}", 
                serverConfig.getPort().getEvent(), serverConfig.getPort().getData());

        Server server = new Server(serverConfig);
        try {
            server.start();
            LOG.info("server ready");
            server.sync();
        } finally {
            server.stop();
            LOG.info("server stopped");
        }
    }
}
