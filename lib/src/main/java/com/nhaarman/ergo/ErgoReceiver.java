package com.nhaarman.ergo;

public interface ErgoReceiver<T> {

    void onSuccess(T result);

    void onException(Exception e);

}
