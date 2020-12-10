package com.example.finalgroupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Delete_Page extends AppCompatActivity {
    // add shared prefs for text view
    String albumId;
    String album;
    String artist;
    Button listbtn;
    Button delete;
    Button help = findViewById(R.id.delhelp);
    ProgressBar pb = findViewById(R.id.delpb);
    JSONArray jsonArray;
    ArrayList<String> songList = new ArrayList<>();
    DeleteListAdapter delAdapter = new DeleteListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__page);

        help.setOnClickListener(clk->{
            Snackbar.make(help , "@string/delete_sb", Snackbar.LENGTH_LONG).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("@string/Help")
                    .setMessage("@string/Delete_help")
                    .setPositiveButton("@string/Back", (click, arg)->{

                    })

                    .create()
                    .show();
        });

        Intent intent = getIntent();
        album = intent.getStringExtra("album");
        albumId = intent.getStringExtra("albumId");
        artist = intent.getStringExtra("artist");

        TextView tv = findViewById(R.id.del_title);
        tv.setText(album);

        ListView delList = findViewById(R.id.delsong_list);
        delList.setAdapter(delAdapter);

        delete.setOnClickListener(clk->{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("@string/Delete")
                    .setMessage("@string/You_sure")
                    .setPositiveButton("@string/Yes", (click, arg)->{
                        Toast.makeText(this, "@string/delete_toast" , Toast.LENGTH_LONG).show();

                        Intent delData = new Intent();
                        delData.putExtra("album", album);
                        delData.putExtra("albumId", albumId);

                        setResult(500);
                        finish();
                    })

                    .setNegativeButton("@string/No", (click, arg)->{})

                    .create()
                    .show();

        });

        SongQuery sQ = new SongQuery();
        sQ.execute("https://theaudiodb.com/api/v1/json/1/track.php?m="+albumId);

        listbtn = findViewById(R.id.song_btn);
        listbtn.setOnClickListener(clk->{
            String url = "http://www.google.com/search?q=" + listbtn.getText() + artist;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });


    }

    class SongQuery extends AsyncTask< String, Integer, String> {

        String name;

        @Override
        public  String doInBackground(String... strings) {
            try{
                String encoded = URLEncoder.encode(strings[0], "UTF-8");

                URL url = new URL(encoded);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader  = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) !=null){
                    sb.append(line +"\n");
                }
                String result = sb.toString();

                JSONObject report = new JSONObject(result);

                publishProgress(25);

                jsonArray= report.getJSONArray("album");
                for(int i=0; i<jsonArray.length(); i++){
                    try{
                        publishProgress(50);

                        JSONObject obj = jsonArray.getJSONObject(i);
                        name = obj.getString("strTrack");

                        publishProgress(75);

                        songList.add(name);
                        delAdapter.notifyDataSetChanged();

                        publishProgress(100);

                    } catch(Exception e){

                    }
                }




            } catch (Exception e) {

            }
            return "done";
        }

        public void onProgressUpdate(Integer ... args){

            pb.setVisibility(View.VISIBLE);
            pb.setProgress(args[0]);
        }

        public void onPostExecute(String doInBackGround){


        }
    }

    class DeleteListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return songList.size();
        }

        @Override
        public Object getItem(int position) {
            return songList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;// fix when at database
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout. song_listview, parent, false);
            Button tv = row.findViewById(R.id.song_btn);
            tv.setText(getItem(position).toString());// try and get album name to go here

            return row;
        }
    }
}