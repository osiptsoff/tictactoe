package ru.sbp.osiptsoff.tictactoe.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.Client;

/**
 * Client event channel initializer.
 */
@RequiredArgsConstructor 
public class EventChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final Client client;

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        // todo
    }
    
}
