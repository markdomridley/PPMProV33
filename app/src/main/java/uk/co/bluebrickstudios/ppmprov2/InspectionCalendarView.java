package uk.co.bluebrickstudios.ppmprov2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.model.Inspection;


public class InspectionCalendarView extends AppCompatActivity {

    private static final String LOG = InspectionCalendarView.class.getName();

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;
    private CalendarView mCalendarView;
    private List<EventDay> mEventDays = new ArrayList<>();

    private int client_id = 0;
    private int estate_id = 0;

    DatabaseHelper controller;

    private static String dateStringPattern = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspection_calendar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        this.controller = DatabaseHelper.getInstance(this);

        /*
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
        */
        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                previewNote(eventDay);
            }
        });

        PPMProApp app = (PPMProApp) getApplication();
        client_id = app.getClient_id();
        estate_id = app.getEstate_id();


        List<Inspection> inspections;
        //inspections = this.controller.getCurrentInspections(client_id, estate_id);
        inspections = this.controller.getAllInspections(client_id, estate_id);

        for(int i = 0; i < inspections.size(); i++) {
            Inspection inspection = inspections.get(i);

            Log.d(LOG, inspection.getName() + ":" + inspection.getStart_at());

            Calendar calStart = stringToCalendar(inspection.getStart_at(), dateStringPattern);
            Calendar calEnd = stringToCalendar(inspection.getEnd_at(), dateStringPattern);

            int icon = 1;

            switch(inspection.getFrequency_id()){
                case 1:
                    icon = R.drawable.freq_1;
                    break;
                case 2:
                    icon = R.drawable.freq_2;
                    break;
                case 3:
                    icon = R.drawable.freq_3;
                    break;
                case 4:
                    icon = R.drawable.freq_4;
                    break;
                case 5:
                    icon = R.drawable.freq_5;
                    break;
                case 6:
                    icon = R.drawable.freq_6;
                    break;
                case 7:
                    icon = R.drawable.freq_7;
                    break;
                case 8:
                    icon = R.drawable.freq_8;
                    break;
                case 9:
                    icon = R.drawable.freq_9;
                    break;
            }


            MyEventDay myEventDay = new MyEventDay(calStart, icon, inspection.getName(), inspection.getId());
            mCalendarView.setDate(myEventDay.getCalendar());
            mEventDays.add(myEventDay);

            myEventDay = new MyEventDay(calEnd, icon, inspection.getName(), inspection.getId());
            mCalendarView.setDate(myEventDay.getCalendar());
            mEventDays.add(myEventDay);

            while (calStart.before(calEnd)) {
                Date result = calStart.getTime();
                Calendar cal = Calendar. getInstance();
                cal.setTime(result);
                myEventDay = new MyEventDay(cal, icon, inspection.getName(), inspection.getId());
                mCalendarView.setDate(myEventDay.getCalendar());
                mEventDays.add(myEventDay);
                calStart.add(Calendar.DATE, 1);
            }


            /*
            EventDay eventDay = new EventDay(cal, R.mipmap.ic_action_list);
            mCalendarView.setDate(eventDay.getCalendar());
            mEventDays.add(eventDay);
            */

        }

        //Calendar calendar = Calendar.getInstance();
        //mEventDays.add(new EventDay(calendar, R.drawable.bluebricklogolarge));
        mCalendarView.setEvents(mEventDays);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());
            mEventDays.add(myEventDay);
            mCalendarView.setEvents(mEventDays);
        }
    }
    /*
    private void addNote() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }
    */
    private void previewNote(EventDay eventDay) {
        Intent intent = new Intent(this, InspectionCalendarEvent.class);
        if(eventDay instanceof MyEventDay){
            intent.putExtra(EVENT, (MyEventDay) eventDay);
        }
        startActivity(intent);
    }

    public static Calendar stringToCalendar(String stringDate, String datePattern) {
        if (stringDate == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        try {
            Timestamp newDate = Timestamp.valueOf(stringDate);
            calendar.setTime(newDate);
        }
        catch (Exception e) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
            try {
                calendar.setTime(simpleDateFormat.parse(stringDate));
            }
            catch (ParseException pe) {
                calendar = null;
            }
        }
        return calendar;
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