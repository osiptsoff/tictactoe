package ru.sbp.osiptsoff.tictactoe.server.handler.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import ru.sbp.osiptsoff.tictactoe.model.ClientDescription;
import ru.sbp.osiptsoff.tictactoe.model.ClientId;
import ru.sbp.osiptsoff.tictactoe.server.handler.auth.id.IdGenerator;

/**
 * Stores and keeps info about registered clients
 */
@RequiredArgsConstructor
public class AuthRegistry {
    private final Map<ClientId, ClientDescription> registry = new ConcurrentHashMap<>();

    private final IdGenerator idGenerator;

    /**
     * @param clientId client id
     * 
     * @return client info
     */
    public ClientDescription get(ClientId clientId) {
        return registry.get(clientId);
    }

    /**
     * Assign new id to client and save info
     * 
     * @param clientDescription client info
     * 
     * @return new id
     */
    public ClientId register(ClientDescription clientDescription) {
        ClientId clientId = idGenerator.newId();

        registry.put(clientId, clientDescription);

        return clientId;
    }

    /**
     * @param clientId client id
     */
    public void deregister(ClientId clientId) {
        registry.remove(clientId);
    }
}
