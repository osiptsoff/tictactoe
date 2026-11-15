package ru.sbp.osiptsoff.tictactoe.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.server.Server;

/**
 * Server data channel initializer.
 */
@RequiredArgsConstructor 
public class DataChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final Server server;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // todo
    }
    
}
