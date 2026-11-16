package ru.sbp.osiptsoff.tictactoe.client.exception;

import java.net.ConnectException;

import lombok.experimental.StandardException;

/**
 * thrown when invalid connection is used
 */
@StandardException
public class RevokedConnectionException extends ConnectException {
    
}

