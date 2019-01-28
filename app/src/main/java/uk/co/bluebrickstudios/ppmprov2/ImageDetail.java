package uk.co.bluebrickstudios.ppmprov2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.model.Image;

public class ImageDetail extends AppCompatActivity {
    private int _Image_Id;
    DatabaseHelper controller;
    private ImageView mImageView;

    public ImageDetail() {
        this._Image_Id = 0;
        this.controller = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.image_detail);
        this.controller = DatabaseHelper.getInstance(this);
        this._Image_Id = 0;
        this._Image_Id = getIntent().getIntExtra("image_id", 0);
        Image image = new Image();
        image = this.controller.getImage((long) this._Image_Id);
        this.mImageView = (ImageView) findViewById(R.id.singleImageView);
        if (image != null) {
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (image.getLocalImageRef() != null) {
                File photoFile = new File(storageDir, image.getName());
                if (photoFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    if (myBitmap != null) {
                        this.mImageView.setImageBitmap(myBitmap);
                    }
                }
            }
            ((TextView) findViewById(R.id.title)).setText(image.getName());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
