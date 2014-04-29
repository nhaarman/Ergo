package com.nhaarman.ergo;

import android.os.ResultReceiver;

/**
 * A class for executing tasks, with success and exception callbacks.
 * @param <T> the return type.
 */
@SuppressWarnings({"ProhibitedExceptionDeclared", "ProhibitedExceptionThrown"})
public abstract class ErgoTask<T> {

    public void execute() {
        try {
            T result = call();
            onSuccess(result);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            onException(e);
        }
    }

    public abstract void setResultReceiver(ResultReceiver resultReceiver);

    protected abstract T call() throws Exception;

    protected abstract void onSuccess(T result);

    protected abstract void onException(Exception e);

}
