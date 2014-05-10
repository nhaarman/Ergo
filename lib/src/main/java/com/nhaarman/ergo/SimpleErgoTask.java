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
import android.os.ResultReceiver;

import java.io.Serializable;

/**
 * An ErgoTask which delivers results to a ResultReceiver.
 * @param <T> the return type.
 */
public abstract class SimpleErgoTask<T extends Serializable> extends ErgoTask<T> {

    private ResultReceiver mResultReceiver;

    /**
     * Sets the ResultReceiver which is notified. Must be called before {@link #execute()}, or an {@link java.lang.IllegalArgumentException} is thrown.
     */
    @Override
    public void setResultReceiver(final ResultReceiver resultReceiver) {
        mResultReceiver = resultReceiver;
    }

    @Override
    public void execute() {
        if (mResultReceiver == null) {
            throw new IllegalArgumentException("Provide a ResultReceiver!");
        }
        super.execute();
    }

    /**
     * Notifies the ResultReceiver of the success result, using {@link com.nhaarman.ergo.ErgoService#EXTRA_RESULT}.
     */
    @Override
    protected void onSuccess(final T result) {
        Bundle resultData = new Bundle();
        resultData.putSerializable(ErgoService.EXTRA_RESULT, result);
        mResultReceiver.send(ErgoService.RESULT_OK, resultData);
    }

    /**
     * Notifies the ResultReceiver of the thrown Exception, using {@link com.nhaarman.ergo.ErgoService#EXTRA_EXCEPTION}.
     */
    @Override
    protected void onException(final Exception e) {
        Bundle resultData = new Bundle();
        resultData.putSerializable(ErgoService.EXTRA_EXCEPTION, e);
        mResultReceiver.send(ErgoService.RESULT_EXCEPTION, resultData);
    }
}