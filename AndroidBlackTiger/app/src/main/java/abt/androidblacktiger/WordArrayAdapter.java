package abt.androidblacktiger;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.getColor;

/**
 * Created by Diarmuid on 10/11/2015.
 */
public class WordArrayAdapter extends ArrayAdapter<WordHistory> {

    private final Context context;
    private final ArrayList<WordHistory> words;
    private final int resource = R.layout.textviewlay;
    private final int wordView = R.id.history_textview;
    private final int transView = R.id.translation_textview;

    public WordArrayAdapter(Context context, ArrayList<WordHistory> words) {
        super(context, R.layout.textviewlay);
        this.context = context;
        this.words = words;
    }

    public View getView (int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null) {
            view = inflater.inflate(resource, parent, false);
        }
        TextView textView = (TextView) view.findViewById(wordView);
        WordHistory wordHistory = words.get(position);
        String word = wordHistory.getWord();
        textView.setText(word);
        String translation = wordHistory.getTranslation();
        TextView translatedView = (TextView) view.findViewById(transView);
        translatedView.setText(translation);
        return view;
    }

    public int getCount() {
        return words.size();
    }

}
