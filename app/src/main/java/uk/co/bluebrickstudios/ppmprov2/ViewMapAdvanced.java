package uk.co.bluebrickstudios.ppmprov2;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
//import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.helper.TouchImageView;
import uk.co.bluebrickstudios.ppmprov2.model.Floor;
import uk.co.bluebrickstudios.ppmprov2.model.Item;
import uk.co.bluebrickstudios.ppmprov2.helper.FloorplanView;

/**
 * Created by dominicr on 09/07/2015.
 */
public class ViewMapAdvanced  extends AppCompatActivity {

    private static final String LOG = ViewMap.class.getName();

    private FloorplanView image;
    private TextView scrollPositionTextView;
    private TextView zoomedRectTextView;
    private TextView currentZoomTextView;
    private DecimalFormat df;
    private String location;
    File file;
    FileOutputStream fileoutputstream;
    ByteArrayOutputStream bytearrayoutputstream;

    private int _Floor_Id=0;

    DatabaseHelper controller = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_advanced);

        controller = DatabaseHelper.getInstance(this);



        _Floor_Id =0;
        Intent intent = getIntent();
        _Floor_Id =intent.getIntExtra("floor_id", 0);
        Floor floor = new Floor();
        floor = controller.getFloor(_Floor_Id);

        //
        // DecimalFormat rounds to 2 decimal places.
        //
        df = new DecimalFormat("#.##");
        scrollPositionTextView = (TextView) findViewById(R.id.scroll_position);
        zoomedRectTextView = (TextView) findViewById(R.id.zoomed_rect);
        currentZoomTextView = (TextView) findViewById(R.id.current_zoom);

        //image = (TouchImageView) findViewById(R.id.img);

        image = (FloorplanView) findViewById(R.id.img);

        if(_Floor_Id > 0){
            Resources resources = this.getResources();
            final int resourceId = resources.getIdentifier(floor.getFloorplan(), "drawable", this.getPackageName());

            image.setImage(ImageSource.resource(resourceId));

            String locationS = intent.getStringExtra("location");
            if(locationS != null && locationS.contains(":")){
                location = locationS;
                String[] coords = location.split(":");
                PointF coordsP = new PointF();
                coordsP.set(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]));
                image.setPin(coordsP);
            }

        }

        image.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Matrix m = new Matrix();
                Matrix matrix = image.getMatrix();
                matrix.invert(m);

                //float[] pts = {image.getStart().x, image.getStart().y };
                float[] pts = {image.getStart().x, image.getY() };
                m.mapPoints(pts);

                Log.d(LOG, Float.toString(image.getStart().x) + ":" + Float.toString(image.getStart().y));

                PointF finalPosition = new PointF(image.getStart().x, image.getStart().y);

                PointF vStart = new PointF(image.getStart().x, image.getStart().y);
                PointF sStart = vStart == null ? null : new PointF(image.viewToSourceCoord(vStart).x, image.viewToSourceCoord(vStart).y);

                location = Double.toString(sStart.x) + ":" + Double.toString(sStart.y);

                image.setPin(sStart);

                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.close) {
            finish();
        }
        if (id == R.id.save) {

            bytearrayoutputstream = new ByteArrayOutputStream();

            Intent objIndent = new Intent();
            objIndent.putExtra("location", location);

            //image.resetScaleAndCenter();

            image.buildDrawingCache();
            Bitmap bm = image.getDrawingCache();

            bm.compress(Bitmap.CompressFormat.JPEG, 30, bytearrayoutputstream);

            try{
                file = File.createTempFile("FLOORPLAN_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
            }
            catch(IOException ioe){
                ioe.printStackTrace();
            }

            try
            {
                fileoutputstream = new FileOutputStream(file);
                fileoutputstream.write(bytearrayoutputstream.toByteArray());
                fileoutputstream.close();

                objIndent.putExtra("floorplanimage", file.getName());
                objIndent.putExtra("floorplanimage_path", "file:" + file.getAbsolutePath());
            }

            catch (Exception e)
            {
                e.printStackTrace();
                objIndent.putExtra("floorplanimage", "NOFILE");
            }

            setResult(RESULT_OK, objIndent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}