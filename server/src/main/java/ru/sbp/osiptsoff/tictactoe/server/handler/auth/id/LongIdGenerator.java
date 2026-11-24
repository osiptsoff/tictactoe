package ru.sbp.osiptsoff.tictactoe.server.handler.auth.id;

import java.util.concurrent.atomic.AtomicLong;

import ru.sbp.osiptsoff.tictactoe.model.ClientId;

/**
 * Generates IDs by taking monotonously increasing Long values.
 * It is worth noting that Long has max value.
 */
public class LongIdGenerator implements IdGenerator {
    private final AtomicLong previousId = new AtomicLong(-1l);

    @Override
    public ClientId newId() {
        return new ClientId(String.valueOf(previousId.incrementAndGet()));
    }
    
}
