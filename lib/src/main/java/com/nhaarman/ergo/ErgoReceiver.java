package com.nhaarman.ergo;

public interface ErgoReceiver {

    void onSuccess();

    void onException(Exception e);

}
