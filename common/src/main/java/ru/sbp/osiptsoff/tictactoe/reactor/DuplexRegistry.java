package ru.sbp.osiptsoff.tictactoe.reactor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Sinks;

/**
 * Peer's registry of all connected peers.
 */
@RequiredArgsConstructor
public class DuplexRegistry {
    private final Map<ChannelId, Duplex> registry = new ConcurrentHashMap<>();

    private final Sinks.Many<Pipe> registerEvents;
    private final Sinks.Many<Pipe> deregisterEvents;

    /**
     * Create reactor's duplex for channel and memorize it.
     * 
     * @param channel new channel
     */
    public Pipe registerChannel(Channel channel) {
        Duplex duplex = new Duplex();

        // todo add player id
        registry.put(channel.id(), duplex);
        registerEvents.tryEmitNext(duplex.getLogicSide());
        return duplex.getHandlerSide();
    }

    /**
     * Close reactor's duplex for channel and forget it.
     * 
     * @param channel closing channel
     */
    public void deregisterChannel(Channel channel) {
        Duplex duplex = registry.get(channel.id());
        duplex.close();

        registry.remove(channel.id());
        // todo emit player id
        deregisterEvents.tryEmitNext(duplex.getLogicSide());
    }
}
