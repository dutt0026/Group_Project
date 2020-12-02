package com.example.finalgroupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Audio_Database extends AppCompatActivity {

    MyListAdapter adapter = new MyListAdapter();
    private ArrayList<String> content = new ArrayList<>();
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio__database);

        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);

        et = findViewById(R.id.art_search);
        Button b = findViewById(R.id.search);
        b.setOnClickListener(clk ->{
            TextView result = findViewById(R.id.results);
            result.setText("@string/results "+ et.getText() +": ");//maybe try and get from database or array
        });
    }

class MyListAdapter extends BaseAdapter{

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;// fix when at database
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = getLayoutInflater();
        View row = inflater.inflate(R.layout. listview_layout, parent, false);
        TextView tv = row.findViewById(R.id.album);
        tv.setText();//figure out how to get the name of the albums

        return row;
    }
}
}