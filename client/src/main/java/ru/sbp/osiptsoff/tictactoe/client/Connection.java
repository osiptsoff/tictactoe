package ru.sbp.osiptsoff.tictactoe.client;

import io.netty.channel.ChannelFuture;
import lombok.AllArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.exception.RevokedConnectionException;
import ru.sbp.osiptsoff.tictactoe.message.data.Data;
import ru.sbp.osiptsoff.tictactoe.message.event.Event;
import ru.sbp.osiptsoff.tictactoe.peer.Channels;

/**
 * This class represents client-side connection and is used to send messages to server.
 */
@AllArgsConstructor 
public class Connection {
    private Channels clientChannels;

    /**
     * @param event event to send to server
     * @throws RevokedConnectionException if connection was revoked
     */
    ChannelFuture send(Event event) throws RevokedConnectionException {
        throwIfRevoked();
        return clientChannels.getEventChannel().writeAndFlush(event);
    }

    /**
     * @param data data to send to server
     * @throws RevokedConnectionException if connection was revoked
     */
    ChannelFuture send(Data data) throws RevokedConnectionException {
        throwIfRevoked();
        return clientChannels.getDataChannel().writeAndFlush(data);
    }

    /**
     * makes this connection unusable by owners,
     * it is presumed that Channels are closed by client
     */
    void revoke() {
        clientChannels = null;
    }

    private void throwIfRevoked() throws RevokedConnectionException {
        if (clientChannels == null) {
            throw new RevokedConnectionException("connection revoked");
        }
    }
}
