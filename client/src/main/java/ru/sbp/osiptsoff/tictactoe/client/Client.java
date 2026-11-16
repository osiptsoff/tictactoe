package ru.sbp.osiptsoff.tictactoe.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.handler.DataChannelInitializer;
import ru.sbp.osiptsoff.tictactoe.client.handler.EventChannelInitializer;
import ru.sbp.osiptsoff.tictactoe.config.ClientConfig;
import ru.sbp.osiptsoff.tictactoe.peer.AbstractPeer;

@RequiredArgsConstructor
public class Client extends AbstractPeer {
    @Getter(onMethod = @__(@Override))
    private final ClientConfig config;

    @Getter 
    private Connection connection;

    @Override
    public synchronized void start() {
        super.start();
        connection = new Connection(getChannels());
    }

    @Override 
    public synchronized void stop() {
        super.stop();
        connection.revoke();
        connection = null;
    }

    @Override
    protected ChannelFuture setupEventChannel() {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
            .group(getThreads().getWorkerGroup())
            .channel(NioSocketChannel.class)
            .handler(new EventChannelInitializer(this));
        
        return bootstrap.connect(config.getClient().getHost(), config.getPort().getEvent());
    }

    @Override
    protected ChannelFuture setupDataChannel() {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
            .group(getThreads().getWorkerGroup())
            .channel(NioDatagramChannel.class)
            .handler(new DataChannelInitializer(this));

        return bootstrap.connect(config.getClient().getHost(), config.getPort().getData());
    }
    
}
