package com.nhaarman.ergo;

import android.os.Bundle;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class InnerResultReceiverTest extends TestCase {

    private InnerResultReceiver mInnerResultReceiver;

    @Mock
    private InnerReceiver mInnerReceiver;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test the dispatching.
     */
    public void testOnReceiveResult() {
        mInnerResultReceiver = new InnerResultReceiver(null, mInnerReceiver);

        Bundle bundle = new Bundle();
        mInnerResultReceiver.onReceiveResult(0, bundle);

        verify(mInnerReceiver).onReceiveResult(0, bundle);
    }

    /**
     * Test no dispatching when the InnerReceiver hasn't been set.
     */
    public void testResultReceiverNotSet() {
        mInnerResultReceiver = new InnerResultReceiver(null, null);

        Bundle bundle = new Bundle();
        mInnerResultReceiver.onReceiveResult(0, bundle);

        verify(mInnerReceiver, never()).onReceiveResult(anyInt(), any(Bundle.class));
    }

    /**
     * Test dispatching when the InnerReceiver has been set after construction.
     */
    public void testSetResultReceiver() {
        mInnerResultReceiver = new InnerResultReceiver(null, null);
        mInnerResultReceiver.setReceiver(mInnerReceiver);

        Bundle bundle = new Bundle();
        mInnerResultReceiver.onReceiveResult(0, bundle);

        verify(mInnerReceiver).onReceiveResult(0, bundle);
    }

}