package abt.androidblacktiger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Diarmuid.
 */
public class CameraActivity extends AppCompatActivity
{
    Uri imagePath;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String time = SimpleDateFormat.getDateTimeInstance().format(new Date());
        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        imagePath = null;
        try {
            imagePath = android.net.Uri.fromFile(File.createTempFile(time, ".jpg", imageDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(imagePath != null) {
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