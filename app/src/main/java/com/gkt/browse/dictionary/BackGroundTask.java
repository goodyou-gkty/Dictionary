package com.gkt.browse.dictionary;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class BackGroundTask extends AsyncTask<String, Integer, String> {

    DataGenerater dataGenerater;
    @Override
    protected String doInBackground(String... strings) {
        {


            final String app_id ="1d794bc6";
            final String app_key = "163a0c117862ddf3eea249e217e64ba2";
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }

        }


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        String def="";
        String domains="";
        String exampleDefs = "";


        try {

            JSONObject js = new JSONObject(s);
            JSONArray results = js.getJSONArray("results");
            for(int i = 0;i<results.length();i++){
                JSONObject lentries = results.getJSONObject(i);
                JSONArray la = lentries.getJSONArray("lexicalEntries");
                for(int j=0;j<la.length();j++){
                    JSONObject entries = la.getJSONObject(j);
                    JSONArray e = entries.getJSONArray("entries");
                    for(int i1=0;i1<e.length();i1++){
                        JSONObject senses = la.getJSONObject(i1);
                        JSONArray ss = entries.getJSONArray("senses");
                        JSONObject d = ss.getJSONObject(0);
                        JSONArray de = d.getJSONArray("definitions");
                        JSONArray domain = d.getJSONArray("domains");
                        JSONArray example = d.getJSONArray("examples");
                        JSONObject exampl1 = example.getJSONObject(0);
                        JSONArray exampleDef = exampl1.getJSONArray("definitions");
                        def = de.getString(0);
                        domains = domain.getString(0);
                        exampleDefs = exampleDef.getString(0);
                    }
                }
            }
            Log.e("def",def);
            Log.e("domains",domains);
            Log.e("exampleDefs",exampleDefs);

         //   dataGenerater = new DataGenerater(def,domains,exampleDefs);
           // MainActivity.data.add(dataGenerater);

        } catch (JSONException e) {
            e.printStackTrace();
        }

      //  System.out.println(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
