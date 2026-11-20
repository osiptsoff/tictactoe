package ru.sbp.osiptsoff.tictactoe.handler.bootstrap;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Codec;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame.PrefixFramer;

/**
 * Adds all handlers necessary for decoding/encoding.
 * Applies length-based frame decoder and protobuff codec.
 */
public class ProtobufCodecBootstrap implements HandlerBootstrap {

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
            .addLast(new Codec(new ProtobufSerializer(), new PrefixFramer()));
    }
    
}
