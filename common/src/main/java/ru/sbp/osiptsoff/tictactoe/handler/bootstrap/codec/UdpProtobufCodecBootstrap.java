package ru.sbp.osiptsoff.tictactoe.handler.bootstrap.codec;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DatagramPacketDecoder;
import io.netty.handler.codec.DatagramPacketEncoder;
import ru.sbp.osiptsoff.tictactoe.handler.bootstrap.HandlerBootstrap;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Decoder;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Encoder;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.Framer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.PrefixFramer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.Serializer;

/**
 * Adds all handlers necessary for decoding/encoding.
 * For UDP.
 */
public class UdpProtobufCodecBootstrap implements HandlerBootstrap {
    private final Serializer serializer = new ProtobufSerializer();
    private final Framer framer = new PrefixFramer();

    @Override
    public ChannelPipeline bootstrap(ChannelPipeline pipeline) {
        // todo
        return pipeline;
    }
    
}
