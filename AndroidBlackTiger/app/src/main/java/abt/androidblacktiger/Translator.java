package abt.androidblacktiger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.rmtheis.yandtran.ApiKeys;
import com.rmtheis.yandtran.language.Language;
import com.rmtheis.yandtran.translate.Translate;

import java.util.ArrayList;

/**
 * An {@link AsyncTask} for the translation functionality provided by {@link com.rmtheis.yandtran.YandexTranslatorAPI}
 * Should be called as Translator.execute()
 * Author: Diarmuid
 */
public class Translator extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

    private Context context;

    public Translator(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... toTranslate) {
        ArrayList<String> englishWords = toTranslate[0];
        ArrayList<String> words = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Translate.setKey(ApiKeys.YANDEX_API_KEY);
        Language srcLang = Language.ENGLISH;
        Language destLang = Language.fromString(prefs.getString(context.getString(R.string.preference_language), "en"));
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

    protected void onPostExecute(ArrayList<String> translated){
        super.onPostExecute(translated);

    }
}