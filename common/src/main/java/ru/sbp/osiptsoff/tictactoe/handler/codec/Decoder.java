package ru.sbp.osiptsoff.tictactoe.handler.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AccessLevel;
import lombok.Getter;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;

/**
 * Decodes message from wire.
 * Does not process fragmentation - this must be handled separately.
 */
public class Decoder extends ByteToMessageDecoder {
    @Getter(AccessLevel.PRIVATE)
    private final Serializer serializer = new ProtobufSerializer();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decode'");
    }
    
}
