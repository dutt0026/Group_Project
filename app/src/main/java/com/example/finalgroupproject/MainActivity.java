package com.example.finalgroupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent goToAD  = new Intent (MainActivity.this, Audio_Database.class);

        Button ad = findViewById(R.id.start);
        ad.setOnClickListener(clk -> startActivity(goToAD));


    }
}