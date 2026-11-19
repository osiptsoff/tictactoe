package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame;

import io.netty.buffer.ByteBuf;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;

/**
 * Object responsible for framing and providing unambigous way to distinguish
 * payload type on wire. 
 */
public interface Framer {
    /**
     * Read message from frame.
     * 
     * 
     * @param inBuf buffer to read data from
     * @param serializer message serializer
     * 
     * @return message
     */
    Object readFramedMessage(ByteBuf inBuf, Serializer serializer);

    /**
     * Frame and write message in a way it can be unambigously read later.
     * 
     * 
     * @param outBuf buffer to write data to
     * @param serializer message serializer
     * @param message message
     */
    void writeFramedMessage(ByteBuf outBuf, Serializer serializer, Object message);
}
