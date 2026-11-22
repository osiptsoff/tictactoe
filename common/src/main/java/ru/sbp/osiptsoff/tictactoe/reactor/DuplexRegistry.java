package ru.sbp.osiptsoff.tictactoe.reactor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.sbp.osiptsoff.tictactoe.model.ClientId;

/**
 * Peer's registry of all connected peers.
 */
@RequiredArgsConstructor
public class DuplexRegistry {
    private final Map<ClientId, Duplex> registry = new ConcurrentHashMap<>();

    private final Sinks.Many<Pipe> registerEvents = Sinks.many().unicast().onBackpressureError();
    private final Sinks.Many<ClientId> deregisterEvents = Sinks.many().unicast().onBackpressureError();

    /**
     * Create reactor's duplex for channel and memorize it.
     * 
     * @param channel new channel
     */
    public Pipe registerClient(ClientId clientId) {
        if (registry.get(clientId) != null) {
            
        }

        Duplex duplex = new Duplex();

        registry.put(clientId, duplex);
        registerEvents.tryEmitNext(duplex.getLogicSide());
        return duplex.getHandlerSide();
    }

    /**
     * Close reactor's duplex for channel and forget it.
     * 
     * @param channel closing channel
     */
    public void deregisterClient(ClientId clientId) {
        Duplex duplex = registry.get(clientId);
        duplex.close();

        registry.remove(clientId);
        deregisterEvents.tryEmitNext(clientId);
    }

    /**
     * @return flux of register events
     */
    public Flux<Pipe> getRegisterEvents() {
        return registerEvents.asFlux();
    }

    /**
     * @return flux of deregister events
     */
    public Flux<ClientId> getDeregisterEvents() {
        return deregisterEvents.asFlux();
    }
}
