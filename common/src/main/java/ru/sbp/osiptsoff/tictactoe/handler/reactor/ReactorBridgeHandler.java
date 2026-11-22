package ru.sbp.osiptsoff.tictactoe.handler.reactor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import reactor.core.Disposable;
import ru.sbp.osiptsoff.tictactoe.message.Message;
import ru.sbp.osiptsoff.tictactoe.message.event.AuthPerformed;
import ru.sbp.osiptsoff.tictactoe.model.ClientId;
import ru.sbp.osiptsoff.tictactoe.reactor.DuplexRegistry;
import ru.sbp.osiptsoff.tictactoe.reactor.Pipe;

/**
 * Emits inbound messages to sink, accepts messages
 * from sink and writes them on wire, building a bridge
 * between netty and reactor.
 */
@RequiredArgsConstructor
public class ReactorBridgeHandler extends ChannelInboundHandlerAdapter {
    private final DuplexRegistry duplexRegistry;

    private ClientId clientId;
    private Pipe pipe;
    private Disposable subscription;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!Message.class.isAssignableFrom(msg.getClass())) {
            ctx.fireChannelRead(msg);
            return;
        }
        if (pipe == null) {
            return;
        }

        Message message = (Message) msg;
        pipe.send(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        deregisterDuplex();
        ctx.fireChannelInactive();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (!AuthPerformed.class.isAssignableFrom(evt.getClass())) {
            ctx.fireUserEventTriggered(evt);
            return;
        }

        AuthPerformed authPerformed = (AuthPerformed) evt;
        registerDuplex(ctx, authPerformed.getClientId());
        ctx.fireUserEventTriggered(evt);
    }

    private void registerDuplex(ChannelHandlerContext ctx, ClientId clientId) {
        this.clientId = clientId;
        pipe = duplexRegistry.registerClient(clientId);
        subscription = pipe.incoming()
            .ofType(Message.class)
            .subscribe(
                evt -> ctx.channel().writeAndFlush(evt),
                // logic-side errors, they will not be sent here, and 
                // netty should not know about them
                err -> {}
            );
    }

    private void deregisterDuplex() {
        subscription.dispose();
        duplexRegistry.deregisterClient(clientId);
    }
}
