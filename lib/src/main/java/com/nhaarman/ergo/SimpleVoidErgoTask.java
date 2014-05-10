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

/**
 * An ErgoTask which does not return anything, except for a success or exception message.
 * Handles notifying the ResultReceiver.
 */
public abstract class SimpleVoidErgoTask extends ErgoTask<Void> {

    private ResultReceiver mResultReceiver;

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

    @Override
    protected void onSuccess(final Void result) {
        Bundle resultData = new Bundle();
        mResultReceiver.send(ErgoService.RESULT_OK, resultData);
    }

    @Override
    protected void onException(final Exception e) {
        Bundle resultData = new Bundle();
        resultData.putSerializable(ErgoService.EXTRA_EXCEPTION, e);
        mResultReceiver.send(ErgoService.RESULT_EXCEPTION, resultData);
    }
}