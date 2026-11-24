package ru.sbp.osiptsoff.tictactoe.server.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.BootstrapChain;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.IdleStateBootstrap;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.ReactorBridgeBootstrap;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.codec.TcpProtobufCodecBootstrap;
import ru.sbp.osiptsoff.tictactoe.server.Server;
import ru.sbp.osiptsoff.tictactoe.server.handler.auth.AuthHandler;

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
            .withBootstrap(new IdleStateBootstrap(
                server.getConfig().getIdleSeconds().getEvent().getRead(),
                server.getConfig().getIdleSeconds().getEvent().getWrite(),
                null))
            .withBootstrap(new TcpProtobufCodecBootstrap())
            .withHandler(new AuthHandler(server.getAuthRegistry(), server.getConfig().getServer().getCredentials()))
            .withBootstrap(new ReactorBridgeBootstrap(server.getDuplexRegistry()))
            .bootstrap();
    }
    
}
