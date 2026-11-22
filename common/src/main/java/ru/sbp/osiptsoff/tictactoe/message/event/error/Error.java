package ru.sbp.osiptsoff.tictactoe.message.event.error;

import ru.sbp.osiptsoff.tictactoe.message.event.Event;

/**
 * Marker interface for errors (like auth error),
 * which travel through event channel
 */
public interface Error extends Event {
    
}
