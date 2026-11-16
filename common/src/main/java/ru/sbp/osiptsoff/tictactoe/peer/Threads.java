package ru.sbp.osiptsoff.tictactoe.peer;

import java.util.concurrent.ExecutorService;

import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Peer threads
 */
@RequiredArgsConstructor
@Getter 
public class Threads {
    /**
     * peer connection managementthreads
     */
    private final EventLoopGroup bossGroup;

    /**
     * peer io threads
     */
    private final EventLoopGroup workerGroup;

    /**
     * peer business logic threads
     */
    private final ExecutorService processorGroup;

    /**
     * shuts all threads down
     */
    public void shutdown() {
        getBossGroup().shutdownGracefully();
        getWorkerGroup().shutdownGracefully();
        getProcessorGroup().shutdown();
    }
}
