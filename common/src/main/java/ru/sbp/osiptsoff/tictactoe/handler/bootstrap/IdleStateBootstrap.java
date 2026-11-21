package ru.sbp.osiptsoff.tictactoe.handler.bootstrap;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;

/**
 * Adds handlers which will control channel idleness, 
 * close channel when nothing was read/written for a certain
 * amount of time, and do specified actions.
 */
@RequiredArgsConstructor
public class IdleStateBootstrap implements HandlerBootstrap {
    /**
     * seconds, 0 to disable
     */
    private final int readTimeout;

    /**
     * seconds, 0 to disable
     */
    private final int writeTimeout;

    private final BeforeChannelClose beforeChannelClose;

    @Override
    public ChannelPipeline bootstrap(ChannelPipeline pipeline) {
        if (pipeline.get(IdleStateHandler.class) != null) {
            return pipeline;
        }
        return pipeline
            .addLast(new IdleStateHandler(readTimeout, writeTimeout, 0))
            .addLast(new IdleStateProcessorHandler());
    }

    @FunctionalInterface
    public interface BeforeChannelClose extends Function<Channel, ChannelFuture> {

    }

    private class IdleStateProcessorHandler extends ChannelDuplexHandler {
        private static final Logger LOG = LoggerFactory.getLogger(IdleStateProcessorHandler.class);
        private static final AttributeKey<Boolean> CLOSING =
            AttributeKey.valueOf(IdleStateBootstrap.class, "closing");

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            Channel ch = ctx.channel();

            // events other than IdleStateEvent do not interest us here
            if (!(evt instanceof IdleStateEvent)) {
                ctx.fireUserEventTriggered(evt);
                return;
            }
            // we're closing this channel already
            if (Boolean.TRUE.equals(ch.attr(CLOSING).get())) {
                return;
            }
            ch.attr(CLOSING).set(true);

            processEvent((IdleStateEvent) evt, ch);
            initiateChannelClose(ch);
        }

        private void processEvent(IdleStateEvent e, Channel ch) {
            switch (e.state()) {
                case READER_IDLE:
                    LOG.info("closing channel '{}' due to idle reader", ch.remoteAddress());
                    break;
                case WRITER_IDLE:
                    LOG.info("closing channel '{}' due to idle writer", ch.remoteAddress());
                    break;
                default:
            }
        }

        private void initiateChannelClose(Channel ch) {
            ChannelFuture f = null;
            if (beforeChannelClose != null) {
                try {
                    f = beforeChannelClose.apply(ch);
                } catch (Exception ex) {
                    LOG.warn("beforeChannelClose callback failed", ex);
                    f = null;
                }
            }
            if (f != null) {
                f.addListener(_ -> ch.close());
            } else {
                ch.close();
            }
        }
    }
}
