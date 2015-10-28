package abt.androidblacktiger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.rmtheis.yandtran.ApiKeys;
import com.rmtheis.yandtran.language.Language;
import com.rmtheis.yandtran.translate.Translate;

import java.util.ArrayList;

public class Translator extends AsyncTask<TranslatorParams, Void, ArrayList<String>> {

    public static String preferencesLabel = "abt.langPrefs";
    public static String sourceLanguage = "abt.srcLang";
    public static String destinationLanguage = "lang_setting";

    private static String translate(Context context, String word) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Translate.setKey(ApiKeys.YANDEX_API_KEY);
        Language srcLang = Language.fromString(prefs.getString(sourceLanguage, "en"));
        Language destLang = Language.fromString(prefs.getString(destinationLanguage, "en"));
        try {
            return Translate.execute(word, srcLang, destLang);
        } catch (Exception e) {
            e.printStackTrace();
            return word + " " + srcLang.toString() + " " + destLang.toString();
        }
    }

    @Override
    protected ArrayList<String> doInBackground(TranslatorParams... params) {
        ArrayList<String> words = new ArrayList<>();
        for (TranslatorParams param : params) {
            words.add(translate(param.getContext(), param.getWord()));
        }
        return words;
    }
}

class TranslatorParams {
    private final Context context;
    private final String word;

    public TranslatorParams(Context context, String word) {

        this.context = context;
        this.word = word;
    }

    public Context getContext() {
        return context;
    }

    public String getWord() {
        return word;
    }
}