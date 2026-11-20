package ru.sbp.osiptsoff.tictactoe.handler.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.Framer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;

/**
 * Fecodes message from wire.
 */
@RequiredArgsConstructor 
public final class Decoder extends ByteToMessageDecoder {
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
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Framer.DeframingResult deframingResult = framer.deframe(in);

        byte[] encodedMessage = new byte[deframingResult.messageLength()];
        in.readBytes(encodedMessage);
        Object message = serializer.deserialize(encodedMessage, deframingResult.messageType());

        out.add(message);
    }
}
