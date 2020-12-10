package com.example.finalgroupproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventSearch extends AppCompatActivity {

    private Button searchEvents; // button to search events --> activity_event_list.xml
    private Button savedEvents; // button to show saved events --> activity_saved_events.xml
    private EditText city;
    private EditText radius;
    private List<Event> eventsList = new ArrayList<>();
    private EventListAdapter eventListAdapter;
    private ListView listView;
    private ProgressBar progressBar;
    private SharedPreferences sp;
    private Toolbar toolbar;
    private int clickedSavedEvents = 0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);


	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }  });
	    */

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);
        searchEvents = (Button) findViewById(R.id.search_button);
        savedEvents = (Button) findViewById(R.id.Saved_EventsBtn);
        city = (EditText) findViewById(R.id.city);
        radius = (EditText) findViewById(R.id.radius);
        listView = (ListView) findViewById(R.id.eventsListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        EventDatabaseSingleton.getInstance(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("PARIS'S MAIN PAGE :D");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // SETTING UP NAVIGATION DRAWER
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                String message = null;

                switch(item.getItemId())
                {
                    case R.id.search_item:
                        message = "You clicked on search";

                        break;
                    case R.id.savdEvents:
                        message = "You clicked on saved events";

                        break;
                    case R.id.helpitem:
                        message = "You clicked on help";
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                        alertDialogBuilder.setTitle("HELP PAGE");
                        alertDialogBuilder.setMessage("Search for upcoming events near a given city. Enter the city name, and a search radius for the events. The application will return a list of events scheduled in that city. Clicking on an event name should show you the starting date, the price range of tickets and the URL from ticketmaster. You are also able to save the event’s details and return back to them at a later date.");
                        break;

                    case R.id.language:
                        message = "You clicked on French";
                        break;
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);

                //Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //For NavigationDrawer:





        //

        // adapter setup

        eventListAdapter = new EventListAdapter(eventsList, getApplicationContext());
        listView.setAdapter(eventListAdapter);
        //adapter setup END

        searchEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!valid()) {
                    // alert user that city & radius is required b4 proceeding
                    return;
                }
                String cityText = city.getText().toString().trim();
                String radiusText = radius.getText().toString().trim();

                EventQueryTask eventQuery = new EventQueryTask();
                String urlString = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=879TjJRpOdnDbY4CnDUGWhFWE3rJ2KxE&city="+cityText+"&radius="+radiusText;
                eventQuery.execute(urlString);
            }
        });

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
                dataToPass.putBoolean("show_saved",true);
                dataToPass.putInt("item_pos", position);
                dataToPass.putLong("item_id", id);

                EventDetailFragment dFragment = new EventDetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.to_replace,dFragment);
                fragmentTransaction.commit();

            }
        });
        /*Saved Events Button --> brings you to activity_saved_events.xml where it displays all previously saved events */
        savedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventSearch.this, SavedEvents.class);
                startActivityForResult(intent, 50);

            }
        });

        sp = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = sp.getString("ReserveName", "");



    }



    private class EventQueryTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... urls) {
            for(String url: urls){
                try {
                    URL u = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
                    StringBuilder sb = new StringBuilder();

                    publishProgress(25);

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    publishProgress(50);

                    String jsonString = sb.toString();
                    JSONObject j = new JSONObject(jsonString);
                    JSONArray jsonEventsArray = j.getJSONObject("_embedded").getJSONArray("events");
                    publishProgress(75);
                    for (int i = 0; i < jsonEventsArray.length(); i++){
                        JSONObject jsonEvent = jsonEventsArray.getJSONObject(i);
                        Event event = new Event();
                        event.setEventTitle(jsonEvent.getString("name"));
                        event.setStartDate(jsonEvent.getJSONObject("dates").getJSONObject("start").getString("dateTime"));
                        event.setURL(jsonEvent.getString("url"));
                        JSONObject priceRange = (jsonEvent.getJSONArray("priceRanges")).getJSONObject(0);
                        event.setMax(priceRange.getDouble("max"));
                        event.setMin(priceRange.getDouble("min"));
                        eventsList.add(event);

                    }
                    publishProgress(100);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            eventListAdapter.notifyDataSetChanged();
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private boolean valid() {
        String cityText = city.getText().toString().trim();
        String radiusText = radius.getText().toString().trim();
        if (cityText.length() == 0 || radiusText.length() == 0) {
            return false;
        }
        if (!TextUtils.isDigitsOnly(radiusText)) {
            return false;
        }
        return true;
    }
    @Override
    protected void onResume () {
        super.onResume();
        city.setText(sp.getString("ReserveName", " "));
    }
    @Override
    protected void onPause() {
        super.onPause();

        //get the object
        SharedPreferences.Editor editor = sp.edit();

        //saving users input under "ReserveName"
        String usersInput = city.getText().toString();
        editor.putString("ReserveName", usersInput);
        editor.commit();
    }




    /// navigation bar stuff


}
