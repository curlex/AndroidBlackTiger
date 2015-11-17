package abt.androidblacktiger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ciarán on 28/10/2015.
 */
public class CameraActivity extends AppCompatActivity
{
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String TAG = "CameraError";
    String mCurrentPhotoPath;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dispatchTakePictureIntent();
        galleryAddPic();
    }

    public void dispatchTakePictureIntent()
    {
        Intent TakePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(TakePictureIntent.resolveActivity(getPackageManager())!=null)
        {
            File photofile = null;
            try
            {
                photofile =  createImageFile();
            }
            catch(IOException e)
            {
                Log.d(TAG, "Error occurred creating file");
            }
            if (photofile!=null)
            {
                TakePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                startActivityForResult(TakePictureIntent, MEDIA_TYPE_IMAGE);
                Log.d(TAG, mCurrentPhotoPath);
            }
        }
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File StorageDir = getFilesDir();
        File image = File.createTempFile(imageFileName, ".jpg", StorageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}






