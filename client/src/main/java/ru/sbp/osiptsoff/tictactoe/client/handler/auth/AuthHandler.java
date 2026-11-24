package ru.sbp.osiptsoff.tictactoe.client.handler.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.ExchangeState;
import ru.sbp.osiptsoff.tictactoe.config.ClientConfig;
import ru.sbp.osiptsoff.tictactoe.message.event.AuthPerformed;
import ru.sbp.osiptsoff.tictactoe.message.event.AuthRequested;
import ru.sbp.osiptsoff.tictactoe.message.event.error.AuthError;
import ru.sbp.osiptsoff.tictactoe.model.ClientDescription;

/**
 * Client-side auth handler.
 * 
 * Intercepts all messages related to auth.
 * Sends auth request when first "unauthorized" got from server.
 * Passes auth errors up to pipeline.
 */
@RequiredArgsConstructor
public class AuthHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthHandler.class);
    private static final int MAX_AUTH_TRIES = 1;

    private final ExchangeState exchangeState;
    private final ClientConfig clientConfig;

    private int authTries = 0;

    @Override 
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (isAuthResult(msg)) {
            processAuthResult(ctx, msg);
        } else if (isAuthError(msg)) {
            processAuthError(ctx, msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        exchangeState.setClientId(null);
        ctx.fireChannelInactive();
    }

    private boolean isAuthResult(Object msg) {
        return AuthPerformed.class.isAssignableFrom(msg.getClass());
    }

    private boolean isAuthError(Object msg) {
        return AuthError.class.isAssignableFrom(msg.getClass());
    }

    private void processAuthResult(ChannelHandlerContext ctx, Object msg) {
        AuthPerformed authPerformed = (AuthPerformed) msg;

        exchangeState.setClientId(authPerformed.getClientId());
    }

    private void processAuthError(ChannelHandlerContext ctx, Object msg) {
        AuthError authError = (AuthError) msg;
        AuthError.Type type = authError.getType();

        switch (type) {
            case UNAUTHENTICATED:
                handleUnauthenticated(ctx, authError);
                break;
            case NO_MORE_ALLOWED:
                // we do nothing here - let top-level logic decide
                ctx.fireChannelRead(msg);
                break;
            case WRONG_CREDENTIALS:
                ctx.fireChannelRead(msg);
                break;
            case AUTH_REQUEST_DUPLICATED:
                // do nothing since we authenticated already
                LOG.warn("we duplicated auth request");
                break;
            default:
                LOG.warn("unknown auth error state");
        }
    }

    private void handleUnauthenticated(ChannelHandlerContext ctx, AuthError authError) {
        if (MAX_AUTH_TRIES <= authTries) {
            LOG.debug("auth to '{}' failed", ctx.channel().remoteAddress());
            ctx.fireChannelRead(authError);
            return;
        }

        authTries++;
        ctx.writeAndFlush(newAuthRequest(ctx));
    }

    private AuthRequested newAuthRequest(ChannelHandlerContext ctx) {
        ClientDescription clientDescription = new ClientDescription(
            ctx.channel().localAddress(),
            clientConfig.getClient().getName()
        );
        String credentials = clientConfig.getClient().getCredentials();

        return new AuthRequested(clientDescription, credentials);
    }
}
