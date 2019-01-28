package uk.co.bluebrickstudios.ppmprov2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.adapter.InspectionAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ItemAdapter;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.model.Inspection;


public class InspectionCalendarEvent extends AppCompatActivity {

    DatabaseHelper controller = null;
    RecyclerView recList;
    int client_id = 0;
    int estate_id = 0;
    TextView inspection_Id;

    private static final String LOG = InspectionCalendarEvent.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG, "DEBUG");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspection_calendar_day);
        Intent intent = getIntent();

        controller = DatabaseHelper.getInstance(this);

        /* instantiate Recycler View */
        recList = (RecyclerView) findViewById(R.id.cardInspectionList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        PPMProApp app = (PPMProApp)getApplication();
        client_id = app.getClient_id();
        estate_id = app.getEstate_id();

        String selected_date = "";

        //TextView note = (TextView) findViewById(R.id.note);
        if (intent != null) {
            Object event = intent.getParcelableExtra(InspectionCalendarView.EVENT);
            if(event instanceof MyEventDay){
                MyEventDay myEventDay = (MyEventDay)event;
                getSupportActionBar().setTitle(getFormattedDate(myEventDay.getCalendar().getTime()));
                selected_date = getFormattedDate(myEventDay.getCalendar().getTime());
                //note.setText(myEventDay.getNote());
                //return;
            }
            if(event instanceof EventDay){
                EventDay eventDay = (EventDay)event;
                getSupportActionBar().setTitle(getFormattedDate(eventDay.getCalendar().getTime()));
                selected_date = getFormattedDate(eventDay.getCalendar().getTime());

            }
        }

        ArrayList<Inspection> inspections;
        inspections = this.controller.getInspectionsByDay(client_id, estate_id, selected_date);

        if (inspections.size() != 0) {

            Log.d(LOG, "INSPECTIONS");

            InspectionAdapter inspectionAdapter = new InspectionAdapter(inspections);

            /*
            inspectionAdapter.SetOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view , int position) {
                    inspection_Id = (TextView) view.findViewById(R.id.inspectionId);
                    String itemId = inspection_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),ItemDetail.class);
                    objIndent.putExtra("inspection_Id", Integer.parseInt(itemId));
                    startActivityForResult(objIndent, 1);
                }
            });
            */
            recList.setAdapter(inspectionAdapter);

        }
        else{

            Log.d(LOG, "NO INSPECTIONS");

        }




    }
    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.generic_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
