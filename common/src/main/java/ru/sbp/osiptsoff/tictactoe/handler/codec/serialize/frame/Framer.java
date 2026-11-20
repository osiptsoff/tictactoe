package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame;

import io.netty.buffer.ByteBuf;

/**
 * Object responsible for framing and providing unambigous way to distinguish
 * payload type on wire. 
 */
public interface Framer {
    /**
     * Find type and deframe bytes to pure payload.
     * Sets reader index to position from which message payload starts.
     * 
     * @param inBuf buffer to read data from
     * 
     * @return information which allows deframing
     */
    DeframingResult deframe(ByteBuf inoutBuf);

    /**
     * Frame message in a way that further deframing will
     * be able unambigously distinguish its type
     * Modifies buffer content.
     * Sets writer index to position from which message payload can be written.
     * 
     * @param outBuf buffer to write data to
     * @param message message
     * @param serializedMessageSize message size in bytes after serialization
     */
    void frame(ByteBuf inoutBuf, Object message, int serializedMessageSize);

    public record DeframingResult(Class<?> messageType, int messageLength) { }
}
