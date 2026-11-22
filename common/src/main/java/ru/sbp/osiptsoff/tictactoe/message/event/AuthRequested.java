package ru.sbp.osiptsoff.tictactoe.message.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.model.ClientDescription;

/**
 * Client send this to server when they want to gain access
 */
@RequiredArgsConstructor
@Getter
public class AuthRequested implements Event {
    private final ClientDescription clientDescription;
    // may be null when server requires no auth
    private final String credentials;
}
