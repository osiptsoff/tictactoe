package ru.sbp.osiptsoff.tictactoe.client;

import java.util.function.Consumer;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Callback which is executed when request to server is done.
 */
public abstract class ConnectionCallback {
    /**
     * Construct ConnectionCallback from lambdas.
     * 
     * @param onSuccess on success callback
     * @param onError on error callback
     * @param onAnyCase on any case callback
     * 
     * @return ConnectionCallback
     */
    public static ConnectionCallback of(
            Consumer<Future<? super Void>> onSuccess, 
            Consumer<Future<? super Void>> onError,
            Consumer<Future<? super Void>> onAnyCase
    ) {
        return new ConnectionCallback() {
            @Override
            public void onSuccess(Future<? super Void> resultFuture) {
                if (onSuccess != null) {
                    onSuccess.accept(resultFuture);
                }
            }

            @Override
            public void onError(Future<? super Void> errorFuture) {
                if (onError != null) {
                    onError.accept(errorFuture);
                }
            }

            @Override
            public void onAnyCase(Future<? super Void> anyCaseFuture) {
                if (onAnyCase != null) {
                    onAnyCase.accept(anyCaseFuture);
                }
            }
            
        };
    }

    /**
     * Construct ConnectionCallback from lambdas.
     * 
     * @param onSuccess on success callback
     * @param onError on error callback
     * 
     * @return ConnectionCallback
     */
    public static ConnectionCallback of(
        Consumer<Future<? super Void>> onSuccess, 
        Consumer<Future<? super Void>> onError
    ) {
        return of(onSuccess, onError, null);
    }

    /**
     * @param resultFuture action on succes
     */
    public abstract void onSuccess(Future<? super Void> resultFuture);

    /**
     * @param errorFuture action on error
     */
    public abstract void onError(Future<? super Void> errorFuture);

    /**
     * @param anyCaseFuture action on any case
     */
    public abstract void onAnyCase(Future<? super Void> anyCaseFuture);

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
            onAnyCase(e);
        };
    }
}
