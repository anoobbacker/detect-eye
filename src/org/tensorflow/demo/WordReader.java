package org.tensorflow.demo;

import android.content.Context;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import org.tensorflow.demo.env.Logger;

import java.net.ContentHandler;
import java.util.HashSet;
import java.util.Set;

public class WordReader extends Thread {
    private final HashSet<String> processedByHaptic;
    private Boolean stopMe;
    private final HashSet<String> toProcessForHaptic;
    private final Logger LOGGER;
    private final Object identifiedObjLock = new Object();
    private final Object readObjLock = new Object();
    private final TextToSpeech textToSpeechEngine;
    private static final int MAXQUEUESIZE = 3;
    private static final int BETWEENREADDELAY = 10;
    private final Context context;
    private final Vibrator vibrator;

    //This is hardcoded now but ideally it should be based
    // on other sensor inputs like acceleromater.
    private static final int MAXREADOUTKNOCKRATE = 15;
    private int readOutCountTrack = 0;

    private DetectorActivity.FeedbackMode vibrateFeedbackMode;
    private DetectorActivity.FeedbackMode narrateFeedbackMode;

    public WordReader(TextToSpeech textToSpeechEngine, DetectorActivity.FeedbackMode narrateFeedbackMode, Context context, Logger logger) {
        this.stopMe = false;
        this.toProcessForHaptic = new HashSet<String>();
        this.processedByHaptic = new HashSet<String>();
        this.LOGGER = logger;
        this.textToSpeechEngine = textToSpeechEngine;
        this.narrateFeedbackMode = narrateFeedbackMode;
        this.vibrateFeedbackMode = DetectorActivity.FeedbackMode.None;
        this.context = context;
        if (null != context) {
            this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        } else {
            this.vibrator = null;
        }
    }

    public synchronized void setVibrateFeedbackMode(DetectorActivity.FeedbackMode feedbackMode) {
        this.vibrateFeedbackMode = feedbackMode;
    }

    public synchronized void setNarrateFeedbackMode(DetectorActivity.FeedbackMode feedbackMode) {
        this.narrateFeedbackMode = feedbackMode;
    }

    public synchronized DetectorActivity.FeedbackMode getVibrateFeedbackMode()
    {
        return this.vibrateFeedbackMode;
    }

    public synchronized DetectorActivity.FeedbackMode getNarrateFeedbackMode()
    {
        return this.narrateFeedbackMode;
    }

    public synchronized void stopMe() {
        this.stopMe = true;
    }

    public void addMessage(Set<String> identifiedObjects) {
        if (identifiedObjects.size() <= 0) return;

        synchronized (identifiedObjLock) {
            int bufferSize = this.toProcessForHaptic.size();
            if (bufferSize > MAXQUEUESIZE) {
                LOGGER.i("Flushing out the queue as the len is %d", bufferSize);
                this.toProcessForHaptic.clear();
            }
            this.toProcessForHaptic.addAll(identifiedObjects);
            LOGGER.i("All items in list %s", this.toProcessForHaptic.toString());
        }
    }

    public void readMeOut() {
        String firstMessage = null;
        boolean isVibrated = false;

        synchronized (identifiedObjLock) {
            if (this.toProcessForHaptic.size() <= 0) return;
            firstMessage = this.toProcessForHaptic.iterator().next();

            if (!this.processedByHaptic.contains(firstMessage)) {
                if ((null != textToSpeechEngine) && (this.narrateFeedbackMode == DetectorActivity.FeedbackMode.Speak)) {
                    LOGGER.i("Pitch is label %s", firstMessage);
                    textToSpeechEngine.speak(firstMessage, TextToSpeech.QUEUE_FLUSH, null, null);
                }
                if ( (null != vibrator) && !isVibrated && (this.vibrateFeedbackMode == DetectorActivity.FeedbackMode.Vibrate)) {
                    int millisecond = 100;
                    LOGGER.i("Vibrate for %d seconds", millisecond);
                    vibrator.vibrate(millisecond);
                    isVibrated = true;
                }
                this.processedByHaptic.add(firstMessage);
            }
            this.toProcessForHaptic.remove(firstMessage);
        }

        readOutCountTrack++;

        if (readOutCountTrack > MAXREADOUTKNOCKRATE) {
            this.processedByHaptic.clear();
            readOutCountTrack = 0;
        }
    }

    public void run() {
        while (!stopMe) {
            try {
                this.readMeOut();
                sleep(BETWEENREADDELAY);
            } catch (InterruptedException e) {
                LOGGER.i("Error: %s", e.getStackTrace());
            }
        }
    }

    //old function kept for reference in case needed.
    private void oldfun() {
        /*
        switch (mode) {
            case Vibrate:
                float amplitude = (result.getConfidence() * 100) / 255;
                int millisecond = Math.round(result.getConfidence() * 100 * 100);
                //VibrationEffect effect = VibrationEffect.CreateOneShot(700, amplitude)
                LOGGER.i("Vibrate for %d seconds", millisecond);
                v.vibrate(millisecond);
                break;
            case Speak:
                //float pitch = (result.getConfidence() * 10) / 7;
                //LOGGER.i("Pitch is %f for label %s", pitch, result.getTitle());
                //textToSpeechEngine.setPitch(pitch);
                //textToSpeechEngine.speak(result.getTitle(), TextToSpeech.QUEUE_FLUSH, null, null);
                break;
        }
        */
    }

}