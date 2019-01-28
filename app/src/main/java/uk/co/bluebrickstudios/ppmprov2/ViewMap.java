package uk.co.bluebrickstudios.ppmprov2;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import java.text.DecimalFormat;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.helper.TouchImageView;
import uk.co.bluebrickstudios.ppmprov2.helper.TouchImageView.OnTouchImageViewListener;
import uk.co.bluebrickstudios.ppmprov2.model.Floor;

public class ViewMap extends AppCompatActivity {
    private static final String LOG;
    private int _Floor_Id;
    DatabaseHelper controller;
    private TextView currentZoomTextView;
    private DecimalFormat df;
    private TouchImageView image;
    private TextView scrollPositionTextView;
    private TextView zoomedRectTextView;

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ViewMap.1 */
    class C02091 implements OnLongClickListener {
        C02091() {
        }

        public boolean onLongClick(View v) {
            Matrix m = new Matrix();
            ViewMap.this.image.getImageMatrix().invert(m);
            m.mapPoints(new float[]{ViewMap.this.image.getLastPoint().x, ViewMap.this.image.getLastPoint().y});
            return true;
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ViewMap.2 */
    class C03062 implements OnTouchImageViewListener {
        C03062() {
        }

        public void onMove() {
            PointF point = ViewMap.this.image.getScrollPosition();
            RectF rect = ViewMap.this.image.getZoomedRect();
            float currentZoom = ViewMap.this.image.getCurrentZoom();
            boolean isZoomed = ViewMap.this.image.isZoomed();
            ViewMap.this.scrollPositionTextView.setText("x: " + ViewMap.this.df.format((double) point.x) + " y: " + ViewMap.this.df.format((double) point.y));
            ViewMap.this.zoomedRectTextView.setText("left: " + ViewMap.this.df.format((double) rect.left) + " top: " + ViewMap.this.df.format((double) rect.top) + "\nright: " + ViewMap.this.df.format((double) rect.right) + " bottom: " + ViewMap.this.df.format((double) rect.bottom));
            ViewMap.this.currentZoomTextView.setText("getCurrentZoom(): " + currentZoom + " isZoomed(): " + isZoomed);
        }
    }

    public ViewMap() {
        this._Floor_Id = 0;
        this.controller = null;
    }

    static {
        LOG = ViewMap.class.getName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_map);
        this.controller = DatabaseHelper.getInstance(this);
        this._Floor_Id = 0;
        this._Floor_Id = getIntent().getIntExtra("floor_id", 0);
        Floor floor = new Floor();
        floor = this.controller.getFloor((long) this._Floor_Id);
        this.df = new DecimalFormat("#.##");
        this.scrollPositionTextView = (TextView) findViewById(R.id.scroll_position);
        this.zoomedRectTextView = (TextView) findViewById(R.id.zoomed_rect);
        this.currentZoomTextView = (TextView) findViewById(R.id.current_zoom);
        this.image = (TouchImageView) findViewById(R.id.img);
        this.image.setMaxZoom(4.0f);
        if (this._Floor_Id > 0) {
            this.image.setImageResource(getResources().getIdentifier(floor.getFloorplan(), "drawable", getPackageName()));
        }
        this.image.setOnLongClickListener(new C02091());
        this.image.setOnTouchImageViewListener(new C03062());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.generic_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
