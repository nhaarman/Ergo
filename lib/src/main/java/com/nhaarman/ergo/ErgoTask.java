package com.nhaarman.ergo;

import android.os.ResultReceiver;

/**
 * A class for executing tasks, with success and exception callbacks.
 */
@SuppressWarnings({"ProhibitedExceptionDeclared", "ProhibitedExceptionThrown"})
public abstract class ErgoTask {

    public void execute() {
        try {
            call();
            onSuccess();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            onException(e);
        }
    }

    public abstract void setResultReceiver(ResultReceiver resultReceiver);

    protected abstract void call() throws Exception;

    protected abstract void onSuccess();

    protected abstract void onException(Exception e);

}
