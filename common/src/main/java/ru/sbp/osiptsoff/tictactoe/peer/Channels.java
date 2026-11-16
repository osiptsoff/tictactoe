package ru.sbp.osiptsoff.tictactoe.peer;


import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Channels {
    /**
     * channel for event exchange
     */
    private final Channel eventChannel;

    /**
     * channel for data exchange
     */
    private final Channel dataChannel;

    /**
     * request channels close
     */
    public void close() {
        getEventChannel().close();
        getDataChannel().close();
    }
}
