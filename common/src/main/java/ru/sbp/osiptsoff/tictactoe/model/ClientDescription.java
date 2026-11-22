package ru.sbp.osiptsoff.tictactoe.model;

import java.net.SocketAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Some info describing client
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter 
public class ClientDescription {
    private SocketAddress socketAddress;
    private String name;
}
