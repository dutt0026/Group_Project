package com.example.finalgroupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Details_Page extends AppCompatActivity {

    private SQLiteDatabase db;
    MyOpener dbOpener;
    ProgressBar pb = findViewById(R.id.spb);
    Button save;
    Button songBtn;
    Button help = findViewById(R.id.dhelp);
    String songName;
    String albumId;
    JSONArray jsonArray;
    private ArrayList<String> songs = new ArrayList<>();
    private MySongListAdapter songAdapter = new MySongListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__page);

        SongQuery sQ = new SongQuery();

        help.setOnClickListener(clk->{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("@string/Help")
                    .setMessage("@string/Details_help")
                    .setPositiveButton("@string/Back", (click, arg)->{

                    })

                    .create()
                    .show();
        });

        Intent intent = getIntent();
        String artist = intent.getStringExtra("artist");
        String title = intent.getStringExtra("album");
        albumId = intent.getStringExtra("albumId");
        TextView tv = findViewById(R.id.title);
        tv.setText(title);

        sQ.execute("https://theaudiodb.com/api/v1/json/1/track.php?m="+albumId);

        save = findViewById(R.id.save);
        save.setOnClickListener(clk -> {
            // below loads information into database
            ContentValues newRowValues = new ContentValues();

            newRowValues.put(MyOpener.ALBUM, title);
            newRowValues.put(MyOpener.ALBUM_ID, albumId);
            db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Toast.makeText(this, "@string/details_toast" , Toast.LENGTH_LONG).show();
        });

        ListView sl = findViewById(R.id.song_list);
        sl.setAdapter(songAdapter);


        songBtn = findViewById(R.id.song_btn);
        songBtn.setOnClickListener(clk -> {
            Snackbar.make(songBtn , "@string/details_sb", Snackbar.LENGTH_LONG).show();
            String url = "http://www.google.com/search?q=" + songBtn.getText() + artist;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });


    }


    static class MyOpener extends SQLiteOpenHelper {

        protected final static String DATABASE_NAME = "AudioDB";// clean up unused cols
        protected final static int VERSION_NUM = 1;
        public final static String TABLE_NAME = "Audio";
        public final static String ALBUM = "Album";
        public final static String ALBUM_ID = "Album Id";
        public final static String ARTIST = "Artist";
        public final static String COL_ID = "_id";

        public MyOpener(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            // Creates database
            db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ARTIST + "TEXT,"//might not need this col
                    + ALBUM + "TEXT,"
                    + ALBUM_ID + "TEXT);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            onCreate(db);

            db.insert(TABLE_NAME, null, new ContentValues());
        }
    }


    class MySongListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Object getItem(int position) {
            return songs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;// fix when at database
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.song_listview, parent, false);
            songBtn.setText(getItem(position).toString());

            return row;
        }
    }

    class SongQuery extends AsyncTask<String, Integer, String> {

        String name;

        @Override
        public String doInBackground(String... strings) {// might have to encode urls Async video minute 56
            try {
                String encoded = URLEncoder.encode(strings[0], "UTF-8");// might only have to be the user input encoded

                URL url = new URL(encoded);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject report = new JSONObject(result);

                jsonArray = report.getJSONArray("album");
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        name = obj.getString("strTrack");

                        publishProgress(25);
                        publishProgress(50);
                        publishProgress(75);

                        songs.add(name);
                        songAdapter.notifyDataSetChanged();

                        publishProgress(100);

                    } catch (Exception e) {

                    }
                }


            } catch (Exception e) {

            }
            return "done";
        }

        public void onProgressUpdate(Integer... args) {

            pb.setVisibility(View.VISIBLE); // pb is progress bar to be made
            pb.setProgress(args[0]);
        }

        public void onPostExecute(String doInBackGround) {


        }
    }

}
