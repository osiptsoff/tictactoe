package ru.sbp.osiptsoff.tictactoe.handler.bootstrap;

import io.netty.channel.ChannelPipeline;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.reactor.ReactorBridgeHandler;
import ru.sbp.osiptsoff.tictactoe.reactor.DuplexRegistry;

/**
 * Adds all handlers necessary to bridge netty and reactor.
 */
@RequiredArgsConstructor
public class ReactorBridgeBootstrap implements HandlerBootstrap {
    private final DuplexRegistry duplexRegistry;

    @Override
    public ChannelPipeline bootstrap(ChannelPipeline pipeline) {
        return pipeline
            .addLast(new ReactorBridgeHandler(duplexRegistry));
    }
    
}
