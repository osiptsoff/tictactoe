package ru.sbp.osiptsoff.tictactoe.handler.reactor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import reactor.core.Disposable;
import ru.sbp.osiptsoff.tictactoe.message.Message;
import ru.sbp.osiptsoff.tictactoe.reactor.DuplexRegistry;
import ru.sbp.osiptsoff.tictactoe.reactor.Pipe;

@RequiredArgsConstructor
public class ReactorBridgeHandler extends ChannelInboundHandlerAdapter {
    private final DuplexRegistry duplexRegistry;

    private Pipe pipe;
    private Disposable subscription;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!Message.class.isAssignableFrom(msg.getClass())) {
            ctx.fireChannelRead(msg);
        }
        Message message = (Message) msg;
        pipe.send(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        pipe = duplexRegistry.registerChannel(ctx.channel());

        subscription = pipe.incoming()
            .ofType(Message.class)
            .subscribe(
                evt -> ctx.channel().writeAndFlush(evt),
                // logic-side errors, they will not be sent here, and 
                // netty should not know about them
                err -> {}
            );

        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        subscription.dispose();
        duplexRegistry.deregisterChannel(ctx.channel());
        ctx.fireChannelInactive();
    }
}
