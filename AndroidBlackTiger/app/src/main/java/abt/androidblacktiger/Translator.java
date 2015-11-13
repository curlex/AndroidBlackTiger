package abt.androidblacktiger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.rmtheis.yandtran.ApiKeys;
import com.rmtheis.yandtran.language.Language;
import com.rmtheis.yandtran.translate.Translate;

import java.util.ArrayList;

/**
 * An {@link AsyncTask} for the translation functionality provided by {@link com.rmtheis.yandtran.YandexTranslatorAPI}
 * Should be called as Translator.execute()
 * Author: Diarmuid
 */
public class Translator extends AsyncTask<String, Void, ArrayList<String>> {

    private Context context;

    public Translator(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... englishWords) {
        ArrayList<String> words = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Translate.setKey(ApiKeys.YANDEX_API_KEY);
        Language srcLang = Language.ENGLISH;
        Language destLang = Language.fromString(prefs.getString(SettingsListener.DESTINATION_LANGUAGE, "en"));
        for (String param : englishWords) {
            try {
                words.add(Translate.execute(param, srcLang, destLang));
            } catch (Exception e) {
                e.printStackTrace();
                words.add("---Error Translating---");
            }
        }
        return words;
    }
}