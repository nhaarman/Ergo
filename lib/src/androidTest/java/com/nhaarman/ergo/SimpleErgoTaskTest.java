package com.nhaarman.ergo;

import android.os.Bundle;
import android.os.ResultReceiver;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@SuppressWarnings({"NewExceptionWithoutArguments", "ProhibitedExceptionDeclared", "ProhibitedExceptionThrown"})
public class SimpleErgoTaskTest extends TestCase {

    private static final String RESULT_STRING = "result";
    private static final Exception RESULT_EXCEPTION = new Exception();
    private static final RuntimeException RESULT_RUNTIMEEXCEPTION = new RuntimeException();

    private SimpleErgoTask<String> mSuccessTask;
    private SimpleErgoTask<String> mExceptionTask;
    private SimpleErgoTask<String> mRuntimeExceptionTask;

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


    private static class SuccessTestErgoTask extends SimpleErgoTask<String> {

        @Override
        protected String call() {
            return RESULT_STRING;
        }
    }

    private static class ExceptionTestErgoTask extends SimpleErgoTask<String> {

        @Override
        protected String call() throws Exception {
            throw RESULT_EXCEPTION;
        }
    }

    private static class RuntimeExceptionTestErgoTask extends SimpleErgoTask<String> {

        @Override
        protected String call() {
            throw RESULT_RUNTIMEEXCEPTION;
        }
    }
}