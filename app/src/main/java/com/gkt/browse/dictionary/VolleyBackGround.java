package com.gkt.browse.dictionary;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VolleyBackGround {

        private static VolleyBackGround instance;
        private RequestQueue requestQueue;
        private ImageLoader imageLoader;
        private static Context ctx;
        private DataGenerater dataGenerater;
        private boolean stops = false;

        private VolleyBackGround(Context context) {
            ctx = context;
            requestQueue = getRequestQueue();

            imageLoader = new ImageLoader(requestQueue,
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap>
                                cache = new LruCache<String, Bitmap>(20);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }

        public static synchronized VolleyBackGround getInstance(Context context) {
            if (instance == null) {
                instance = new VolleyBackGround(context);
            }
            return instance;
        }

        public RequestQueue getRequestQueue() {
            if (requestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            }
            return requestQueue;
        }


    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

        public ImageLoader getImageLoader() {
            return imageLoader;
        }


        public JsonObjectRequest makeJsonRequest(String url)
        {

            final String app_id ="1d794bc6";
            final String app_key = "163a0c117862ddf3eea249e217e64ba2";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                       (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                           @Override
                           public void onResponse(JSONObject response) {
                               //textView.setText("Response: " + response.toString());

                               stops = true;

                               Log.i("json",response.toString());

                               try {
                                   generateJSON(response);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }


                           }
                       }, new Response.ErrorListener() {

                           @Override
                           public void onErrorResponse(VolleyError error) {
                               // TODO: Handle error

                               Log.i("json",error.toString());

                               stops = true;
                               Toast.makeText(ctx,"Search correct word",Toast.LENGTH_LONG).show();
                               MainActivity.myRecylerAdapter.notifyDataSetChanged();

                           }
                       }){


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("app_id", app_id);
                    headers.put("app_key", app_key);

                    return headers;
                }
            };

            VolleyBackGround.getInstance(ctx).addToRequestQueue(jsonObjectRequest,"headerRequest");

               return jsonObjectRequest;

        }

        public  StringRequest makeRequest(String url)
        {

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                           // textView.setText("Response is: "+ response.substring(0,500));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // textView.setText("That didn't work!");
                }
            });

            return stringRequest;
        }

        public void generateJSON(JSONObject jsonObject) throws JSONException {

            String text = "";
            String definition="";
            String domain="";
            String type = "";
            String example = "";
            String etym="";

            JSONArray results = jsonObject.getJSONArray("results");

            for(int i = 0;i<results.length();i++){

                JSONObject lentries = results.getJSONObject(i);

                JSONArray la = lentries.getJSONArray("lexicalEntries");

                for(int j=0;j<la.length();j++){

                    JSONObject entries = la.getJSONObject(j);

                    JSONArray e = entries.getJSONArray("entries");

                    for(int i1=0;i1<e.length();i1++){

                        JSONObject entryi = e.getJSONObject(i);

                        if(entryi.has("etymologies"))
                        {
                            JSONArray etymology = entryi.getJSONArray("etymologies"); //array of string

                            Log.i("etymology",etymology.getString(0));

                            etym = etymology.getString(0);

                            Log.i("etymology",etym);
                        }

                        if(entryi.has("grammaticalFeatures"))
                        {
                            JSONArray grammaticalFeatures = entryi.getJSONArray("grammaticalFeatures");

                            Log.i("grammaticalFeatures",grammaticalFeatures.getString(0));

                            JSONObject grammaticalFeaturesi = grammaticalFeatures.getJSONObject(0);

                            text = grammaticalFeaturesi.getString("text");

                            Log.i("text",text);

                            type = grammaticalFeaturesi.getString("type");

                            Log.i("Type",type);
                        }// array of string


                        if(entryi.has("homographNumber")) {

                            String homographNumber = entryi.getString("homographNumber");

                            Log.i("homographNumber",homographNumber);

                        }

                        if(entryi.has("senses"))
                        {
                            JSONArray senses = entryi.getJSONArray("senses");

                            JSONObject sensesi = senses.getJSONObject(0);

                            if(sensesi.has("definitions")) {

                                JSONArray definitions = sensesi.getJSONArray("definitions");

                                 definition = definitions.getString(0);

                                Log.i("definitions", definition);
                            }

                            if(sensesi.has("domains"))

                            {
                                JSONArray domains = sensesi.getJSONArray("domains");

                                domain = domains.getString(0);

                                Log.i("domains", domain);
                            }

                            if(sensesi.has("examples"))
                            {
                                JSONArray examples = sensesi.getJSONArray("examples");

                                JSONObject examplesi = examples.getJSONObject(0);

                                example= examplesi.getString("text");

                                Log.i("Example", example);
                            }
                        }


                        dataGenerater = new DataGenerater(definition,domain,example,etym,text,type);

                        MainActivity.data.add(dataGenerater);
                          MainActivity.myRecylerAdapter.notifyDataSetChanged();



                    }
                }
            }

        }

    }

