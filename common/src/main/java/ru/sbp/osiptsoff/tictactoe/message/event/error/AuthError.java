package ru.sbp.osiptsoff.tictactoe.message.event.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.handler.codec.frame.identifier.Transportable;

/**
 * Access error.
 */
@Transportable
@RequiredArgsConstructor 
@Getter
public class AuthError implements Error {
    private final Type type;
    
    /**
     * Error type.
     */
    public enum Type {
        /**
         * Client was not registered
         */
        UNAUTHENTICATED,

        /**
         * Connection prohibited because max
         * number of client connected
         */
        NO_MORE_ALLOWED,

        /**
         * Provided cridentials are rejected
         */
        WRONG_CREDENTIALS,

        /**
         * Authorized client requested authorization
         */
        AUTH_REQUEST_DUPLICATED,
    }
}
