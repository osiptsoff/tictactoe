package ru.sbp.osiptsoff.tictactoe.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PeerConfig implements Config {
    /**
     * threads config section
     */
    private final Thread thread;

    /**
     * port config section
     */
    private final Port port;

    @Getter 
    @RequiredArgsConstructor 
    public class Thread {
        /**
         * number of connection management threads
         */
        private final int boss;

        /**
         * number of reader-writer threads
         */
        private final int worker;

        /**
         * number of business-logic threads 
         */
        private final int processor;
    }

    @Getter 
    @RequiredArgsConstructor 
    public class Port {
        /**
         * port of event exchange
         */
        private final int event;

        /**
         * port of data exchange
         */
        private final int data;
    }
}
