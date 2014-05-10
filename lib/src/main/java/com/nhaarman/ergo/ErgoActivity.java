/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.ergo;

import android.app.Activity;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * An Activity class that handles saved states for ErgoResultReceivers.
 * Users implementing this class should override {@link #onCreateReceivers()} to provide their callbacks.
 * After that, an {@link InnerResultReceiver} can be gained by calling {@link #createResultReceiverForClass(Class)},
 * which can be used for {@link com.nhaarman.ergo.ErgoService}.
 */
@SuppressWarnings("rawtypes")
public class ErgoActivity extends Activity {

    private final ErgoHelper mErgoHelper = new ErgoHelper();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRegisterReceivers();
    }

    /**
     * Called after {@link #onCreate(android.os.Bundle)}.
     * Override this method to register your {@link com.nhaarman.ergo.ErgoReceiver}s using {@link #registerReceiver(ErgoReceiver)}.
     */
    protected void onRegisterReceivers() {
        Log.w("Ergo", "ErgoActivity.onRegisterReceivers() not overridden or super.onRegisterReceivers() called. Override this method to register your ErgoReceivers!"); //NON-NLS
    }

    /**
     * Registers an ErgoReceiver. Only one instance per ErgoReceiver class can be registered.
     * @param ergoReceiver the ErgoReceiver to register.
     * @throws IllegalArgumentException if given ErgoReceiver class has already been registered.
     */
    public void registerReceiver(final ErgoReceiver<?> ergoReceiver) {
        mErgoHelper.registerReceiver(ergoReceiver);
    }

    /**
     * Unregisters an ErgoReceiver.
     * It is not necessary to call this method upon end-of-life events.
     * @param ergoReceiver the ErgoReceiver to unregister.
     */
    public void unregisterReceiver(final ErgoReceiver<?> ergoReceiver) {
        mErgoHelper.unregisterReceiver(ergoReceiver);
    }

    /**
     * Creates a new {@link InnerResultReceiver} for given class.
     * An instance of given class should have been registered using {@link #registerReceiver(ErgoReceiver)} ()}, or an exception is thrown.
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
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        mErgoHelper.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        mErgoHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
