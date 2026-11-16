package ru.sbp.osiptsoff.tictactoe.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.Client;

/**
 * Client data channel initializer.
 */
@RequiredArgsConstructor
public class DataChannelInitializer extends ChannelInitializer<NioDatagramChannel> {
    private final Client client;

    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
        // todo
    }
    
}
