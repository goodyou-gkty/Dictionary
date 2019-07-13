package com.gkt.browse.dictionary;

import android.os.AsyncTask;
import android.util.Log;


import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class Translator extends AsyncTask<String ,Integer ,String> {
    private static final String API_KEY = "AIzaSyAIMWSUcDbl_yGOBM4d7kLkBH6qXMdfnnY";
    public Translator() {
        super();
    }

    @Override
    protected String doInBackground(String... strings) {

        TranslateOptions translateOptions = TranslateOptions.newBuilder()
                .setApiKey(Translator.API_KEY)
                .build();

        Translate translate = translateOptions.getService();

        // The text to translate
        String text = strings[0];

        // Translates some text into Russian
        Translation translation =
                translate.translate(
                        text,
                        Translate.TranslateOption.sourceLanguage("en"),
                        Translate.TranslateOption.targetLanguage("hi"));


        // System.out.printf("Text: %s%n", text);
        //System.out.printf("Translation: %s%n", translation.getTranslatedText());
        Log.i("Text",text);
        Log.i("Translation",translation.getTranslatedText());

        return translation.getTranslatedText();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.i("translated Text",s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


}
