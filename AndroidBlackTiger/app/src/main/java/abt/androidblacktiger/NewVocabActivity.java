package abt.androidblacktiger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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
    private int currentIndex = 0;
    Uri imagePath;
    HistoryDBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vocab);
        Intent intent = getIntent();
        engWord = intent.getStringExtra(getString(R.string.word_intent_word));
        translatedWord = intent.getStringExtra(getString(R.string.word_intent_translation));
        language = intent.getStringExtra(getString(R.string.word_intent_language));
        locationLat = intent.getDoubleExtra(getString(R.string.word_intent_Latitude), 0);
        locationLong = intent.getDoubleExtra(getString(R.string.word_intent_Longitude), 0);
        shown = intent.getIntExtra(getString(R.string.word_intent_shown), 0);
        again = intent.getBooleanExtra(getString(R.string.word_intent_again), true);

        TextView txtViewEng = (TextView) findViewById(R.id.engWord);
        txtViewEng.setText(engWord);

        TextView txtViewTranslation = (TextView) findViewById(R.id.translation);
        txtViewTranslation.setText(translatedWord);

        ImageView imageView = (ImageView) findViewById(R.id.photo);

        imagePath = intent.getData();
        String path;

        if(imagePath != null) {
            path = imagePath.toString();
            if(path.toLowerCase().startsWith("file://")) {
                path = (new File(URI.create(path))).getAbsolutePath();
                Bitmap bmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bmap);
            }
        }

        Button vocabSaveWordBtn = (Button) findViewById(R.id.vocabSaveWordBtn);
        vocabSaveWordBtn.setOnClickListener(
                new Button.OnClickListener() {
                        public void onClick(View v) {
                            db = ABTApplication.db;
                            CoOrdinates loc = new CoOrdinates(locationLat, locationLong);
                            WordHistory data = new WordHistory(engWord,language,translatedWord, loc, shown, again, image);
                            db.addWordHistory(data);
                        }
                }
        );
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

        image = imagePath.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getApplicationContext(), NewVocabActivity.class);
        intent.putExtra(getString(R.string.word_intent_image),imagePath);
        startActivity(intent);
    }

    public void doNotRepeatWord(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        db = ABTApplication.db;
        CoOrdinates loc = new CoOrdinates(locationLat, locationLong);

        WordHistory data;

        if(checked) {
            again = false;
            data = new WordHistory(engWord,language,translatedWord, loc, shown, again, image);
            db.addWordHistory(data);
        }
    }
}
