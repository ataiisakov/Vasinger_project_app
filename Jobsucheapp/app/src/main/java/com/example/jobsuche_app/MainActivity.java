package com.example.jobsuche_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    public static final String EXTRA_IMG_URL = "imgUrl";
    public static final String EXTRA_DESC = "description" ;
    public static final String EXTRA_BUNDESLAND= "bundesland";

    private RecyclerView mRecycleView;
    private MyAdapter myAdapter;
    private ArrayList<Job> mJobList;
    RequestQueue requestQueue;

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecycleView = findViewById(R.id.recycle_view);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        requestQueue = Volley.newRequestQueue(this);
        mJobList = new ArrayList<>();
        new GetContacts().execute();
    }




    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this,DetailActivity.class);
        Job clickedItem = mJobList.get(position);

        detailIntent.putExtra(EXTRA_IMG_URL,clickedItem.getImgUrl());
        detailIntent.putExtra(EXTRA_DESC,clickedItem.getDescription());
        detailIntent.putExtra(EXTRA_BUNDESLAND,clickedItem.getBundesland());

        startActivity(detailIntent);
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
            adapter.setOnItemClickListener(MainActivity.this);

        }
    }
}
