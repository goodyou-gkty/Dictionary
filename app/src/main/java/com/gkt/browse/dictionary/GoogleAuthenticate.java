package com.gkt.browse.dictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.support.constraint.Constraints.TAG;

public class GoogleAuthenticate {

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private Context context;
    private MainActivity mainActivity;
    GoogleSignInAccount account;
    private int RC_SIGN_IN=0;

    GoogleAuthenticate()
    {
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(""/*getString(R.string.default_web_client_id)*/)
                .requestEmail()
                .build();
         mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

    }

    public void signIn()
    {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mainActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public boolean isExistingUser()
    {
        account = GoogleSignIn.getLastSignedInAccount(context);
        if(account!=null)
            return true;
        else
            return false;
    }

    /*adding signIn button
    *  <com.google.android.gms.common.SignInButton
 android:id="@+id/sign_in_button"
 android:layout_width="wrap_content"
 android:layout_height="wrap_content" />
    * */

    public void profileInfo()
    {
       GoogleSignInAccount  account = GoogleSignIn.getLastSignedInAccount((Activity)context);
        if (account!= null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
         //   updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }



}
