package abt.androidblacktiger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This activity doesn't do very much other than contribute the ActionBar.
 * It's basically a placeholder for a WordListFragment.
 * Author: Diarmuid
 */
public class HistoryActivity extends AppCompatActivity implements WordListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles clicks in the menu.
     * @param item The menu item that was clicked
     * @return true if the click is handled in this method or the return of the superclass otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Just a callback for the list fragment, doesn't do anything
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri.toString());
    }
}
