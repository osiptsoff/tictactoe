package ru.sbp.osiptsoff.tictactoe.peer;

import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Channels {
    /**
     * channel for event exchange
     */
    private final ChannelFuture eventChannel;

    /**
     * channel for data exchange
     */
    private final ChannelFuture dataChannel;

    /**
     * request channels close
     */
    public void close() {
        getEventChannel().channel().close();
        getDataChannel().channel().close();
    }
}
