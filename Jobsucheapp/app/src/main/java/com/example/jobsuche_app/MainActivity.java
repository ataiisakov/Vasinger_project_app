package com.example.jobsuche_app;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    ArrayList<HashMap<String, Object>> jobsList;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    Spinner spinnerArt;
    Spinner spinnerFeld;
    Spinner spinnerLand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jobsList = new ArrayList<>();
//        lv = findViewById(R.id.list);
        spinnerArt = findViewById(R.id.spinner_art);
        spinnerFeld = findViewById(R.id.spinner_berufsfeld);
        spinnerLand = findViewById(R.id.spinner_bundesland);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.art_array,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArt.setOnItemSelectedListener(this);
        spinnerFeld.setOnItemSelectedListener(this);
        spinnerLand.setOnItemSelectedListener(this);
//        new GetJobs().execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = (String) parent.getItemAtPosition(position);
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void findJob(View view) {
        setContentView(R.layout.list_item);
    }


    private class GetJobs extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        public Drawable LoadImageFromWebOperations(String url) {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                return d;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://www.wikway.de/companies/offers-json?password=ain1018";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);


                    // looping through All Contacts
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject c = jsonArr.getJSONObject(i);

                        String bezeichnung = c.getString("Bezeichnung der Stelle");
//                        String name = c.getString("name");
                        String bundesland = c.getString("Bundesland");
//                        String gender = c.getString("gender");
                        String imageUrl = c.getString("Logo");


                        // tmp hash map for single contact
                        HashMap<String, Object> job = new HashMap<>();
                        // adding each child node to HashMap key => value
                        job.put("bezeichnung", bezeichnung);
                        //job.put("image", String.valueOf(bmp));
                        job.put("bundesland", bundesland);
                        job.put("imageUrl", imageUrl);

                        // adding contact to contact list
                        jobsList.add(job);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, jobsList,
                    R.layout.list_item, new String[]{"imageUrl", "bezeichnung", "bundesland"},
                    new int[]{R.id.imageLogo, R.id.textDesc, R.id.textLand});

            lv.setAdapter(adapter);
        }
    }


}
