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
    public static final String EXTRA_TO_SPEAK = "sayThis";
    private TextToSpeech tts;
    private String sayThis;
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

        sayThis = intent.getStringExtra(SpeechService.EXTRA_TO_SPEAK);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        tts.setLanguage(new Locale(pref.getString("speechLocale", "en_US")));
        tts.setSpeechRate(Float.parseFloat(pref.getString("speechSpeed","0.8")));

        if (isInit != null && isInit) {
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
            Toast.makeText(getApplicationContext(),"Reading...",Toast.LENGTH_SHORT).show();
            int result = tts.setLanguage(new Locale(pref.getString("speechLocale", "en_US")));
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                speak();
                isInit = true;
            } else {
                Toast.makeText(getApplicationContext(), UNSUPPORTED_LANGUAGE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void speak() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (tts != null) {
            if(pref.getString("wordPause","0").equals("0")){
                tts.speak(sayThis, TextToSpeech.QUEUE_FLUSH, null);
            }else{
                final Handler h = new Handler();
                String[] words = sayThis.split(" ");
                for (final String word : words) {
                    Runnable t = new Thread() {
                        @Override
                        public void run() {
                            tts.speak(word, TextToSpeech.QUEUE_ADD, null);
                        }
                    };
                    h.postDelayed(t, Integer.parseInt(pref.getString("wordPause","0")));
                }
            }

        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
