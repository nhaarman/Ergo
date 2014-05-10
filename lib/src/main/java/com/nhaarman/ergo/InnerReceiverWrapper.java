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

import android.os.Bundle;

/**
 * An ErgoReceiver which handles communication with the ErgoActivity for you,
 * and figures out whether the task has finished successfully.
 */
class InnerReceiverWrapper<T> implements InnerReceiver {

    private final ErgoHelper mErgoHelper;
    private final ErgoReceiver<T> mErgoReceiver;

    /**
     * Creates a new SimpleReceiver.
     * @param ergoHelper the containing ErgoHelper.
     */
    protected InnerReceiverWrapper(final ErgoHelper ergoHelper, final ErgoReceiver<T> ergoReceiver) {
        mErgoHelper = ergoHelper;
        mErgoReceiver = ergoReceiver;
    }

    /**
     * Notifies the ErgoHelper it's finished, and dispatches the success or exception to the ErgoReceiver.
     * @param resultCode Arbitrary result code delivered by the sender, as
     * defined by the sender. Must be one of {@link com.nhaarman.ergo.ErgoService#RESULT_OK} and {@link com.nhaarman.ergo.ErgoService#RESULT_EXCEPTION}.
     * @param resultData Any additional data provided by the sender. Must contain a T under key {@link com.nhaarman.ergo.ErgoService#EXTRA_RESULT} if successful,
     *                   or an Exception under key {@link com.nhaarman.ergo.ErgoService#EXTRA_EXCEPTION}.
     * @throws java.lang.IllegalArgumentException if an invalid result code was given.
     */
    @Override
    public final void onReceiveResult(final int resultCode, final Bundle resultData) {
        mErgoHelper.onFinishedForClass(mErgoReceiver.getClass());
        if (ErgoService.isSuccessFul(resultCode)) {
            T result = (T) resultData.getSerializable(ErgoService.EXTRA_RESULT);
            mErgoReceiver.onSuccess(result);
        } else if (ErgoService.isException(resultCode)) {
            Exception e = ErgoService.getException(resultData);
            mErgoReceiver.onException(e);
        } else {
            throw new IllegalArgumentException("Invalid result code: " + resultCode);
        }
    }
}