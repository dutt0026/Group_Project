package com.example.finalgroupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SavedEvents extends AppCompatActivity {


    private List<Event> eventsList = new ArrayList<>();
    private ListView listView;
    private EventListAdapter eventListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_events);
        listView = (ListView) findViewById(R.id.listViewSavedEvents);
        eventListAdapter = new EventListAdapter(eventsList, getApplicationContext());
        listView.setAdapter(eventListAdapter);
        loadDataFromDatabase();


        listView.setOnItemLongClickListener((parent, view, position, id) -> {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Do you want to delete this?");
                    alertDialogBuilder.setMessage("The selected row is: " + position);

                    alertDialogBuilder.setPositiveButton("Yes", (click, arg) -> {
                        deleteEvent(eventsList.get(position));
                        eventsList.remove(position);
                        eventListAdapter.notifyDataSetChanged();
                    })
                            .setNegativeButton("No", (click, arg) -> {

                            })
                            .create()
                            .show();

                    return true;
                }

        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                //Create a bundle to pass data to the new fragment
                Bundle dataToPass = new Bundle();
                dataToPass.putString("title", eventsList.get(position).getEventTitle());
                dataToPass.putString("date", eventsList.get(position).getStartDate());
                dataToPass.putString("url", eventsList.get(position).getURL());
                dataToPass.putDouble("min", eventsList.get(position).getMin());
                dataToPass.putDouble("max", eventsList.get(position).getMax());
                dataToPass.putBoolean("show_saved",false);
                dataToPass.putInt("item_pos", position);
                dataToPass.putLong("item_id", id);

                EventDetailFragment dFragment = new EventDetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.to_replace_saved_events,dFragment);
                fragmentTransaction.commit();

            }
        });

    }

    private void loadDataFromDatabase(){

        String[] columns = {EventDatabaseHelper.KEY_ID, EventDatabaseHelper.KEY_DATE, EventDatabaseHelper.KEY_EVENT,EventDatabaseHelper.KEY_MAX,EventDatabaseHelper.KEY_MIN,EventDatabaseHelper.KEY_URL};
        Cursor results = EventDatabaseSingleton.getInstance().getEventDBS().query(false,EventDatabaseHelper.TABLE_NAME, columns,null,null,null,null,null,null);
        //printCursor(results, chatDBS.getVersion());
        int eventIndex = results.getColumnIndex(EventDatabaseHelper.KEY_EVENT);
        int minIndex = results.getColumnIndex(EventDatabaseHelper.KEY_MIN);
        int maxIndex = results.getColumnIndex(EventDatabaseHelper.KEY_MAX);
        int urlIndex = results.getColumnIndex(EventDatabaseHelper.KEY_URL);
        int dateIndex = results.getColumnIndex(EventDatabaseHelper.KEY_DATE);
        int idIndex = results.getColumnIndex(EventDatabaseHelper.KEY_ID);

        while(results.moveToNext())
        {
            String eventTitle = results.getString(eventIndex);
            double min = results.getDouble(minIndex);
            double max = results.getDouble(maxIndex);
            String url = results.getString(urlIndex);
            String date = results.getString(dateIndex);
            long id = results.getLong(idIndex);
            Event event = new Event();
            event.setEventTitle(eventTitle);
            event.setMin(min);
            event.setMax(max);
            event.setURL(url);
            event.setStartDate(date);
            event.setID(id);
            eventsList.add(event);

        }


        eventListAdapter.notifyDataSetChanged();
    }
    protected void deleteEvent(Event event)
    {
        long id = event.getId();
        EventDatabaseSingleton.getInstance().getEventDBS().delete(EventDatabaseHelper.TABLE_NAME, EventDatabaseHelper.KEY_ID + "= ?", new String[]{Long.toString(event.getId())});

    }
}