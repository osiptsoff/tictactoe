package ru.sbp.osiptsoff.tictactoe.reactor;

import java.io.Closeable;

import lombok.Getter;
import reactor.core.publisher.Sinks;
import ru.sbp.osiptsoff.tictactoe.message.Message;

/**
 * This class represents a point of connection between netty handlers
 * and reactor-based logic.
 * 
 * Netty's reactor bridge handler subscribes to logicToHandler sink
 * and writes data emitted from business logic on wire;
 * Business logic consumes data from handlerToLogic sink, this data is
 * produced by bridge handler from data read from wire.
 */
public final class Duplex implements Closeable {
    private final Sinks.Many<Message> handlerToLogic = Sinks.many().unicast().onBackpressureBuffer();
    private final Sinks.Many<Message> logicToHandler = Sinks.many().unicast().onBackpressureBuffer();

    @Getter
    private final Pipe handlerSide = new Pipe(logicToHandler, handlerToLogic);
    @Getter 
    private final Pipe logicSide = new Pipe(handlerToLogic, logicToHandler);

    @Override
    public void close() {
        handlerToLogic.tryEmitComplete();
        logicToHandler.tryEmitComplete();
    }
}
