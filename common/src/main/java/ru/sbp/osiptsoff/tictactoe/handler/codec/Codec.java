package ru.sbp.osiptsoff.tictactoe.handler.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.Framer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;
import ru.sbp.osiptsoff.tictactoe.message.Message;

/**
 * Decodes/encodes message from/for  wire.
 * Expects message to be framed in separate handler for decoding!
 */
@RequiredArgsConstructor
public final class Codec extends ByteToMessageCodec<Message> {
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

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Framer.DeframingResult deframingResult = framer.deframe(in);

        byte[] encodedMessage = new byte[deframingResult.messageLength()];
        in.readBytes(encodedMessage);
        Object message = serializer.deserialize(encodedMessage, deframingResult.messageType());

        out.add(message);
    }
    
}
