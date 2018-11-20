package com.geodetka.rtgtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class OdpActivity extends AppCompatActivity implements TaskCompleted{

    private String name;
    private String surname;
    private String email;
    private String answer;
    private int rtgID;
    private long time;
    private Button button;
    private Boolean sent = false;
    private TextView textFinal;
    private TextView opis;
    private EditText answerText;

    @Override
    public void onTaskComplete(String result) {

        Toast.makeText(this,"The result is " + result ,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odp);

        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");
        email = getIntent().getStringExtra("email");
        rtgID = getIntent().getIntExtra("rtg_id", 0);
        time = getIntent().getLongExtra("time", 0);
        answerText = findViewById(R.id.answerText);
        textFinal = findViewById(R.id.textFinal);
        button = findViewById(R.id.button);
        opis = findViewById(R.id.odpText);
        opis.setText(String.format("%s %s.\n%s %s %s\n%s",
                getString(R.string.odpOpis1),
                name,
                getString(R.string.odpOpis2),
                time,
                getString(R.string.odpOpis3),
                getString(R.string.odpOpis4)));

    }

    public void onClick(View view) {
        if(!sent) {
            button.setText(getString(R.string.lastButtonLabel));
            textFinal.setText(getString(R.string.finalText));
            answer = answerText.getText().toString();
            answerText.setFocusable(false);
            new getJson(OdpActivity.this).execute("http://www.rtgobrazki.ugu.pl/api/rtg/read.php");
            //       answerText.setText(rtgJsonString);
            sent = true;
        }
        else {
            this.finish();
        }
    }
}
