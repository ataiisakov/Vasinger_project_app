package com.example.jobsuche_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    Spinner spinnerBeruf;
    Spinner spinnerLand;
    Spinner spinnerArt;

    public static final String EXTRA_ART = "art";
    public static final String EXTRA_BERUFSFELD_TEXT= "berufsfeld";
    public static final String EXTRA_LAND= "bundesland";
    public static final String EXTRA_DEFAULT_URL= "all_jobs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ListJobsActivity.url = "https://www.wikway.de/companies/offers-json?password=ain1018";

        spinnerArt = findViewById(R.id.spinner_art);
        spinnerBeruf = findViewById(R.id.spinner_beruf);
        spinnerLand = findViewById(R.id.spinner_land);
    }

    public void findJobs(View view) {
        Intent intent = new Intent(this, ListJobsActivity.class);
        intent.putExtra(EXTRA_ART,spinnerArt.getSelectedItem().toString());
        intent.putExtra(EXTRA_BERUFSFELD_TEXT,spinnerBeruf.getSelectedItem().toString());
        intent.putExtra(EXTRA_LAND,spinnerLand.getSelectedItem().toString());
        startActivity(intent);
    }

    public void showAll(View view) {
        Intent intent = new Intent(this,ListJobsActivity.class);
        intent.putExtra(EXTRA_DEFAULT_URL,"https://www.wikway.de/companies/offers-json?password=ain1018");
        startActivity(intent);
    }
}
