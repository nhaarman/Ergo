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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

/**
 * <p>
 *     An IntentService used to perform simple tasks which may yield in a result or an Exception.
 * </p>
 * <p>
 *     To start an instance of (a subclass) of this class, call {@link #startService(android.content.Context, android.content.Intent, android.os.ResultReceiver)}.
 *     Users overriding {@link #onHandleIntent(android.content.Intent)} should call {@code super.onHandleIntent(Intent)}.
 * </p>
 */
public abstract class ErgoService extends IntentService {

    static final int RESULT_OK = 0;
    static final int RESULT_EXCEPTION = -1;

    static final String EXTRA_RESULTRECEIVER = ErgoService.class.getName() + ".extra_resultreceiver";
    static final String EXTRA_RESULT = ErgoService.class.getName() + ".extra_result";
    static final String EXTRA_EXCEPTION = ErgoService.class.getName() + ".extra_exception";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    protected ErgoService(final String name) {
        super(name);
    }

    /**
     * Use this method to start an instance of your Service.
     * If you start a subclass of BaseIntentService without calling this method, an exception is thrown.
     *
     * @param context the Context starting the Service.
     * @param intent the Intent containing the Service to start and possibly some extras.
     * @param resultReceiver the ResultReceiver which will be notified at the end of execution.
     */
    @SuppressWarnings("TypeMayBeWeakened")
    public static void startService(final Context context, final Intent intent, final ResultReceiver resultReceiver) {
        intent.putExtra(EXTRA_RESULTRECEIVER, resultReceiver);
        context.startService(intent);
    }

    /**
     * Returns whether the task has finished successfully.
     * @param resultCode the result code received.
     * @return true if the task has finished successfully.
     */
    public static boolean isSuccessFul(final int resultCode) {
        return resultCode == RESULT_OK;
    }

    /**
     * Returns the Exception that was thrown during execution of the task, if any.
     * @param resultData the result data received.
     * @return the Exception, or {@code null} if none.
     */
    public static Exception getException(final Bundle resultData) {
        return (Exception) resultData.getSerializable(EXTRA_EXCEPTION);
    }


    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param intent The value passed to {@link
     *               android.content.Context#startService(android.content.Intent)}.
     */
    @Override
    protected void onHandleIntent(final Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RESULTRECEIVER);
        if (resultReceiver == null) {
            throw new IllegalArgumentException("Invalid ResultReceiver. Did you call startService(Context, Intent, ResultReceiver)?");
        }

        ErgoTask<?> ergoTask = createTask(intent);
        ergoTask.setResultReceiver(resultReceiver);
        ergoTask.execute();
    }

    /**
     * Called in {@link #onHandleIntent(android.content.Intent)}.
     * Implementations of this class should return the Task which should be executed here.
     *
     * @param intent the Intent containing the Service to start and possibly some extras, as originally passed to {@link #onHandleIntent(android.content.Intent)}.
     */
    protected abstract ErgoTask<?> createTask(final Intent intent);

}
