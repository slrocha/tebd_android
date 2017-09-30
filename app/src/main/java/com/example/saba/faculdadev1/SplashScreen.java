package com.example.saba.faculdadev1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;




public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private TextView textView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //ActionBar actionBar = this.getActionBar();
        //actionBar.hide();
        textView = (TextView) this.findViewById(R.id.textView);
        textView.setText("Aguarde Carregando.......");

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // close this activity

                new JSONParse().execute();

            }
        }, SPLASH_TIME_OUT);
    }

    public class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog= new ProgressDialog(SplashScreen.this);

            //  pDialog.setProgressStyle(R.style.CustomActionBarTheme);


            pDialog.setMessage("Obtendo Dados");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            // JSONParser jParser = null;
            JSONObject json = null;

            ClienteDAO clienteDAO = new ClienteDAO(SplashScreen.this);

            clienteDAO.dropAll();
            JSONArray link = null;

            // jParser = new JSONParser();


            json= Json();
            Log.i("Json", "json");
            int count=0;

            try {
                // Getting JSON Array
                link = json.getJSONArray("data");

                // disciplinaDAO.dropAll();
                //  json.getString("a"");
                for (int i = 0; i < link.length(); i++) {

                    JSONObject c = link.getJSONObject(i);
                    if (c != null) {

                        Log.i(" atualizado", c.toString());

                        ClienteValue clienteValue = new ClienteValue();

                        clienteValue.setNome(c.getString("nome"));
                        clienteValue.setTelefone(c.getString("telefone"));
                        clienteValue.setCpf(c.getString("cpf"));

                        clienteDAO.salvar(clienteValue);
                        Log.i("Salvar", clienteValue.toString());

                        clienteDAO.close();

                    } else{
                        Log.i("Não atualizado", "Não atulaizo");
                    }
                }
            }catch(Exception e){
                e.printStackTrace();

            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(pDialog != null) pDialog.dismiss();
                Intent i = new Intent(SplashScreen.this, FaculdadeActivity.class);
                startActivity(i);
                finish();
                // MainActivity.this.onResume();
            } catch (Exception e) {

            }
        }
    }


    public static JSONObject Json(){

        // JSONParser jParser = null;
        JSONObject json = null;
        String resp=null;

        try {
            // Create connection to send GCM Message request.
            URL url1 = new URL("http://10.0.1.8:8080/file.json");
//            Log.i("urll","atualziado");
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            // OutputStream outputStream = conn.getOutputStream();
            // outputStream.write(json.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            resp = IOUtils.toString(inputStream);
            Log.i("Teste", resp);
            json = new JSONObject("{"+resp+"}");

            // System.out.println(json.get("texto"));
            Log.i("Teste", json.toString());

            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
            return json;

        }catch (Exception e){
            e.printStackTrace();

        }

        return null;
    }

}