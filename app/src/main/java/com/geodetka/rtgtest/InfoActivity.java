package com.geodetka.rtgtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class InfoActivity extends AppCompatActivity implements TaskCompleted{
    private String name;
    private String surname;
    private String email;
    private String rtg_url;
    private int rtg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        new getJson(InfoActivity.this)
                .execute("http://www.rtgobrazki.ugu.pl/api/rtg/read.php", "");
        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");
        email = getIntent().getStringExtra("email");

        TextView opis = findViewById(R.id.opis);
        opis.setText(String.format("%s\n%s %s\n%s.\n%s",
                getString(R.string.opis1), name, surname, email, getString(R.string.opis2)));
    }

    public void onClick(View v) {

        Intent intent = new Intent(this, RTGActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("surname", surname);
        intent.putExtra("email", email);
        intent.putExtra("rtg_url", rtg_url);
        intent.putExtra("rtg_id", rtg_id);

        startActivity(intent);
        this.finish();
    }

    @Override
    public void onTaskComplete(String result) {

        try {
            JSONObject rtgsJsonObj = new JSONObject(result);
            JSONArray rtgsJsonArray = rtgsJsonObj.getJSONArray("records");
            int rand = new Random().nextInt(rtgsJsonArray.length());
            rtg_url = rtgsJsonArray.getJSONObject(rand).getString("rtg_url");
            rtg_id = rtgsJsonArray.getJSONObject(rand).getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Button button = findViewById(R.id.startButton);
        button.setFocusable(true);
    }
}
