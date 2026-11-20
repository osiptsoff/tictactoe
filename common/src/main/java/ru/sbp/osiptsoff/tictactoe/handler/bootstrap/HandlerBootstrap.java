package ru.sbp.osiptsoff.tictactoe.handler.bootstrap;

import io.netty.channel.ChannelPipeline;

/**
 * Adds default set of handlers to pipeline.
 */
public interface HandlerBootstrap {
    /**
     * Add default set of handlers.
     * 
     * @param pipeline pipeline to bootstrap
     * 
     * @return same pipeline for chaining
     */
    ChannelPipeline bootstrap(ChannelPipeline pipeline);
}
