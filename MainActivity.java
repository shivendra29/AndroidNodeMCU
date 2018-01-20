package com.example.shivendra.iot;


import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText InputIp;
    private Button update;
    private Button OnButt;
    private Button OffButt;
    private TextView Status;

    String NewIP;
    String Power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputIp = findViewById(R.id.inIP);
        update = findViewById(R.id.update);
        OnButt = findViewById(R.id.OnButt);
        OffButt = findViewById(R.id.OffButt);
        Status = findViewById(R.id.status);

        final String TAG = "MainActivity";

       class HttpGetRequest extends AsyncTask <String,Void,String> {
           @Override
           protected String doInBackground(String... params) {
                String strURL = params[0];
                String result = null;



                try {
                    URL url = new URL(strURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Content-type", "text/html");
                    con.setReadTimeout(1500);
                    con.setConnectTimeout(1500);

                    try{
                        int status = con.getResponseCode();

                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer content = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        System.out.println(content.toString());
                        in.close();
                    }
                    catch(SocketException e){
                        System.out.println(e.getMessage());
                    }
                    con.disconnect();

                    return result;
                }
                catch(IOException e){
                    e.printStackTrace();
                    return null;
                }
           }

           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);
           }
       }



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewIP = InputIp.getText().toString();
                if (NewIP.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter IP", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Status.setText(null);

                }
            }
        });

        OnButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputIp.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter IP!", Toast.LENGTH_SHORT).show();
                    Status.setText(null);
                } else {
                    Power = "http://" + NewIP + "/LED=ON";
                    try {
                        new HttpGetRequest().execute(Power).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Status.setText("LED ON");
                }
            }
        });

        OffButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputIp.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter IP!", Toast.LENGTH_SHORT).show();
                    Status.setText(null);
                } else {
                    Power = "http://" + NewIP + "/LED=OFF";
                    new HttpGetRequest().execute(Power);
                    Status.setText("LED OFF");
                }

            }
        });


    }


}