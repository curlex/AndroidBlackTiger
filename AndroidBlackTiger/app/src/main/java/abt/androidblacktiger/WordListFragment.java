package abt.androidblacktiger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * An {@link android.app.ListFragment} subclass.
 * Displays all words in the database for this app using the getAllWords() function in
 * {@link HistoryDBHandler}.
 * Author: Diarmuid
 */
public class WordListFragment extends android.app.ListFragment {

    private ArrayList<WordHistory> words;
    private OnFragmentInteractionListener mListener;
    private int currentPosition = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment WordListFragment.
     */
    public static WordListFragment newInstance() {
        return new WordListFragment();
    }

    public WordListFragment() {
        // Required empty public constructor
    }


    /**
     * Gets the words from the database and displays them using a {@link WordArrayAdapter}
     * @param savedInstanceState for the superclass
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadListView();
    }

    private void loadListView() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String language = preferences.getString(getString(R.string.preference_language), "en");
        words = (ArrayList<WordHistory>) ABTApplication.db.getAllWords(language);
        WordArrayAdapter adapter = new WordArrayAdapter(getActivity(), words);
        setListAdapter(adapter);
    }

    public void onPause() {
        super.onPause();
        ListView listView = getListView();
        currentPosition = listView.getFirstVisiblePosition();
    }

    public void onResume() {
        super.onResume();
        loadListView();
        ListView listView = getListView();
        listView.setSelection(currentPosition);
    }


    /**
     * Handles clicks on items in the generated ListView. Opens {@link NewVocabActivity} with extras
     * set for the word selected.
     * @param l the listview
     * @param v some view
     * @param position the position in the list of the item clicked
     * @param id The row id of the item that was clicked
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l,v,position,id);
        WordHistory clickedWord = words.get(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), NewVocabActivity.class);
        intent.putExtra(getString(R.string.word_intent_word), clickedWord.getWord());
        intent.putExtra(getString(R.string.word_intent_translation), clickedWord.getTranslation());
        intent.putExtra(getString(R.string.word_intent_Latitude), clickedWord.getLocations().get(0).getLat());
        intent.putExtra(getString(R.string.word_intent_Longitude), clickedWord.getLocations().get(0).getLng());
        intent.putExtra(getString(R.string.word_intent_language), clickedWord.getLang());
        intent.putExtra(getString(R.string.word_intent_image), clickedWord.getImagePath());
        intent.putExtra(getString(R.string.word_intent_again), clickedWord.getAgain());
        intent.putExtra(getString(R.string.word_intent_shown), clickedWord.getShown());
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
