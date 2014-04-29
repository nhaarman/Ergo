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
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * A ResultReceiver class which relays the call to {@link #onReceiveResult(int, android.os.Bundle)} to an instance of {@link InnerReceiver}.
 * This ResultReceiver can therefore be stored across state changes, and a new Receiver can be set when necessary.
 */
class InnerResultReceiver extends ResultReceiver {

    private InnerReceiver mInnerReceiver;

    /**
     * Create a new MyResultReceiver to receive results. Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     * @param innerReceiver the ErgoReceiver that is notified of the result.
     */
    InnerResultReceiver(final Handler handler, final InnerReceiver innerReceiver) {
        super(handler);
        mInnerReceiver = innerReceiver;
    }

    /**
     * Sets the {@link InnerReceiver} that is notified of the result.
     */
    public void setReceiver(final InnerReceiver innerReceiver) {
        mInnerReceiver = innerReceiver;
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        if (mInnerReceiver != null) {
            mInnerReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
