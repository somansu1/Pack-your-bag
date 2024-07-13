package com.example.packyourbag;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutUs extends AppCompatActivity {

    ImageView imgYoutube,imgInstagram,imgTwitter;
    TextView txtEmail,txtWebsiteUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About us");

        imgYoutube=findViewById(R.id.imgYoutube);
        imgInstagram=findViewById(R.id.imgInstagram);
        imgTwitter=findViewById(R.id.imgTwiter);
        txtEmail=findViewById(R.id.txtEmail);
        txtWebsiteUrl=findViewById(R.id.txtWebsiteUrl);

        imgYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUrl("http://www.youtube.com/btechdays");
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:+btechdays.care@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "From Back Your Bag");
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    System.out.println(e);
                }
            }
        });

        txtWebsiteUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUrl("http://www.btechdays.com");

            }
        });

        imgInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUrl("http://instagram.com/btechdays?igshid=NDk5N2NlZjQ=");

            }
        });

        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUrl("http://twitter.com/Btechdays?=rJ11wlOPC8Dqzdj_DmpbdA&s=09");
            }
        });


    }

    private void navigateToUrl(String url){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

}