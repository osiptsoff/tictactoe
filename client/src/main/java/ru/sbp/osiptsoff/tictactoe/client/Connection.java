package ru.sbp.osiptsoff.tictactoe.client;

import org.jspecify.annotations.Nullable;

import lombok.AllArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.client.exception.RevokedConnectionException;
import ru.sbp.osiptsoff.tictactoe.peer.Channels;

/**
 * This class represents client-side connection and is used to send messages to server.
 */
@AllArgsConstructor 
public class Connection {
    private Channels clientChannels;

    /**
     * register this client on server
     */
    public void register(@Nullable ConnectionCallback callback) throws RevokedConnectionException {
        // todo
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
