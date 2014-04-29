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
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * An Activity class that handles saved states for ErgoResultReceivers.
 * Users implementing this class should override {@link #onCreateReceivers()} to provide their callbacks.
 * After that, an {@link InnerResultReceiver} can be gained by calling {@link #createResultReceiverForClass(Class)},
 * which can be used for {@link com.nhaarman.ergo.ErgoService}.
 */
public class ErgoActivity extends Activity {

    private final Map<String, InnerReceiverWrapper<?>> mReceiverMap = new HashMap<>(3);
    private final Map<String, InnerResultReceiver> mResultReceiverMap = new HashMap<>(3);

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ErgoReceiver<?>[] ergoReceivers = onCreateReceivers();
        for (ErgoReceiver<?> ergoReceiver : ergoReceivers) {
            mReceiverMap.put(ergoReceiver.getClass().getName(), a(ergoReceiver));
        }
    }

    /**
     * Override this method to return your callback classes.
     * @return an array of {@link InnerReceiverWrapper}s, which will be used for callbacks.
     */
    protected ErgoReceiver<?>[] onCreateReceivers() {
        return new ErgoReceiver[0];
    }

    private <T> InnerReceiverWrapper<T> a(final ErgoReceiver<T> receiver) {
        return new InnerReceiverWrapper<>(this, receiver);
    }

    /**
     * Creates a new {@link InnerResultReceiver} for given class.
     * An instance of given class should have been returned in {@link #onCreateReceivers()}, or an exception is thrown.
     * That instance will be the callback class for the ErgoResultReceiver returned.
     * @return an ErgoResultReceiver with the instance for given class as callback.
     */
    protected ResultReceiver createResultReceiverForClass(final Class<? extends ErgoReceiver> receiverClass) {
        InnerResultReceiver resultReceiver = new InnerResultReceiver(mHandler, mReceiverMap.get(receiverClass.getName()));
        mResultReceiverMap.put(receiverClass.getName(), resultReceiver);
        return resultReceiver;
    }

    /**
     * Notifies this ErgoActivity that the task for given class has finished. After calling this method, {@link #isActive(Class)} will return false.
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

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        for (Map.Entry<String, InnerReceiverWrapper<?>> entry : mReceiverMap.entrySet()) {
            InnerResultReceiver resultReceiver = savedInstanceState.getParcelable(entry.getKey());
            if (resultReceiver != null) {
                mResultReceiverMap.put(entry.getKey(), resultReceiver);
                resultReceiver.setReceiver(entry.getValue());
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        for (Map.Entry<String, InnerResultReceiver> entry : mResultReceiverMap.entrySet()) {
            outState.putParcelable(entry.getKey(), entry.getValue());
        }
        super.onSaveInstanceState(outState);
    }

}
