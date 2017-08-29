package com.devup.qcm.sdk.managers;

import java.util.ArrayList;

import android.os.Handler;

import com.devup.qcm.sdk.CopySheet;
import com.devup.qcm.sdk.Exercise;
import com.devup.qcm.sdk.Questionnaire;
import com.devup.qcm.sdk.Test;
import com.devup.qcm.sdk.CopySheet.CopySheetAuthor;
import com.devup.qcm.sdk.Qcm.Proposition;
import com.devup.qcm.sdk.utils.QuestionnaireUtils;
//import com.dns.qcmmaker.sdk.process.QcmProcessor;
//import com.dns.qcmmaker.sdk.process.QcmProcessor.CopySheetPostCallBack;

public class TestManager {
    final static int INTERVAL_COUNT = 1000;
    private TestStateListener mStateListener;
    private Test mTest;
    Handler mHandler = new Handler();
    private int testTime = 0;
    private boolean run = false, indefinite = false, aborted,
            sendToRemoteURLEnable = false;
    CopySheetAuthor author;
    //QcmProcessor mProcessor;

    public TestManager(Test test, CopySheetAuthor author) {
        this.author = author;
        prepareTest(test);
    }

    public TestManager(Questionnaire qcms, CopySheetAuthor author) {
        this.author = author;
        prepareQuestionnaire(qcms);
    }

    public boolean isIndefinite() {
        return indefinite;
    }

    public int getTestTimeLeft() {
        return mTest.timeLeft;
    }

    public int getTestTimePass() {
        return getTestTotalTime() - getTestTimeLeft();
    }

    public int getTestTotalTime() {
        return testTime;
    }

    public void startTest(TestStateListener stateListener) {
        if (!run) {
            mStateListener = stateListener;
            run = true;
            if (mStateListener != null) {
                mStateListener.onTestStart(mTest);
            }
            initThread().start();
        }
    }

    public void restartTest() {
        aborted = false;
        if (mStateListener == null)
            throw new UnsupportedOperationException(
                    "you must start a test befor restart it!");
        mStateListener.onTestRestart(mTest);
        if (!isIndefinite()) {
            mTest.timeLeft = testTime;
        } else {
            mTest.timeLeft = 0;
        }
        if (mTest.getExercises() != null && mTest.getExercises().size() > 0) {
            for (Exercise exercise : mTest.getExercises()) {
                exercise.setAnswers(new ArrayList<Proposition>());
            }
        }
        if (!run) {
            startTest(mStateListener);
        } else {
            mStateListener.onTestStart(mTest);
        }

    }

    public void abortCurrentTest() {
        stopCurrentTest();
        mStateListener.onTestAborted(mTest);
        aborted = true;

    }

    public boolean isTestAborted() {
        return aborted;
    }

    public boolean isTestRunning() {
        return run;
    }

    public void finishCurrentTest() {
        stopCurrentTest();
        finish();
    }

    public boolean isTestTimeOut() {
        return mTest.timeLeft <= 0;
    }

    public void destroyCurrentTest() {
        stopCurrentTest();
    }

    private final void prepareTest(Test test) {
        // mProcessor = new QcmProcessor();
        this.mTest = test;
        testTime = test.timeLeft;
        if (mTest.isIndefinite()) {
            indefinite = true;
            if (mTest.timeLeft >= Test.VALUE_MAX_TIME_TO_BECOME_INFINITE) {
                mTest.timeLeft = 0;
            }
        }
    }

    public CopySheet getTestCopySheet() {
        CopySheet copySheet = new CopySheet(getCurrentTest(), author);
        copySheet.getQuestionnaire().setAuthor(author);
        return copySheet;
    }

    public final void prepareQuestionnaire(Questionnaire qcms) {
        this.mTest = new Test(QuestionnaireUtils.checkRandomShuffle(qcms));
        prepareTest(mTest);
    }

//    public void setPostCopySheetQueryCallBack(
//            CopySheetPostCallBack mQueryCallBack) {
//        this.mCopySheetPostCallBack = mQueryCallBack;
//    }

    public void notifyCurrentExercise(int index) {
        if (mStateListener != null) {
            mStateListener.onTestExerciseSelected(index);
        }
    }

    public Test getCurrentTest() {
        return mTest;
    }

    public interface TestStateListener {
        void onTestTimeOut(Test test);

        void onTestFinish(Test test);

        void onTestStart(Test test);

        void onTestRestart(Test test);

        void onTestAborted(Test test);

        void onTestTimeTick(Test test);

        void onTestExerciseSelected(int exerciseIndex);

    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            mStateListener.onTestTimeTick(mTest);
            if (isTestTimeOut()) {
                mStateListener.onTestTimeOut(mTest);
                finish();
            } else {
                if (isTestAborted()) {
                    finish();
                }
            }
        }

    };
//    private CopySheetPostCallBack mCopySheetPostCallBack = null;

    private void stopCurrentTest() {
        run = false;
    }

    private void finish() {
        mStateListener.onTestFinish(mTest);
        Questionnaire questionnaire = mTest.getQuestionnaire();
        try {
            if (sendToRemoteURLEnable
                    && questionnaire.shouldBeSendToRemoteURL()) {
//                mProcessor.postCopySheet(getTestCopySheet(),
//                        mCopySheetPostCallBack);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Thread initThread() {
        return new Thread() {
            @Override
            public void run() {
                while (run) {
                    try {
                        sleep(INTERVAL_COUNT);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (isIndefinite()) {
                        mTest.timeLeft += INTERVAL_COUNT;
                    } else {
                        mTest.timeLeft -= INTERVAL_COUNT;
                    }
                    if (isTestTimeOut()) {
                        stopCurrentTest();
                    }
                    mHandler.post(mRunnable);
                }
            }
        };
    }

    public boolean isSendToRemoteURLEnable() {
        return sendToRemoteURLEnable;
    }

    public void setSendToRemoteURLEnable(boolean sendToRemoteURLEnable) {
        this.sendToRemoteURLEnable = sendToRemoteURLEnable;
    }

}
