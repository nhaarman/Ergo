package com.nhaarman.ergo;

import android.os.Bundle;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@SuppressWarnings("NewExceptionWithoutArguments")
public class InnerReceiverWrapperTest extends TestCase {

    private static final String RESULT_STRING = "result";
    private static final Exception RESULT_EXCEPTION = new Exception();

    private InnerReceiverWrapper<String> mInnerReceiverWrapper;

    private Bundle mResultIntent;

    @Mock
    private ErgoHelper mErgoHelper;

    @Mock
    private ErgoReceiver<String> mErgoReceiver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        mInnerReceiverWrapper = new InnerReceiverWrapper<>(mErgoHelper, mErgoReceiver);
        mResultIntent = new Bundle();
        mResultIntent.putString(ErgoService.EXTRA_RESULT, RESULT_STRING);
        mResultIntent.putSerializable(ErgoService.EXTRA_EXCEPTION, RESULT_EXCEPTION);
    }

    /**
     * Test whether the InnerReceiverWrapper notifies the ErgoHelper that it's finished.
     */
    public void testFinishedCalled() {
        mInnerReceiverWrapper.onReceiveResult(ErgoService.RESULT_OK, mResultIntent);
        verify(mErgoHelper).onFinishedForClass(mErgoReceiver.getClass());
    }

    /**
     * Test whether the InnerReceiverWrapper correctly handles a success result by notifying the ErgoReceiver.
     */
    public void testSuccess() {
        mInnerReceiverWrapper.onReceiveResult(ErgoService.RESULT_OK, mResultIntent);
        verify(mErgoReceiver).onSuccess(RESULT_STRING);
    }

    /**
     * Test whether the InnerReceiverWrapper correctly handles an Exception result by notifying the ErgoReceiver.
     */
    public void testException() {
        mInnerReceiverWrapper.onReceiveResult(ErgoService.RESULT_EXCEPTION, mResultIntent);
        verify(mErgoReceiver).onException(RESULT_EXCEPTION);
    }

    public void testInvalidResultCode() {
        try {
            mInnerReceiverWrapper.onReceiveResult(Integer.MAX_VALUE, mResultIntent);
            Assert.fail("Missing exception");
        } catch (IllegalArgumentException e) {
            /* Success */
            verify(mErgoReceiver, never()).onSuccess(any(String.class));
            verify(mErgoReceiver, never()).onException(any(Exception.class));
        }
    }
}