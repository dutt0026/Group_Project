package com.example.finalgroupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.finalgroupproject.Details_Page.MyOpener.TABLE_NAME;

public class Saved_Album extends AppCompatActivity {
    public ArrayList<String> dbra = new ArrayList<>();
    public ArrayList<String> idIndex = new ArrayList<>();
    SQLiteDatabase db;
    SavedListAdapter saAdapter = new SavedListAdapter();
    ListView list;

    String albumId;
    String artist;
    Button help = findViewById(R.id.shelp);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved__album);

        loadDataFrom();

        Toast.makeText(this, "@string/saved_toast" , Toast.LENGTH_LONG).show();

        help.setOnClickListener(clk->{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("@string/Help")
                    .setMessage("@string/Saved_help")
                    .setPositiveButton("@string/Back", (click, arg)->{

                    })

                    .create()
                    .show();
        });

        Intent fromAudio = getIntent();
        artist = fromAudio.getStringExtra("artist");

        list = findViewById(R.id.sa_list);
        list.setAdapter(saAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
                Intent goToDelete = new Intent(Saved_Album.this, Delete_Page.class);
                goToDelete.putExtra("album",dbra.get(position));
                goToDelete.putExtra("albumId", idIndex.get(position));
                goToDelete.putExtra("artist", artist);
                startActivityForResult(goToDelete,100);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 500){
            Snackbar.make(help , "@string/saved_sb", Snackbar.LENGTH_LONG).show();
            String pos = data.getStringExtra("album");
            String posId = data.getStringExtra("albumId");

            db.delete(Details_Page.MyOpener.TABLE_NAME, Details_Page.MyOpener.ALBUM + " = ? " + Details_Page.MyOpener.ALBUM_ID+" = ? ", new String[] {pos, posId});
            dbra.remove(pos);
            idIndex.remove(posId);
            saAdapter.notifyDataSetChanged();
        }
    }



    private void loadDataFrom() { // make sure querys are on the right page do shared prefs add help button and add finishing touches.

        // makes db connection
        Details_Page.MyOpener dbOpener = new Details_Page.MyOpener(this);
        db = dbOpener.getWritableDatabase(); // SQL Video 45 min.

        String[] cols = {Details_Page.MyOpener.ALBUM, Details_Page.MyOpener.ALBUM_ID};
        Cursor results = db.query( TABLE_NAME, cols, null, null, null, null,null, null);

        int albumNameIndex = results.getColumnIndex(Details_Page.MyOpener.ALBUM);
        int albumIdIndex = results.getColumnIndex(Details_Page.MyOpener.ALBUM_ID);

       while(results.moveToNext()){

           String dbr = results.getString(albumNameIndex);
           String aidr = results.getString(albumIdIndex);
           dbra.add(dbr);
           idIndex.add(aidr);
       }

    }

    class SavedListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dbra.size();
        }

        @Override
        public Object getItem(int position) {
            return dbra.get(position);
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
            tv.setText(getItem(position).toString());// try and get album name to go here

            return row;
        }
    }

}