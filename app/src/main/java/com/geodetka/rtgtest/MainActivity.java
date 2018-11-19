package com.geodetka.rtgtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextButton;
    private EditText NameEdit;
    private EditText SurnameEdit;
    private EditText emailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        NameEdit = findViewById(R.id.NameEdit);
        SurnameEdit = findViewById(R.id.SurnameEdit);
        emailEdit = findViewById(R.id.emailEdit);
    }

    @Override
    public void onClick(View v) {

        String name = NameEdit.getText().toString().trim();
        String surname = SurnameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();

        NameEdit.setFocusable(false);
        SurnameEdit.setFocusable(false);
        emailEdit.setFocusable(false);
        nextButton.setEnabled(false);

        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("surname", surname);
        intent.putExtra("email", email);

        startActivity(intent);
    }

    public void onExit(View v){
        this.finish();
    }

}
