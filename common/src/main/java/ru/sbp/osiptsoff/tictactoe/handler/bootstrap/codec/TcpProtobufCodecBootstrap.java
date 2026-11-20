package ru.sbp.osiptsoff.tictactoe.handler.bootstrap.codec;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.HandlerBootstrap;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Decoder;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Encoder;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.Framer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.PrefixFramer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;

/**
 * Adds all handlers necessary for decoding/encoding.
 * Applies length-based frame decoder and protobuff codec.
 * For TCP.
 */
public class TcpProtobufCodecBootstrap implements HandlerBootstrap {
    private final Serializer serializer = new ProtobufSerializer();
    private final Framer framer = new PrefixFramer();

    @Override
    public ChannelPipeline bootstrap(ChannelPipeline pipeline) {
        return pipeline
            .addLast(new LengthFieldBasedFrameDecoder(
                1024 * 16,
                0,
                Integer.BYTES,
                0,
                Integer.BYTES
            ))
            .addLast(new LengthFieldPrepender(Integer.BYTES))
            .addLast(new Encoder(serializer, framer))
            .addLast(new Decoder(serializer, framer));
    }
    
}
