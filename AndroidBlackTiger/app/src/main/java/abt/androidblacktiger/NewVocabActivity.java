package abt.androidblacktiger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewVocabActivity extends AppCompatActivity {
    private String engWord = "";
    private String language = "";
    private String translatedWord = "";
    private Double locationLat;
    private Double locationLong;
    private int shown;
    private Boolean again;
    private String image = "";
    private Uri imagePath;
    private HistoryDBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vocab);
        Intent intent = getIntent();
        engWord = intent.getStringExtra(getString(R.string.word_intent_word));
        translatedWord = intent.getStringExtra(getString(R.string.word_intent_translation));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        language = prefs.getString(getString(R.string.preference_language), "en");
        locationLat = intent.getDoubleExtra(getString(R.string.word_intent_Latitude), 0);
        locationLong = intent.getDoubleExtra(getString(R.string.word_intent_Longitude), 0);
        shown = intent.getIntExtra(getString(R.string.word_intent_shown), 0);
        again = intent.getBooleanExtra(getString(R.string.word_intent_again), true);
        image = intent.getStringExtra(getString(R.string.word_intent_image));
        db = ABTApplication.db;

        TextView txtViewEng = (TextView) findViewById(R.id.engWord);
        txtViewEng.setText(engWord);

        TextView txtViewTranslation = (TextView) findViewById(R.id.translation);
        txtViewTranslation.setText(translatedWord);

        if (image != null)
            setPhoto(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_vocab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public void openActivity(View view) {
        String time = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
        time = android.net.Uri.encode(time);
        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagefile = new File(imageDir,time+".jpg");
        imagePath = android.net.Uri.fromFile(imagefile);
        if(!imagefile.exists()){
            try {
                imagefile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(imagefile.isFile()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        image = imagePath.getPath();
        updateDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getApplicationContext(), NewVocabActivity.class);
        intent.putExtra(getString(R.string.word_intent_image), imagePath.getPath());
        startActivity(this.getIntent());
    }

    public void doNotRepeatWord(View view) {
        CheckBox cb = (CheckBox) view.findViewById(R.id.newVocabCheckBox);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });
    }

    public void updateDB() {
        CoOrdinates loc = new CoOrdinates(locationLat, locationLong);
        WordHistory data;
        shown++;

        // if the word is already in the database
        if(db.findWord(engWord, language) != null){
            // if no photo was taken store the default value of image (null)
            if(db.findWord(engWord, language).getImagePath() == null)
                data = new WordHistory(engWord, language, translatedWord, loc, shown, again, image);

            else
                data = new WordHistory(engWord, language, translatedWord, loc, shown, again, db.findWord(engWord,language).getImagePath());
        }
        else
            data = new WordHistory(engWord,language,translatedWord, loc, shown, again, image);

        // add the word into the database
        db.addWordHistory(data);
    }

    private void setPhoto(String path) {
        final ImageView photoTaken = (ImageView) findViewById(R.id.photo);
        final String p = path;

        if (photoTaken != null) {
            photoTaken.post(new Runnable() {
                @Override
                public void run() {
                    // Get the dimensions of the View assigned in xml file
                    int targetW = photoTaken.getMeasuredWidth();
                    int targetH = photoTaken.getMeasuredHeight();

                    // Get the dimensions of the original image
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(p, bmOptions);
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

                    // Compute a scale down factor for the image
                    int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;

                    Bitmap bitmap = BitmapFactory.decodeFile(p, bmOptions);
                    photoTaken.setImageBitmap(bitmap);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HistoryActivity.class);
        updateDB();
        startActivity(intent);
    }
}
