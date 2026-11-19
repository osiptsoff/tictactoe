package ru.sbp.osiptsoff.tictactoe.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AccessLevel;
import lombok.Getter;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;
import ru.sbp.osiptsoff.tictactoe.message.Message;

/**
 * Encodes messages to wire.
 */
public class Encoder extends MessageToByteEncoder<Message> {
    @Getter(AccessLevel.PRIVATE)
    private final Serializer serializer = new ProtobufSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        out.writeBytes(getSerializer().serialize(msg));
    }
    
}
