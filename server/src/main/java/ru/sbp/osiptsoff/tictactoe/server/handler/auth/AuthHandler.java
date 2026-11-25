package ru.sbp.osiptsoff.tictactoe.server.handler.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.message.Message;
import ru.sbp.osiptsoff.tictactoe.message.event.AuthPerformed;
import ru.sbp.osiptsoff.tictactoe.message.event.AuthRequested;
import ru.sbp.osiptsoff.tictactoe.message.event.error.AuthError;
import ru.sbp.osiptsoff.tictactoe.model.ClientDescription;
import ru.sbp.osiptsoff.tictactoe.model.ClientId;

/**
 * Server-side auth handler.
 * Does not let events through if channel is not associated
 * with client id in server registry. Supports simple credential validation.
 */
@RequiredArgsConstructor
public class AuthHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthHandler.class);
    private static final AttributeKey<ClientId> CLIENT_ID =
        AttributeKey.valueOf(ClientId.class, "clientId");

    private final AuthRegistry authRegistry;
    private final String credentials;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel channel = ctx.channel();

        if (!Message.class.isAssignableFrom(msg.getClass())) {
            LOG.info("dropped unknown message");
        } else if (isAuthorized(channel)) {
            processAuthorized(ctx, msg);
        } else {
            processUnauthorized(ctx, msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ClientId id = ctx.channel().attr(CLIENT_ID).get();
        if (id != null) {
            authRegistry.deregister(id);
        }
        ctx.fireChannelInactive();
    }

    private void processAuthorized(ChannelHandlerContext ctx, Object msg) {
        if (isAuthRequest(msg)) {
            deny(ctx, AuthError.Type.AUTH_REQUEST_DUPLICATED);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private void processUnauthorized(ChannelHandlerContext ctx, Object msg) {
        if (isAuthRequest(msg)) {
            Optional<ClientId> clientIdOptional = authorize(ctx.channel(), msg);
            if (clientIdOptional.isPresent()) {
                AuthPerformed authPerformed = new AuthPerformed(clientIdOptional.get());
                ctx.channel().writeAndFlush(authPerformed);
                ctx.fireUserEventTriggered(authPerformed);
            } else {
                deny(ctx, AuthError.Type.WRONG_CREDENTIALS);
            }
        } else {
            deny(ctx, AuthError.Type.UNAUTHENTICATED);
        }
    }

    private boolean isAuthRequest(Object msg) {
        return AuthRequested.class.isAssignableFrom(msg.getClass());
    }

    private void deny(ChannelHandlerContext ctx, AuthError.Type reason) {
        LOG.info("request from '{}' denied by reason of '{}'", ctx.channel().remoteAddress(), reason);
        ctx.channel().writeAndFlush(new AuthError(reason));
    }

    private boolean isAuthorized(Channel channel) {
        ClientId id = channel.attr(CLIENT_ID).get();
        return id != null && authRegistry.get(id) != null;
    }

    private Optional<ClientId> authorize(Channel channel, Object obj) {
        AuthRequested authRequested = (AuthRequested) obj;

        if (isCredentialsNeeded() && !isCredentialsCorrect(authRequested.getCredentials())) {
            return Optional.empty();
        }

        ClientDescription description = authRequested.getClientDescription();
        description.setSocketAddress(channel.remoteAddress());

        ClientId clientId = authRegistry.register(description);
        channel.attr(CLIENT_ID).set(clientId);
        
        return Optional.of(clientId);
    }

    private boolean isCredentialsNeeded() {
        return credentials != null && !credentials.isEmpty();
    }

    private boolean isCredentialsCorrect(String clientCredentials) {
        return credentials.equals(clientCredentials);
    }
}
