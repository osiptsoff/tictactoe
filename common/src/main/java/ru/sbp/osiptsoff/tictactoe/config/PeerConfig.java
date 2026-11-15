package ru.sbp.osiptsoff.tictactoe.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PeerConfig implements Config {
    /**
     * threads config section
     */
    private Thread thread;

    /**
     * port config section
     */
    private Port port;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Thread {
        /**
         * number of connection management threads
         */
        private int boss;

        /**
         * number of reader-writer threads
         */
        private int worker;

        /**
         * number of business-logic threads 
         */
        private int processor;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Port {
        /**
         * port of event exchange
         */
        private int event;

        /**
         * port of data exchange
         */
        private int data;
    }
}
