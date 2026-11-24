package ru.sbp.osiptsoff.tictactoe.client;

import lombok.Getter;
import lombok.Setter;
import ru.sbp.osiptsoff.tictactoe.model.ClientId;

/**
 * Client information accepted or computed as a result of exchange with server
 */
@Getter
public final class ExchangeState {
    @Setter
    private volatile ClientId clientId;
}
