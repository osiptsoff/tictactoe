package ru.sbp.osiptsoff.tictactoe.model;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Id of client. Guaranteed to be HashMap-compatible.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class ClientId {
    private final String id;
}
