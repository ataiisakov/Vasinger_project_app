package com.example.jobsuche_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView mRecycleView;
    private MyAdapter myAdapter;
    private ArrayList<Job> mJobList;
    RequestQueue requestQueue;

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
//        spinnerArt = findViewById(R.id.spinner_art);
//        spinnerFeld = findViewById(R.id.spinner_berufsfeld);
//        spinnerLand = findViewById(R.id.spinner_bundesland);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.art_array,android.R.layout.simple_spinner_dropdown_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerArt.setOnItemSelectedListener(this);
//        spinnerFeld.setOnItemSelectedListener(this);
//        spinnerLand.setOnItemSelectedListener(this);


        mRecycleView = findViewById(R.id.recycle_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        requestQueue = Volley.newRequestQueue(this);
        mJobList = new ArrayList<>();
//        parseJSON();
        new GetContacts().execute();
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
        setContentView(R.layout.activity_main);
    }

    private void parseJSON(){
        final String url = "https://www.wikway.de/companies/offers-json?password=ain1018";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray(url);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                String logo = jObj.getString("Logo");
                                String bundesland = jObj.getString("Bundesland");
                                String bezeichnung = jObj.getString("Bezeichnung der Stelle");
                                mJobList.add(new Job(logo,bezeichnung,bundesland));

                            }
                            myAdapter = new MyAdapter(MainActivity.this,mJobList);
                            mRecycleView.setAdapter(myAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
        });
    requestQueue.add(request);
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

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
//                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String img = c.getString("Logo");
                        String bezeichnung_der_stelle = c.getString("Bezeichnung der Stelle");
                        String bundesland = c.getString("Bundesland");




                        // adding contact to contact list
                        mJobList.add(new Job(img,bezeichnung_der_stelle,bundesland));
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
            MyAdapter adapter = new MyAdapter(MainActivity.this,mJobList);
            mRecycleView.setAdapter(adapter);
        }
    }
}
