package com.nhaarman.ergo;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper class that handles saved states for ErgoResultReceivers.
 * Users of this class should register ErgoReceivers using {@link #registerErgoReceiver(ErgoReceiver)} to provide their callbacks.
 * After that, an {@link InnerResultReceiver} can be gained by calling {@link #createResultReceiverForClass(Class)},
 * which can be used for {@link com.nhaarman.ergo.ErgoService}.
 */
@SuppressWarnings("rawtypes")
public class ErgoHelper {

    /**
     * The registered ErgoReceivers, wrapped in an InnerReceiverWrapper.
     * Key: ErgoReceiver.getClass().getName().
     */
    private final Map<String, InnerReceiverWrapper<?>> mReceiverMap = new HashMap<>(3);

    /**
     * The active InnerResultReceivers, which are returned from {@link #createResultReceiverForClass(Class)}.
     * Key: ErgoReceiver.getClass().getName().
     */
    private final Map<String, InnerResultReceiver> mResultReceiverMap = new HashMap<>(3);

    private final Handler mHandler = new Handler();

    /**
     * Registers an ErgoReceiver. Only one instance per ErgoReceiver class can be registered.
     * @param ergoReceiver the ErgoReceiver to register.
     * @throws IllegalArgumentException if given ErgoReceiver class has already been registered.
     */
    public void registerErgoReceiver(final ErgoReceiver<?> ergoReceiver) {
        if (mReceiverMap.containsKey(ergoReceiver.getClass().getName())) {
            throw new IllegalArgumentException("ErgoReceiver " + ergoReceiver.getClass().getName() + " has already been registered!");
        }
        mReceiverMap.put(ergoReceiver.getClass().getName(), wrap(ergoReceiver));
    }

    /**
     * Unregisters an ErgoReceiver.
     * It is not necessary to call this method upon end-of-life events.
     * @param ergoReceiver the ErgoReceiver to unregister.
     */
    public void unregisterErgoReceiver(final ErgoReceiver<?> ergoReceiver) {
        mReceiverMap.remove(ergoReceiver.getClass().getName());
    }

    private <T> InnerReceiverWrapper<T> wrap(final ErgoReceiver<T> receiver) {
        return new InnerReceiverWrapper<>(this, receiver);
    }

    /**
     * Creates a new {@link InnerResultReceiver} for given class.
     * An instance of given class should have been registered in {@link #registerErgoReceiver(ErgoReceiver)}, or an exception is thrown.
     * That instance will be the callback class for the ErgoResultReceiver returned.
     * @return an ErgoResultReceiver with the instance for given class as callback.
     */
    protected ResultReceiver createResultReceiverForClass(final Class<? extends ErgoReceiver> receiverClass) {
        InnerResultReceiver resultReceiver = new InnerResultReceiver(mHandler, mReceiverMap.get(receiverClass.getName()));
        mResultReceiverMap.put(receiverClass.getName(), resultReceiver);
        return resultReceiver;
    }

    /**
     * Notifies this ErgoHelper that the task for given class has finished. After calling this method, {@link #isActive(Class)} will return false.
     * Users should generally not call this method, as {@link InnerReceiverWrapper} does that for you.
     */
    protected void onFinishedForClass(final Class<? extends ErgoReceiver> receiverClass) {
        mResultReceiverMap.remove(receiverClass.getName());
    }

    /**
     * Returns whether the task for given class is still active.
     * @return true if it is active.
     */
    protected boolean isActive(final Class<? extends ErgoReceiver> receiverClass) {
        return mResultReceiverMap.containsKey(receiverClass.getName());
    }

    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (Map.Entry<String, InnerReceiverWrapper<?>> entry : mReceiverMap.entrySet()) {
                InnerResultReceiver resultReceiver = savedInstanceState.getParcelable(entry.getKey());
                if (resultReceiver != null) {
                    mResultReceiverMap.put(entry.getKey(), resultReceiver);
                    resultReceiver.setReceiver(entry.getValue());
                }
            }
        }
    }

    protected void onSaveInstanceState(final Bundle outState) {
        for (Map.Entry<String, InnerResultReceiver> entry : mResultReceiverMap.entrySet()) {
            outState.putParcelable(entry.getKey(), entry.getValue());
        }
    }
}
