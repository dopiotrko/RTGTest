package com.geodetka.rtgtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RTGActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Picasso mPicasso;
    private String name;
    private String surname;
    private String email;
    private String url;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtg);

        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");
        email = getIntent().getStringExtra("email");

        url = "http://www.rtgobrazki.ugu.pl/image01.jpg";

        ImageView RTGimg = findViewById(R.id.rtgImage);
        mPicasso = new PicassoModule().getPicasso(this);
        String url = "http://www.rtgobrazki.ugu.pl/image01.jpg";
        mPicasso.load(url)
                .placeholder(com.geodetka.rtgtest.R.color.placeholderColor)
                .error(com.geodetka.rtgtest.R.color.errorColor)
                .into(RTGimg, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
        startTime = System.currentTimeMillis();
    }

    protected void onClick(View v){
        Intent intent = new Intent(this, OdpActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("surname", surname);
        intent.putExtra("email", email);
        int rtgId = 1;
        intent.putExtra("rtgId", rtgId);
        long time = (System.currentTimeMillis() - startTime)/1000;
        intent.putExtra("time", time);
        startActivity(intent);
        this.finish();
    }
}
