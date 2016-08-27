package com.purva.nits.spokenenglishapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by nits on 19-08-2016.
 */
public class SpeechService extends Service implements TextToSpeech.OnInitListener {

    public static final String UNSUPPORTED_LANGUAGE_MESSAGE = "TextToSpeech language not supported";
    public static final String EXTRA_TO_SPEAK = "toSpeak";
    private TextToSpeech tts;
    private String toSpeak;
    private Boolean isInit;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        tts = new TextToSpeech(getApplicationContext(), this);
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacksAndMessages(null);

        toSpeak = intent.getStringExtra(SpeechService.EXTRA_TO_SPEAK);

        if (isInit != null) {
            if (isInit)
                Toast.makeText(getApplicationContext(),toSpeak,Toast.LENGTH_SHORT).show();
                speak();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 15 * 1000);

        return SpeechService.START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int result = tts.setLanguage(new Locale(pref.getString("speechLocale", "en_US")));
//            int result = tts.setLanguage(Locale.US);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                speak();
                Toast.makeText(getApplicationContext(), "Speech locale " + pref.getString("speechLocale", "en_US"), Toast.LENGTH_SHORT).show();
                isInit = true;
            } else {
                Toast.makeText(getApplicationContext(), UNSUPPORTED_LANGUAGE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void speak() {
        if (tts != null) {
            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
