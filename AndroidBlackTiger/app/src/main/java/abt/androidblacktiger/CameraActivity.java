package abt.androidblacktiger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Diarmuid.
 */
public class CameraActivity extends AppCompatActivity
{
    Uri imagePath;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getApplicationContext(), NewVocabActivity.class);
        intent.putExtra(getString(R.string.word_intent_image),imagePath);
        startActivity(intent);
    }
}