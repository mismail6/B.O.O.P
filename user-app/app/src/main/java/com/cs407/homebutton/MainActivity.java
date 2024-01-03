package com.cs407.homebutton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String RASPBERRY_PI_URL =   "http://wifi public ip:port/Endpoint identifier";
    private static final String RASPBERRY_PI_URL_L = "http://raspberry pi local ip:port/Endpoint identifier";// Replace with your Raspberry Pi's IP

    private boolean isLocal;
    private static final String TOKEN = "*m4L2#PP1v1)("; // Use the same token as in Raspberry Pi code
    private Button btn;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);

        String A = "192.168"; // local wifi
        String B = "10.0.0";   // local wifi subnet
        String ip = getLocalIpAddress(this);
        isLocal = false;
        if ( ip!=null && isLocalURL(ip, A, B)) {
            isLocal = true;
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setEnabled(false);
                sendHttpRequest(v);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ip==null) {
                    Toast.makeText(MainActivity.this, "Not connected to home wifi",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, ip,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public String getLocalIpAddress(Context context) {
        try {
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (wm != null && wm.isWifiEnabled()) {
                WifiInfo wifiInfo = wm.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                //Log.d("IP IS SUCCESSFULLy GOTTEN: ", "IP IS SUCCESSFULLy GOTTEN:");
                return Formatter.formatIpAddress(ipAddress);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            //Log.d("IP IS BUGGY: ", "IP IS BUGGY");
            return null;
        }

    }

    public boolean isLocalURL(String ip, String A, String B) {
        if (ip.length() >= 7) { // Ensure A has at least 7 characters
            String firstSevenIP = ip.substring(0, 7); // Extract the first 7 characters of IP
            String firstSixIP = ip.substring(0, 6); // Extract the first 6 characters of IP
            if ((firstSevenIP.equals(A)) || (firstSixIP.equals(B)) || ip.equals("10.0.2.16")) { //10.0.2.16 was to test with android emulator
                //Log.d("Entered Local:", "Enetered Local???");
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    // Method triggered by button click in the layout file
    public void sendHttpRequest(View view) {
        new SendHttpRequest().execute();
    }

    private class SendHttpRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url;
                if (isLocal) {
                    url = new URL(RASPBERRY_PI_URL_L);
                    //Log.d("USING IP: ", RASPBERRY_PI_URL_L);
                }
                else {
                    url = new URL(RASPBERRY_PI_URL);
                    //Log.d("USING IP: ", RASPBERRY_PI_URL);
                }

                //Log.d("TRIAL 1", "TRAIL 1");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Token", TOKEN); // Set the token in request headers
                urlConnection.setDoOutput(true);
                //Log.d("TRIAL 2", "TRAIL 2");

                OutputStream outputStream = urlConnection.getOutputStream();
                // If you need to send data in the request body, you can write to the outputStream here
                //Log.d("TRIAL 2.5", "TRAIL 2.5");

                int responseCode = urlConnection.getResponseCode();
                // Handle responseCode if needed
                //Log.d("TRIAL 3", "TRAIL 3");
                urlConnection.disconnect();
                Thread.sleep(350);
                //Log.d("TRIAL 4", "TRAIL 4");
            } catch (IOException | InterruptedException e) {
                //Log.d("RAN INTO EXCEPTION", "EXCEPTION");
                //btn.setEnabled(true);
                //Log.d("TRIAL EXCEPTION", "TRAIL EXCEPTION");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            btn.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            btn.setEnabled(false);
        }

    }
}
