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

    private final ErgoActivity mErgoActivity;
    private final ErgoReceiver<T> mErgoReceiver;

    /**
     * Creates a new SimpleReceiver.
     * @param ergoActivity the containing ErgoActivity.
     */
    protected InnerReceiverWrapper(final ErgoActivity ergoActivity, final ErgoReceiver<T> ergoReceiver) {
        mErgoActivity = ergoActivity;
        mErgoReceiver = ergoReceiver;
    }

    @Override
    public final void onReceiveResult(final int resultCode, final Bundle resultData) {
        mErgoActivity.onFinishedForClass(mErgoReceiver.getClass());
        if (ErgoService.isSuccessFul(resultCode)) {
            T result = (T) resultData.getSerializable(ErgoService.EXTRA_RESULT);
            mErgoReceiver.onSuccess(result);
        } else {
            Exception e = ErgoService.getException(resultData);
            mErgoReceiver.onException(e);
        }
    }
}