package com.geodetka.rtgtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class OdpActivity extends AppCompatActivity implements TaskCompleted{

    private String name;
    private String surname;
    private String email;
    private int rtg_id;
    private long time;
    private Button button;
    private TextView textFinal;
    private EditText answerText;

    @Override
    public void onTaskComplete(String result) {

        Toast.makeText(this,"The result is " + result ,Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odp);

        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");
        email = getIntent().getStringExtra("email");
        rtg_id = getIntent().getIntExtra("rtg_id", 0);
        time = getIntent().getLongExtra("time", 0);
        answerText = findViewById(R.id.answerText);
        textFinal = findViewById(R.id.textFinal);
        button = findViewById(R.id.button);
        TextView opis = findViewById(R.id.odpText);
        opis.setText(String.format("%s %s.\n%s %s %s\n%s",
                getString(R.string.odpOpis1),
                name,
                getString(R.string.odpOpis2),
                time,
                getString(R.string.odpOpis3),
                getString(R.string.odpOpis4)));

    }

    public void onClick(View view) {

        button.setText(getString(R.string.lastButtonLabel));
        textFinal.setText(getString(R.string.finalText));
        String answer = answerText.getText().toString();
        answerText.setFocusable(false);
        String jsonBody = "";

        try {
            jsonBody = new JSONObject()
                    .put("name", name)
                    .put("surrname", surname)
                    .put("email", email)
                    .put("time", time)
                    .put("rtg_id", rtg_id)
                    .put("answer", answer)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new getJson(OdpActivity.this)
                .execute("http://dopiotrko.heliohost.org/api/answer/create.php", jsonBody);
    }
}
