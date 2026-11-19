package ru.sbp.osiptsoff.tictactoe.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.Client;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Codec;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame.PrefixFramer;

/**
 * Client event channel initializer.
 */
@RequiredArgsConstructor 
public class EventChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final Client client;

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
            .addLast(new LengthFieldBasedFrameDecoder(
                1024 * 16,
                0,
                Integer.BYTES,
                -Integer.BYTES,
                0
            ))
            .addLast(new Codec(new ProtobufSerializer(), new PrefixFramer()));
    }
    
}
