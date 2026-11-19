package ru.sbp.osiptsoff.tictactoe.server.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.codec.Codec;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.ProtobufSerializer;
import ru.sbp.osiptsoff.tictactoe.handler.codec.serialize.frame.PrefixFramer;
import ru.sbp.osiptsoff.tictactoe.server.Server;

/**
 * Server event channel initializer.
 */
@RequiredArgsConstructor 
public class EventChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final Server server;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
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
