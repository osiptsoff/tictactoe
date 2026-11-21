package ru.sbp.osiptsoff.tictactoe.reactor;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;
import ru.sbp.osiptsoff.tictactoe.message.Message;

/**
 * Think of this class as of gate: one way is inbound traffic, one way is outbound traffic,
 * like you stand in front of them.
 * Which way leads where is not this class' concern.
 */
@RequiredArgsConstructor
public final class Pipe {
    private final Sinks.Many<Message> inbound;
    private final Sinks.Many<Message> outbound;

    /**
     * @return flux of inbound side trafic
     */
    public Flux<Message> incoming() {
        return inbound.asFlux();
    }

    /**
     * @param message message to send to outbound side
     * 
     * @return result
     */
    public Mono<EmitResult> send(Message message) {
        return Mono.fromCallable(() -> outbound.tryEmitNext(message));
    }
}
