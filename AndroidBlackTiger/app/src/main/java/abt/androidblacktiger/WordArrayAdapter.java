package abt.androidblacktiger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Diarmuid on 10/11/2015.
 */
public class WordArrayAdapter extends ArrayAdapter<WordHistory> {

    private final Context context;
    private final ArrayList<WordHistory> words;
    private final int resource = R.layout.textviewlay;

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
        TextView textView = (TextView) view.findViewById(R.id.history_textview);
        String word = words.get(position).getWord();
        System.out.println("Word: " + word);
        textView.setText(word);
        return view;
    }

    public int getCount() {
        return words.size();
    }

}
