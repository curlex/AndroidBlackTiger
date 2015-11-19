package abt.androidblacktiger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rmtheis.yandtran.ApiKeys;
import com.rmtheis.yandtran.language.Language;
import com.rmtheis.yandtran.translate.Translate;

import java.util.ArrayList;

/**
 * Created by User on 19/11/2015.
 */
public class MyTranslate {
    private Context context;

    public MyTranslate(Context context) {
        this.context = context;
    }

    protected ArrayList<String> doInBackground(ArrayList<String> toTranslate) {
        ArrayList<String> englishWords = toTranslate;
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
}
