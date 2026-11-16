package ru.sbp.osiptsoff.tictactoe.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.server.Server;

/**
 * Server data channel initializer.
 */
@RequiredArgsConstructor 
public class DataChannelInitializer extends ChannelInitializer<NioDatagramChannel> {
    private final Server server;

    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
        // todo
    }
    
}
