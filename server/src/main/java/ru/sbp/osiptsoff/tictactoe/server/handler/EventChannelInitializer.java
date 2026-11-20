package ru.sbp.osiptsoff.tictactoe.server.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.BootstrapChain;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.ProtobufCodecBootstrap;
import ru.sbp.osiptsoff.tictactoe.server.Server;

/**
 * Server event channel initializer.
 */
@RequiredArgsConstructor 
public class EventChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final Server server;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        new BootstrapChain()
            .forPipeline(ch.pipeline())
            .withBootstrap(new ProtobufCodecBootstrap())
            .bootstrap();
    }
    
}
