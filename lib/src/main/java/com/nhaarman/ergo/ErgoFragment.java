package com.nhaarman.ergo;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;


/**
 * A Fragment class that handles saved states for ErgoResultReceivers.
 * Users implementing this class should override {@link #onRegisterErgoReceivers()} to provide their callbacks.
 * After that, an {@link InnerResultReceiver} can be gained by calling {@link #createResultReceiverForClass(Class)},
 * which can be used for {@link com.nhaarman.ergo.ErgoService}.
 */
@SuppressWarnings("rawtypes")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ErgoFragment extends Fragment {

    private final ErgoHelper mErgoHelper = new ErgoHelper();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRegisterErgoReceivers();
    }

    /**
     * Called after {@link #onCreate(android.os.Bundle)}.
     * Override this method to register your {@link com.nhaarman.ergo.ErgoReceiver}s using {@link #registerErgoReceiver(ErgoReceiver)}.
     */
    protected void onRegisterErgoReceivers() {
        Log.w("Ergo", "ErgoFragment.onRegisterErgoReceivers() not overridden or super.onRegisterErgoReceivers() called. Override this method to register your ErgoReceivers!"); //NON-NLS
    }

    /**
     * Registers an ErgoReceiver. Only one instance per ErgoReceiver class can be registered.
     * @param ergoReceiver the ErgoReceiver to register.
     * @throws IllegalArgumentException if given ErgoReceiver class has already been registered.
     */
    public void registerErgoReceiver(final ErgoReceiver ergoReceiver) {
        mErgoHelper.registerErgoReceiver(ergoReceiver);
    }

    /**
     * Unregisters an ErgoReceiver.
     * It is not necessary to call this method upon end-of-life events.
     * @param ergoReceiver the ErgoReceiver to unregister.
     */
    public void unregisterErgoReceiver(final ErgoReceiver ergoReceiver) {
        mErgoHelper.unregisterErgoReceiver(ergoReceiver);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mErgoHelper.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Creates a new {@link InnerResultReceiver} for given class.
     * An instance of given class should have been registered using {@link #onRegisterErgoReceivers()}, or an exception is thrown.
     * That instance will be the callback class for the ErgoResultReceiver returned.
     * @return an ErgoResultReceiver with the instance for given class as callback.
     */
    protected ResultReceiver createResultReceiverForClass(final Class<? extends ErgoReceiver> receiverClass) {
        return mErgoHelper.createResultReceiverForClass(receiverClass);
    }

    /**
     * Returns whether the task for given class is still active.
     * @return true if it is active.
     */
    protected boolean isActive(final Class<? extends ErgoReceiver> receiverClass) {
        return mErgoHelper.isActive(receiverClass);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        mErgoHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
