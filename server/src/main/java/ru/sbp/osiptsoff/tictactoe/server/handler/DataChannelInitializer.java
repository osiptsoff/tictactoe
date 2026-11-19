package ru.sbp.osiptsoff.tictactoe.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Codec;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame.PrefixFramer;
import ru.sbp.osiptsoff.tictactoe.server.Server;

/**
 * Server data channel initializer.
 */
@RequiredArgsConstructor 
public class DataChannelInitializer extends ChannelInitializer<NioDatagramChannel> {
    private final Server server;

    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
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
