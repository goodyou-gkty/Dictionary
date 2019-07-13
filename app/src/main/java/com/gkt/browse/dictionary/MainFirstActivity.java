package com.gkt.browse.dictionary;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class MainFirstActivity extends AppCompatActivity {

    private boolean isLinked=false;
    private boolean toLogin = true;
    private FirebaseAuth mAuth;
    private AuthCredential credential= LoginActivity.credential;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main_first);
        int secondsDelayed = 1;
        Class<?> destinaiton = LoginActivity.class;
        //Log.i("credential",credential.toString());
       // Log.i("FireBaseAuth",mAuth.toString());
        linkCredentialToFirebase();
        if(isLinked==false && mAuth!=null && credential!=null) {
            FirebaseUser prevUser = FirebaseAuth.getInstance().getCurrentUser();
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser currentUser = task.getResult().getUser();
                            // Merge prevUser and currentUser accounts and data
                            // ...
                            toLogin = false;

                        }
                    });
        }



        new Handler().postDelayed(new Runnable() {

            public void run() {
                startActivity(new Intent(MainFirstActivity.this,toLogin?LoginActivity.class:MainActivity.class));
                finish();
            }
        }, secondsDelayed * 5000);
    }


    private void linkCredentialToFirebase()
    {

        if(mAuth!=null && credential!=null) {
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Linking", "linkWithCredential:success");
                                FirebaseUser user = task.getResult().getUser();
                                isLinked = true;
                                // updateUI(user);
                            } else {
                                Log.w("not linkable", "linkWithCredential:failure", task.getException());
                                Toast.makeText(MainFirstActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

}

