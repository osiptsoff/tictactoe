package ru.sbp.osiptsoff.tictactoe.handler.codec.frame.identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a transportable message that should be registered
 * in the IdentifierRegistry. This makes class applicable for sending by wire.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Transportable {
    
}
