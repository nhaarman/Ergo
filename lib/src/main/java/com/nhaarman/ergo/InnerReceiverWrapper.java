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
class InnerReceiverWrapper implements InnerReceiver {

    private final ErgoHelper mErgoHelper;
    private final ErgoReceiver mErgoReceiver;

    /**
     * Creates a new SimpleReceiver.
     *
     * @param ergoHelper the containing ErgoHelper.
     */
    protected InnerReceiverWrapper(final ErgoHelper ergoHelper, final ErgoReceiver ergoReceiver) {
        mErgoHelper = ergoHelper;
        mErgoReceiver = ergoReceiver;
    }

    @Override
    public final void onReceiveResult(final int resultCode, final Bundle resultData) {
        mErgoHelper.onFinishedForClass(mErgoReceiver.getClass());
        if (ErgoService.isSuccessFul(resultCode)) {
            mErgoReceiver.onSuccess();
        } else {
            Exception e = ErgoService.getException(resultData);
            mErgoReceiver.onException(e);
        }
    }
}