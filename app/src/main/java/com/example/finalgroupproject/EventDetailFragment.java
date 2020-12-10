package com.example.finalgroupproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class EventDetailFragment extends Fragment {
    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();


        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_event, container, false);
        saveButton = (Button) result.findViewById(R.id.save);

        //show the message
        TextView eventTitle = (TextView)result.findViewById(R.id.event_detail_title_value);
        TextView eventDate = (TextView)result.findViewById(R.id.event_detail_date_value);
        TextView eventURL = (TextView)result.findViewById(R.id.event_detail_url_value);
        TextView eventPriceRange = (TextView)result.findViewById(R.id.event_detail_price_value);
        eventTitle.setText(dataFromActivity.getString("title"));
        eventDate.setText(dataFromActivity.getString("date"));
        eventURL.setText(dataFromActivity.getString("url"));
        String s = String.valueOf(dataFromActivity.getDouble("min")) + " - " + String.valueOf(dataFromActivity.getDouble("min"));
        eventPriceRange.setText(s);
        final EventDatabaseSingleton eventDatabaseSingleton = EventDatabaseSingleton.getInstance();
        if(dataFromActivity.getBoolean("show_saved")){
            saveButton.setVisibility(View.VISIBLE);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View c) {
                    ContentValues newRowValues = new ContentValues();
                    newRowValues.put(EventDatabaseHelper.KEY_EVENT, dataFromActivity.getString("title"));
                    newRowValues.put(EventDatabaseHelper.KEY_DATE, dataFromActivity.getString("date"));
                    newRowValues.put(EventDatabaseHelper.KEY_MAX, String.valueOf(dataFromActivity.getDouble("max")));
                    newRowValues.put(EventDatabaseHelper.KEY_MIN, String.valueOf(dataFromActivity.getDouble("min")));
                    newRowValues.put(EventDatabaseHelper.KEY_URL, dataFromActivity.getString("url"));
                    SQLiteDatabase cb = eventDatabaseSingleton.getEventDBS();
                    cb.insert(EventDatabaseHelper.TABLE_NAME, null, newRowValues);
                    //eventDatabaseSingleton.getEventDataBaseHelper().delete();
                }
            });
        }else{
            saveButton.setVisibility(View.INVISIBLE);
        }

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }


}
