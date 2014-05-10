package com.nhaarman.ergo;

import android.os.Bundle;
import android.os.ResultReceiver;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings({"NewExceptionWithoutArguments", "ProhibitedExceptionDeclared", "ProhibitedExceptionThrown"})
public class SimpleVoidErgoTaskTest extends TestCase {

    private static final Exception RESULT_EXCEPTION = new Exception();
    private static final RuntimeException RESULT_RUNTIMEEXCEPTION = new RuntimeException();

    private SimpleVoidErgoTask mSuccessTask;
    private SimpleVoidErgoTask mExceptionTask;
    private SimpleVoidErgoTask mRuntimeExceptionTask;

    @Mock
    private ResultReceiver mResultReceiver;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        mSuccessTask = new SuccessTestErgoTask();
        mExceptionTask = new ExceptionTestErgoTask();
        mRuntimeExceptionTask = new RuntimeExceptionTestErgoTask();
    }

    /**
     * Test a successful execution.
     */
    public void testSuccessExecution() {
        mSuccessTask.setResultReceiver(mResultReceiver);
        mSuccessTask.execute();

        verify(mResultReceiver).send(eq(ErgoService.RESULT_OK), any(Bundle.class));
    }

    /**
     * Test an execution which throws an Exception.
     */
    public void testExceptionExecution() {
        mExceptionTask.setResultReceiver(mResultReceiver);
        mExceptionTask.execute();

        verify(mResultReceiver).send(eq(ErgoService.RESULT_EXCEPTION), any(Bundle.class));
    }

    /**
     * Test an execution without setting a ResultReceiver.
     */
    public void testCallRuntimeExceptionThrown() {
        mRuntimeExceptionTask.setResultReceiver(mResultReceiver);
        try {
            mRuntimeExceptionTask.execute();
            Assert.fail("Missing exception!");
        } catch (RuntimeException ignored) {
            /* Success */
            verify(mResultReceiver, never()).send(anyInt(), any(Bundle.class));
        }
    }

    /**
     * Test an execution without setting a ResultReceiver.
     */
    public void testNoResultReceiverSet() {
        try {
            mSuccessTask.execute();
            Assert.fail("Missing exception!");
        } catch (IllegalArgumentException ignored) {
            /* Success */
            verify(mResultReceiver, never()).send(anyInt(), any(Bundle.class));
        }
    }


    private static class SuccessTestErgoTask extends SimpleVoidErgoTask {

        @Override
        protected Void call() {
            return null;
        }
    }

    private static class ExceptionTestErgoTask extends SimpleVoidErgoTask {

        @Override
        protected Void call() throws Exception {
            throw RESULT_EXCEPTION;
        }
    }

    private static class RuntimeExceptionTestErgoTask extends SimpleVoidErgoTask {

        @Override
        protected Void call() {
            throw RESULT_RUNTIMEEXCEPTION;
        }
    }
}