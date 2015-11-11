package abt.androidblacktiger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Diarmuid Ryan. For use with ListFragment.
 */
public class WordArrayAdapter extends ArrayAdapter<WordHistory> {

    private final Context context;
    private final ArrayList<WordHistory> words;

    public WordArrayAdapter(Context context, ArrayList<WordHistory> words) {
        super(context, R.layout.textviewlay);
        this.context = context;
        this.words = words;
    }

    public View getView (int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resource = R.layout.textviewlay;
            view = inflater.inflate(resource, parent, false);
        }
        int wordView = R.id.history_textview;
        TextView textView = (TextView) view.findViewById(wordView);
        WordHistory wordHistory = words.get(position);
        String word = wordHistory.getWord();
        textView.setText(word);
        String translation = wordHistory.getTranslation();
        int transView = R.id.translation_textview;
        TextView translatedView = (TextView) view.findViewById(transView);
        translatedView.setText(translation);
        return view;
    }

    public int getCount() {
        return words.size();
    }

}
