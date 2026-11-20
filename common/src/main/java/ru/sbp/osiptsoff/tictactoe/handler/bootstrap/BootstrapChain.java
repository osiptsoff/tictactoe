package ru.sbp.osiptsoff.tictactoe.handler.bootstrap;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelPipeline;

/**
 * Provides flat interface for handler bootstrap chaining.
 */
public final class BootstrapChain {
    private final List<HandlerBootstrap> bootstraps = new ArrayList<>();

    private ChannelPipeline pipeline;

    /**
     * @param pipeline pipeline to add handlers to
     * 
     * @return this object for chaining
     */
    public BootstrapChain forPipeline(ChannelPipeline pipeline) {
        this.pipeline = pipeline;

        return this;
    }

    /**
     * @param bootstrap additional bootstrap to apply to pipe
     * 
     * @return this object for chaining
     */
    public BootstrapChain withBootstrap(HandlerBootstrap bootstrap) {
        bootstraps.add(bootstrap);

        return this;
    }

    /**
     * @return bootstrapped pipeline
     */
    public ChannelPipeline bootstrap() {
        for (HandlerBootstrap bootstrap : bootstraps) {
            bootstrap.bootstrap(pipeline);
        }

        return pipeline;
    }
}
