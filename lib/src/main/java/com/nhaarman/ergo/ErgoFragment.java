package com.nhaarman.ergo;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;


/**
 * A Fragment class that handles saved states for ErgoResultReceivers.
 * Users implementing this class should override {@link #onCreateReceivers()} to provide their callbacks.
 * After that, an {@link InnerResultReceiver} can be gained by calling {@link #createResultReceiverForClass(Class)},
 * which can be used for {@link com.nhaarman.ergo.ErgoService}.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ErgoFragment extends Fragment {

    private final ErgoHelper mErgoHelper = new ErgoHelper();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErgoHelper.setErgoReceivers(onCreateReceivers());
    }

    /**
     * Override this method to return your callback classes.
     * @return an array of {@link InnerReceiverWrapper}s, which will be used for callbacks.
     */
    protected ErgoReceiver<?>[] onCreateReceivers() {
        return new ErgoReceiver[0];
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mErgoHelper.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Creates a new {@link InnerResultReceiver} for given class.
     * An instance of given class should have been returned in {@link #onCreateReceivers()}, or an exception is thrown.
     * That instance will be the callback class for the ErgoResultReceiver returned.
     * @return an ErgoResultReceiver with the instance for given class as callback.
     */
    protected ResultReceiver createResultReceiverForClass(final Class<? extends ErgoReceiver<?>> receiverClass) {
        return mErgoHelper.createResultReceiverForClass(receiverClass);
    }

    /**
     * Returns whether the task for given class is still active.
     * @return true if it is active.
     */
    protected boolean isActive(final Class<? extends ErgoReceiver<?>> receiverClass) {
        return mErgoHelper.isActive(receiverClass);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        mErgoHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
