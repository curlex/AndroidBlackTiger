package abt.androidblacktiger;

import android.content.Context;
import android.content.SharedPreferences;

import com.rmtheis.yandtran.ApiKeys;
import com.rmtheis.yandtran.language.Language;
import com.rmtheis.yandtran.translate.Translate;

public class Translator {

    public static String preferencesLabel = "abt.langPrefs";
    public static String sourceLanguage = "abt.srcLang";
    public static String destinationLanguage = "abt.destLang";

    public static String translate(Context context, String word) {
        SharedPreferences prefs = context.getSharedPreferences(preferencesLabel, 0);
        Translate.setKey(ApiKeys.YANDEX_API_KEY);
        Language srcLang = Language.fromString(prefs.getString(sourceLanguage, "en"));
        Language destLang = Language.fromString(prefs.getString(destinationLanguage, "en"));
        try {
            return Translate.execute(word, srcLang, destLang);
        } catch (Exception e) {
            e.printStackTrace();
            return word;
        }
    }

    public static void main(String[] args) throws Exception {

        Translate.setKey(ApiKeys.YANDEX_API_KEY);

        String translatedText = Translate.execute("School", Language.ENGLISH, Language.IRISH);
        System.out.println(translatedText);
        translatedText = Translate.execute("Supermarket", Language.ENGLISH, Language.IRISH);
        System.out.println(translatedText);
        translatedText = Translate.execute("School", Language.ENGLISH, Language.PORTUGUESE);
        System.out.println(translatedText);
        translatedText = Translate.execute("Supermarket", Language.ENGLISH, Language.PORTUGUESE);
        System.out.println(translatedText);
        translatedText = Translate.execute("Bus Station", Language.ENGLISH, Language.ARABIC);
        System.out.println(translatedText);
        translatedText = Translate.execute("Bus Station", Language.ENGLISH, Language.PORTUGUESE);
        System.out.println(translatedText);
    }
}