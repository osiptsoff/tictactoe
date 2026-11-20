package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame;

import io.netty.buffer.ByteBuf;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;
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
    public Object readFramedMessage(ByteBuf inBuf, Serializer serializer) {
        Class<?> clazz = IdentifierRegistry.getClass(inBuf.readLong());
        byte[] bytes = new byte[inBuf.readableBytes()];
        inBuf.readBytes(bytes);
        
        return serializer.deserialize(bytes, clazz);
    }

    @Override
    public void writeFramedMessage(ByteBuf outBuf, Serializer serializer, Object message) {
        byte[] serialized = serializer.serialize(message);
        outBuf.writeLong(IdentifierRegistry.getIdentifier(message));
        outBuf.writeBytes(serialized);
    }
    
}
