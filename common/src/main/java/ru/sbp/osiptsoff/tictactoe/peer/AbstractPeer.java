package ru.sbp.osiptsoff.tictactoe.peer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.sbp.osiptsoff.tictactoe.config.PeerConfig;

/**
 * Basic class representing tic-tac-toe client or server
 */
public abstract class AbstractPeer {
    /**
     * logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPeer.class);

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    private Threads threads;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    private Channels channels;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private boolean isRunning;

    /**
     * start peer
     */
    public final synchronized void start() {
        LOG.debug("peer start requested");
        if (isRunning()) {
            LOG.debug("peer already active, start aborted");
            return;
        }

        setThreads(newThreads());
        setChannels(newChannels());

        setRunning(true);
        LOG.info("peer started");
    }

    /**
     * stop peer
     */
    public final synchronized void stop() {
        LOG.debug("peer stop requested");
        if (!isRunning()) {
            LOG.debug("peer is not active, stop aborted");
            return;
        }

        getChannels().close();
        LOG.debug("channels closed");
        getThreads().shutdown();
        LOG.debug("threads shut");

        setRunning(false);
        LOG.info("peer stopped");
    }

    /**
     * wait for peer to close channels
     * 
     * @throws InterruptedException if wait interrupted
     */
    public final void sync() throws InterruptedException {
        if (!isRunning()) {
            return;
        }

        getChannels().getEventChannel().channel().closeFuture().sync();
        getChannels().getDataChannel().channel().closeFuture().sync();
    }

    /**
     * @return config
     */
    public abstract PeerConfig getConfig();

    /**
     * @return event channel
     */
    protected abstract ChannelFuture setupEventChannel();

    /**
     * @return data channel
     */
    protected abstract ChannelFuture setupDataChannel();

    /**
     * @return channels
     */
    private Channels newChannels() {
        ChannelFuture eventChannel = setupEventChannel();
        ChannelFuture dataChannel = setupDataChannel();

        try {
            eventChannel.sync();
            LOG.debug("event channel set up");
            dataChannel.sync();
            LOG.debug("data channel set up");
        } catch (InterruptedException ie) {
            LOG.debug("channel setup wait interrupted", ie);
            Thread.currentThread().interrupt();
        }

        // todo will we have an infinitw loop since stop must call close? find out
        eventChannel.channel().closeFuture().addListener(_ -> stop());
        dataChannel.channel().closeFuture().addListener(_ -> stop());

        return new Channels(eventChannel, dataChannel);
    }

    /**
     * @return new threads
     */
    private Threads newThreads() {
        EventLoopGroup bossGroup = new MultiThreadIoEventLoopGroup(
                getConfig().getThread().getBoss(), NioIoHandler.newFactory());
        EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(
                getConfig().getThread().getWorker(), NioIoHandler.newFactory());
        ExecutorService processorGroup = Executors.newFixedThreadPool(
                getConfig().getThread().getProcessor());
        return new Threads(bossGroup, workerGroup, processorGroup);
    }
}
