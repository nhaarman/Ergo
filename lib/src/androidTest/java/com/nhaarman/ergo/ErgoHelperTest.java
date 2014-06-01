package com.nhaarman.ergo;

import android.os.Bundle;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.*;

public class ErgoHelperTest extends TestCase {

    private ErgoHelper mErgoHelper;

    @Mock
    private ErgoReceiver<String> mErgoReceiver;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        mErgoHelper = new ErgoHelper();
    }

    public void testActiveErgoReceiver() {
        mErgoHelper.registerErgoReceiver(mErgoReceiver);
        mErgoHelper.createResultReceiverForClass(mErgoReceiver.getClass());

        Assert.assertTrue(mErgoHelper.isActive(mErgoReceiver.getClass()));
    }

    public void testFinishErgoReceiver() {
        mErgoHelper.registerErgoReceiver(mErgoReceiver);
        mErgoHelper.createResultReceiverForClass(mErgoReceiver.getClass());
        mErgoHelper.onFinishedForClass(mErgoReceiver.getClass());

        Assert.assertFalse(mErgoHelper.isActive(mErgoReceiver.getClass()));
    }

    public void testRestore() {
        mErgoHelper.registerErgoReceiver(mErgoReceiver);
        mErgoHelper.createResultReceiverForClass(mErgoReceiver.getClass());

        Bundle bundle = new Bundle();
        mErgoHelper.onSaveInstanceState(bundle);

        ErgoHelper ergoHelper = new ErgoHelper();
        ergoHelper.registerErgoReceiver(mErgoReceiver);
        ergoHelper.onRestoreInstanceState(bundle);

        Assert.assertTrue(ergoHelper.isActive(mErgoReceiver.getClass()));
    }
}