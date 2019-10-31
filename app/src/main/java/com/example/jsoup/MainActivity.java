package com.example.jsoup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private final textGrabber textgrab = new textGrabber();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textgrab.getWebsite("home");
                try {
                    while (textgrab.getTextContent() == "Page hasn't finished loading.")
                        TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textView.setText(textgrab.getTextContent());
    }

    public void onClick(View view){
        Button clickedbtn = findViewById(view.getId());
        final String page = clickedbtn.getText().toString();
        Toast.makeText(getBaseContext(), "Loading data from " + page, Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textgrab.getWebsite(page);
                try {
                    TimeUnit.SECONDS.sleep(1);
                    while (textgrab.getTextContent() == "Page hasn't finished loading.")
                        TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
                textView.setText(textgrab.getTextContent());
            }
        });
    }
}
