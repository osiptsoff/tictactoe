package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame;

import io.netty.buffer.ByteBuf;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame.identifier.IdentifierRegistry;

/**
 * Framer which frames message in a way where it is prefixed by unique message type code.
 * 
 *        Frame:
 * +--------------------------+---------+
 * | message type unique code | message |
 * |           (long)         |         |
 * +--------------------------+---------+
 */
public class PrefixFramer implements Framer {

    @Override
    public DeframingResult deframe(ByteBuf inoutBuf) {
        Class<?> clazz = IdentifierRegistry.getClass(inoutBuf.readLong());
        
        return new DeframingResult(clazz, inoutBuf.readableBytes());
    }

    @Override
    public void frame(ByteBuf inoutBuf, Object message, int serializedMessageSize) {
        inoutBuf.writeLong(IdentifierRegistry.getIdentifier(message));
    }
    
}
