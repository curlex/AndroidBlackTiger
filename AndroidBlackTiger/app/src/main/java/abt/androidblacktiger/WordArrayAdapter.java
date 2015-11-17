package abt.androidblacktiger;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * For use with WordListFragment. Extends {@link ArrayAdapter}.
 * Displays an {@link ArrayList} of {@link WordHistory} in a {@link android.widget.ListView}
 * Author: Diarmuid
 */
public class WordArrayAdapter extends ArrayAdapter<WordHistory> {

    private final Context context;
    private final ArrayList<WordHistory> words;
    private Random random;

    public WordArrayAdapter(Context context, ArrayList<WordHistory> words) {
        super(context, R.layout.textviewlay);
        this.context = context;
        this.words = words;
        random = new Random();
    }

    /**
     * Gets an individual row for display in the {@link android.widget.ListView}
     * @param position The position of the row in the ListView
     * @param view The view in which the rwo is contained
     * @param parent The parent view of the row
     * @return view with the data added
     */
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
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        int randomCol = Color.rgb(r,g,b);
        TextView colour = (TextView) view.findViewById(R.id.history_colour);
        colour.setBackgroundColor(randomCol);
        return view;
    }

    public int getCount() {
        return words.size();
    }

}
