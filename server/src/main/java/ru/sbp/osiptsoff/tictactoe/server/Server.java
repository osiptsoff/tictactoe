package ru.sbp.osiptsoff.tictactoe.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.config.ServerConfig;
import ru.sbp.osiptsoff.tictactoe.peer.AbstractPeer;
import ru.sbp.osiptsoff.tictactoe.server.handler.DataChannelInitializer;
import ru.sbp.osiptsoff.tictactoe.server.handler.EventChannelInitializer;

@RequiredArgsConstructor
public class Server extends AbstractPeer {
    @Getter(onMethod = @__(@Override))
    private final ServerConfig config;

    @Override
    protected ChannelFuture setupEventChannel() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap
            .group(getThreads().getBossGroup(), getThreads().getWorkerGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new EventChannelInitializer(this))
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

        return bootstrap.bind(getConfig().getPort().getEvent());
    }

    @Override
    protected ChannelFuture setupDataChannel() {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
            .group(getThreads().getWorkerGroup())
            .channel(NioDatagramChannel.class)
            .handler(new DataChannelInitializer(this));
        
        return bootstrap.bind(getConfig().getPort().getData());
    }
}
