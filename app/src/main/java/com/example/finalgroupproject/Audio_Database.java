package com.example.finalgroupproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Audio_Database extends AppCompatActivity {

    SharedPreferences prefs = null;
    MyListAdapter adapter = new MyListAdapter();
    private ArrayList<Object> content = new ArrayList<>();
    EditText et;
    ListView list;
    Button help = findViewById(R.id.help);
    ProgressBar pb = findViewById(R.id.prog_bar);
    String artist;
    JSONArray jsonArray;
    String albumName;
    String albumId;
    String song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio__database);

        prefs = getSharedPreferences("Audio", Context.MODE_PRIVATE);
        String saved = prefs.getString("savedET", null);
        et.setText(saved);

        AudioQuery request = new AudioQuery();

        // might have add s to http. Async video 1:02:00

        help.setOnClickListener(clk->{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("@string/Help")
                    .setMessage("@string/Audio_help")
                    .setPositiveButton("@string/Back", (click, arg)->{

                    })

                    .create()
                    .show();
        });

        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        //sends to Details page
        list.setOnItemClickListener( (parent, view, position, id) -> {
            Intent goToDetails = new Intent(Audio_Database.this, Details_Page.class);
            goToDetails.putExtra("album",content.get(position).toString());
            goToDetails.putExtra("artist", et.getText());
            goToDetails.putExtra("albumID", albumId);
            startActivity(goToDetails);
        });



        list.setOnItemLongClickListener((parent, view, position, id) -> {
            Toast.makeText(this, "@string/audio_toast" + content.get(position) + "ID:" + albumId, Toast.LENGTH_LONG).show();
            return true;
        } );

        et = findViewById(R.id.art_search);
        Button b = findViewById(R.id.search);
        b.setOnClickListener(clk ->{
            EDSaved(et.getText().toString());
            request.execute("https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s="+ et.getText());
            TextView result = findViewById(R.id.results);
            result.setText("@strings/results "+ et.getText() +": ");
        });

        Button ssa = findViewById(R.id.ssa);
        ssa.setOnClickListener(clk->{
            Intent goToSavedAlbum = new Intent(Audio_Database.this, Saved_Album.class);
            goToSavedAlbum.putExtra("artist", et.getText());
            startActivity(goToSavedAlbum);
        });
    }

    private void EDSaved ( String str){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedET", str);
        editor.commit();
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
        tv.setText(getItem(position).toString());

        return row;
    }
}

class AudioQuery extends AsyncTask< String, Integer, String>{

        String id;
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

            jsonArray= report.getJSONArray("album");// figure out how to do this for different pages
            for(int i=0; i<jsonArray.length(); i++){
                try{
                    JSONObject obj = jsonArray.getJSONObject(i);
                        id = obj.getString("idAlbum");
                        name = obj.getString("strAlbum");

                        publishProgress(25);
                        publishProgress(50);
                        publishProgress(75);


                        content.add(name);
                        adapter.notifyDataSetChanged();

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

        albumId = id;
    }
}

}

