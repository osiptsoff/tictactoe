package ru.sbp.osiptsoff.tictactoe.client;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Callback which is executed when request to server is done.
 */
public abstract class ConnectionCallback {
    /**
     * @param resultFuture action on succes
     */
    public abstract void onSuccess(Future<? super Void> resultFuture);

    /**
     * @param errorFuture action on error
     */
    public abstract void onError(Future<? super Void> errorFuture);

    /**
     * @return bridge to netty listener
     */
    protected final GenericFutureListener<? extends Future<? super Void>> toListener() {
        return e -> {
            if (e.isSuccess()) {
                onSuccess(e);
            } else {
                onError(e);
            }
        };
    }
}
