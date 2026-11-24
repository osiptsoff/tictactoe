package ru.sbp.osiptsoff.tictactoe.message.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.model.ClientId;

/**
 * Server registered client.
 */
@RequiredArgsConstructor
@Getter
public class AuthPerformed implements Event {
    private final ClientId clientId;
}
