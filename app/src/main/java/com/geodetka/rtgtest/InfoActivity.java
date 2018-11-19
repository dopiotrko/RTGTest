package com.geodetka.rtgtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {
    private String name;
    private String surname;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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

        startActivity(intent);
        this.finish();
    }
}
