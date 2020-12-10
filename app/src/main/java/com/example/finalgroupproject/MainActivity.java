package com.example.finalgroupproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String ACTIVITY_NAME ="MainActivity";
    private Toolbar toolBar; // Toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = (Toolbar)findViewById(R.id.toolbar);
        toolBar.setTitle("Main Activity");


        final Button event = findViewById(R.id.ticketMasterButton);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventSearch.class);
                startActivityForResult(intent, 50);
            }
        });




    }

    public void onActivityResult ( int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == 50 && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "returned to MainActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            Toast toast = Toast.makeText(this, messagePassed, Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void onStart () {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    public void onResume () {
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }
    public void onPause () {
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }
    public void onStop () {
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }
    public void onDestroy () {
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

}