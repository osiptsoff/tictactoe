package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame;

import io.netty.buffer.ByteBuf;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame.identifier.IdentifierRegistry;

/**
 * Framer which frames message in a way where it is prefixed by unique message type code
 * and message's size in bytes.
 * 
 *        Frame:
 * +----------------+--------------------------+---------+
 * |  length (int)  | message type unique code | message |
 * | self-inclusive |           (long)         |         |
 * +----------------+--------------------------+---------+
 */
public class PrefixFramer implements Framer {

    @Override
    public Object readFramedMessage(ByteBuf inBuf, Serializer serializer) {
        int size = inBuf.readInt() - Integer.BYTES - Long.BYTES;
        Class<?> clazz = IdentifierRegistry.getClass(inBuf.readLong());
        byte[] bytes = new byte[size];
        inBuf.readBytes(bytes);
        
        return serializer.deserialize(bytes, clazz);
    }

    @Override
    public void writeFramedMessage(ByteBuf outBuf, Serializer serializer, Object message) {
        byte[] serialized = serializer.serialize(message);
        outBuf.writeInt(Integer.BYTES + Long.BYTES + serialized.length);
        outBuf.writeLong(IdentifierRegistry.getIdentifier(message));
        outBuf.writeBytes(serialized);
    }
    
}
