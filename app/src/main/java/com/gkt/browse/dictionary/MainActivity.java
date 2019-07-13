package com.gkt.browse.dictionary;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 0;
    private static final int SELECT_IMAGE =4;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    public static ArrayList<DataGenerater> data = new ArrayList<>();
   // private ShareActionProvider mShareActionProvider;

   private static final String SMS_SENT_INTENT_FILTER = "com.yourapp.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.yourapp.sms_delivered";

    static  public MyRecylerAdapter myRecylerAdapter;

    private SearchView searchView;

    private VolleyBackGround volleyBackGround;
    private ProgressBar progressBar;
    private boolean start = false;
    private GoogleAuthenticate googleAuthenticate;
    private int CAMERA_PIC_REQUEST = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        volleyBackGround = VolleyBackGround.getInstance(getApplicationContext());
       // volleyBackGround.makeJsonRequest("https://glosbe.com/gapi/translate?from=pol&dest=eng&format=json&phrase=witaj&pretty=true");
    //  translator();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myRecylerAdapter = new MyRecylerAdapter(getApplicationContext());
       relativeLayout = (RelativeLayout)findViewById(R.id.content);
        //View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_main,);
       recyclerView = (RecyclerView)findViewById(R.id.recycler);
       recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
       recyclerView.setAdapter(myRecylerAdapter);

       searchView = (SearchView)findViewById(R.id.search);



      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String s) {

              if(searchView.getQuery()!=null)
              searchBox(searchView.getQuery().toString());
              else
              {
                  Toast.makeText(getApplicationContext(),"wrong entry",Toast.LENGTH_LONG).show();
              }

              return true;
          }

          @Override
          public boolean onQueryTextChange(String s) {
              return false;
          }
      });

      searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
          @Override
          public boolean onSuggestionSelect(int i) {
              return false;
          }

          @Override
          public boolean onSuggestionClick(int i) {

              Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_LONG).show();

              return true;
          }
      });


      searchView.setOnCloseListener(new SearchView.OnCloseListener() {
          @Override
          public boolean onClose() {

              MainActivity.data.clear();
              myRecylerAdapter.notifyDataSetChanged();
              return true;
          }
      });

    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.cameraImport) {
            // Handle the camera action

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

        } else if (id == R.id.gallery) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);


        } else if (id == R.id.googleSignIn) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

            //for sharing link
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        } else if (id == R.id.nav_send) {
            String message = "hey, this is my message";

            String phnNo = "7479507037 "; //preferable use complete international number

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 5, new Intent(
                    SMS_SENT_INTENT_FILTER), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 5, new Intent(
                    SMS_DELIVERED_INTENT_FILTER), 0);

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phnNo, null, message, sentPI, deliveredPI);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //app's method

    private String dictionaryEntries(String word) {
        final String language = "en";
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required

        String url = "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
        Log.i("word",url);
        return  url;

    }

    private String  dictTranslator()
    {
        final String language = "en";
        final String target_lang = "hi";
        final String word = "Change";
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id + "/translations=" + target_lang;

    }

    private void callBackTask()
    {

        BackGroundTask backGroundTask = new BackGroundTask();

       // backGroundTask.execute(dictionaryEntries());
    }

    private void searchBox(String word)
    {
        data.clear();

        String url = dictionaryEntries(word);


        JsonObjectRequest jsonObjectRequest = volleyBackGround.makeJsonRequest(url);
        start = true;

        //volleyBackGround.addToRequestQueue(jsonObjectRequest,"headerRequest");

    }



  private void translator()
    {


        Translator translator = new Translator();
        String text= "go there";

        translator.execute(text);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            Log.i("Bitmap",image.toString());
           // ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
           // imageview.setImageBitmap(image);
            Toast.makeText(getApplicationContext(),"loaded",Toast.LENGTH_SHORT).show();
        }

        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        Toast.makeText(getApplicationContext(),"loaded",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED )  {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
