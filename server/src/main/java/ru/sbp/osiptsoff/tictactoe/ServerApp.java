package ru.sbp.osiptsoff.tictactoe;

import ru.sbp.osiptsoff.tictactoe.config.ConfigLoader;
import ru.sbp.osiptsoff.tictactoe.config.ServerConfig;
import ru.sbp.osiptsoff.tictactoe.config.YamlConfigLoader;
import ru.sbp.osiptsoff.tictactoe.server.Server;

public class ServerApp 
{
    public static void main(String[] args) throws InterruptedException {
        ConfigLoader configLoader = new YamlConfigLoader();
        ServerConfig serverConfig = (ServerConfig) configLoader.load(ServerConfig.class);

        Server server = new Server(serverConfig);
        try {
            server.start();
            server.sync();
        } finally {
            server.stop();
        }
    }
}
