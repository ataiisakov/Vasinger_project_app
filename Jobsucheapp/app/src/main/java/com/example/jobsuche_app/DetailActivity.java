package com.example.jobsuche_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import static com.example.jobsuche_app.ListJobsActivity.EXTRA_BUNDESLAND;
import static com.example.jobsuche_app.ListJobsActivity.EXTRA_DESC;
import static com.example.jobsuche_app.ListJobsActivity.EXTRA_IMG_URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra(EXTRA_IMG_URL);
        String descr = intent.getStringExtra(EXTRA_DESC);
        String bundesland = intent.getStringExtra(EXTRA_BUNDESLAND);

        ImageView imageView = findViewById(R.id.img_detail);
        TextView textViewDesc = findViewById(R.id.desc_detail);
        TextView textViewBundesland = findViewById(R.id.land_detail);

        Picasso.get().load(imgUrl).fit().centerInside().into(imageView);
        textViewDesc.setText(descr);
        textViewBundesland.setText(bundesland);
    }
}
