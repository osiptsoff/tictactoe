package ru.sbp.osiptsoff.tictactoe.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.Framer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;
import ru.sbp.osiptsoff.tictactoe.message.Message;

/**
 * Encodes message for wire.
 */
@RequiredArgsConstructor
public final class Encoder extends MessageToByteEncoder<Message> {
    /**
     * Turns messages into bytes and vice versa.
     */
    private final Serializer serializer;

    /**
     * Frames bytes before writing and hepls serializer to
     * find out exact type of message before reading.
     */
    private final Framer framer;

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] encodedMessage = serializer.serialize(msg);
        framer.frame(out, msg, encodedMessage.length);
        out.writeBytes(encodedMessage);
    }
    
}
