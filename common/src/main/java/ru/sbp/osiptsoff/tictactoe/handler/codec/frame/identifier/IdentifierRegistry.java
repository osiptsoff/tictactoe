package ru.sbp.osiptsoff.tictactoe.handler.codec.frame.identifier;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sbp.osiptsoff.tictactoe.message.event.AuthPerformed;
import ru.sbp.osiptsoff.tictactoe.message.event.AuthRequested;
import ru.sbp.osiptsoff.tictactoe.message.event.Hello;
import ru.sbp.osiptsoff.tictactoe.message.event.error.AuthError;

/**
 * To decode message from wire we must know what is written in bytes.
 * For this purpose we will assign identifier for each message.
 */
public final class IdentifierRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(IdentifierRegistry.class);

    private static final Map<Class<?>, Long> REGISTRY = new HashMap<>();
    private static final Map<Long, Class<?>> IDENTIFIERS_TO_CLASS = new HashMap<>();

    static {
        // will write all message classes here for now
        // todo replace this with custom annotation and annotation processor
        register(Hello.class);
        register(AuthError.class);
        register(AuthRequested.class);
        register(AuthPerformed.class);
    }

    private IdentifierRegistry() {

    }

    /**
     * @param object object
     * @return identifier of object's class
     */
    public static long getIdentifier(Object object) {
        Long identifier = REGISTRY.get(object.getClass());

        if (identifier == null) {
            LOG.error("requested id for non-registered class");
            throw new IllegalStateException();
        }

        return identifier;
    }

    /**
     * @param identifier identifier
     * @return class corresponding to identifier
     */
    public static Class<?> getClass(long identifier) {
        Class<?> clazz = IDENTIFIERS_TO_CLASS.get(identifier);

        if (clazz == null) {
            LOG.error("requested class for non-registered id");
            throw new IllegalStateException();
        }

        return clazz;
    }

    /**
     * Registers identifier of given class.
     * 
     * @param clazz class
     */
    static void register(Class<?> clazz) {
        long identifier = getIdentifier(clazz);
        if (REGISTRY.containsKey(clazz)) {
            LOG.debug("tried to register already registered class, this should not happen");
            return;
        }
        if (IDENTIFIERS_TO_CLASS.containsKey(identifier)) {
            LOG.error("tried to register already registered identifier for another class, collision detected");
            throw new IllegalStateException();
        }

        REGISTRY.put(clazz, identifier);
        IDENTIFIERS_TO_CLASS.put(identifier, clazz);
    }

    /**
     * Provides identifier for given class. Identifier is deterministic.
     * 
     * @param clazz class
     * @return identifier
     */
    private static long getIdentifier(Class<?> clazz) {
        return clazz.getName().hashCode();
    }
}
