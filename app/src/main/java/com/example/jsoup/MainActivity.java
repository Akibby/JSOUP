package com.example.jsoup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final textGrabber textgrab = new textGrabber();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownloadTextTask().execute("home");
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... page){
            textgrab.getWebsite(page[0]);
            return textgrab.getTextContent();
        }

        protected void onPostExecute(String result){
            TextView textView = findViewById(R.id.tv);
            textView.setText(result);
        }

    }

    public void onClick(View view){
        Button clickedbtn = findViewById(view.getId());
        final String page = clickedbtn.getText().toString();
        Toast.makeText(getBaseContext(), "Loading data from " + page, Toast.LENGTH_SHORT).show();
        new DownloadTextTask().execute(page);
    }
}
