package ru.sbp.osiptsoff.tictactoe.server.handler.auth.id;

import ru.sbp.osiptsoff.tictactoe.model.ClientId;

/**
 * Generates id for clients.
 */
public interface IdGenerator {
    /**
     * @return new clientId, guaranteed to not be taken by registered client
     */
    ClientId newId();
}
