package abt.androidblacktiger;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewVocabActivity extends AppCompatActivity {
    private String engWord = "";
    private String translatedWord = "";
    private String location = "";
    private String[] wordsToShow = {"", ""};
    private int wordsCount = wordsToShow.length;
    private int currentIndex = -1;
    private Button changeWordBtn;
    private TextSwitcher mySwitcher;
    Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vocab);
        Intent intent = getIntent();
        engWord = intent.getStringExtra(getString(R.string.word_intent_word));
        translatedWord = intent.getStringExtra(getString(R.string.word_intent_translation));
        location = intent.getStringExtra(getString(R.string.word_intent_Latitude));
        location = intent.getStringExtra(getString(R.string.word_intent_Longitude));

        wordsToShow[0] = engWord;
        wordsToShow[1] = translatedWord;

        changeWordBtn = (Button) findViewById(R.id.changeWordsButton);
        mySwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);

        //set how the words will appear on the screen
        mySwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView txt = new TextView(NewVocabActivity.this);
                txt.setGravity(Gravity.CENTER);
                txt.setTextSize(30);
                txt.setTextColor(Color.BLUE);
                return txt;
            }
        });

        // set the typo of animation to change
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        mySwitcher.setAnimation(in);
        mySwitcher.setAnimation(out);

        // set the button listener for swapping the words
        changeWordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                currentIndex++;

                if(currentIndex == wordsCount)
                    currentIndex = 0;

                mySwitcher.setText(wordsToShow[currentIndex]);
            }
        });
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
            return true;
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getApplicationContext(), NewVocabActivity.class);
        intent.putExtra(getString(R.string.word_intent_image),imagePath);
        startActivity(intent);
    }

    public void doNotRepeatWord(View view) {
        // if the word is prompted and the checkbox is marked don't display the word
    }
}
