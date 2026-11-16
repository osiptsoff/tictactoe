package ru.sbp.osiptsoff.tictactoe.client;

import org.jspecify.annotations.Nullable;

import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.peer.Channels;

/**
 * This class represents client-side connection and is used to send messages to server.
 */
@RequiredArgsConstructor 
public class Connection {
    private final Channels clientChannels;

    /**
     * register this client on server
     */
    public void register(@Nullable ConnectionCallback callback) {
        // todo
    }
}
