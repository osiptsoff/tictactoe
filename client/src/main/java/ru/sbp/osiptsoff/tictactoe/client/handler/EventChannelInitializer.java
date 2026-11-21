package ru.sbp.osiptsoff.tictactoe.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.Client;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.BootstrapChain;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.IdleStateBootstrap;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.ReactorBridgeBootstrap;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.codec.TcpProtobufCodecBootstrap;

/**
 * Client event channel initializer.
 */
@RequiredArgsConstructor 
public class EventChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final Client client;

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        new BootstrapChain()
            .forPipeline(ch.pipeline())
            .withBootstrap(new IdleStateBootstrap(
                client.getConfig().getIdleSeconds().getEvent().getRead(),
                client.getConfig().getIdleSeconds().getEvent().getWrite(),
                null))
            .withBootstrap(new TcpProtobufCodecBootstrap())
            .withBootstrap(new ReactorBridgeBootstrap(client.getDuplexRegistry()))
            .bootstrap();
    }
    
}
