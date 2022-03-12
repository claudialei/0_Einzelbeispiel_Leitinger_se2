package com.example.leitinger_einzelbeispiel_se2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button abschicken = findViewById(R.id.btn_abschicken);

        abschicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mtrlNr = findViewById(R.id.et_mtrlNr);
                sendToServer(mtrlNr.getText().toString() + "\n");

            }
        });
    }

    public void sendToServer (String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("se2-isys.aau.at", 53212);
                    OutputStream output = socket.getOutputStream();

                    byte[] data = text.getBytes(StandardCharsets.UTF_8);
                    output.write(data);


                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String answer = reader.readLine();

                    socket.close();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView serverAnswer = findViewById(R.id.tv_antwortServer);
                            serverAnswer.setText(answer);
                            serverAnswer.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}