package com.gkt.browse.dictionary;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "SIGNIN";
    static public FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN =2 ;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private Button login;
    private Button createAc;
    private EditText email;
    private EditText pass;
    private FirebaseUser user;
    private Auth0 auth0;
    public  static  AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Log.i("mAuth",mAuth.toString());
        //  callBackTask();

        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        //final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        // final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        auth0 = new Auth0(this);
        auth0.setOIDCConformant(true);

        //linkCredentialToFirebase();

      //  login();

       //facebook auth
            final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            List<String> permissionNeeds = Arrays.asList("user_photos", "email",
                    "user_birthday", "public_profile");
            loginButton.setReadPermissions(permissionNeeds);
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    AccessToken accessToken1 = loginResult.getAccessToken();
                    handleFacebookAccessToken(accessToken1);
                    credential = FacebookAuthProvider.getCredential(accessToken1.getToken());
                    Log.i("Register", "registered " + accessToken1);

                }

                @Override
                public void onCancel() {
                    // App code
                    Log.i("cancelFb", "canclelled");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.i("fbError", "error");
                }
            });


            //google sign in
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            // Set the dimensions of the sign-in button.
            signInButton = findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    googleSignIn();
                }
            });

            //email-pass authentication
            login = (Button) findViewById(R.id.submit);
            createAc = (Button) findViewById(R.id.register);
            email = (EditText) findViewById(R.id.email);
            pass = (EditText) findViewById(R.id.password);


            createAc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emails, passw;

                    emails = email.getText().toString();
                    passw = pass.getText().toString();

                    if (emails != null && passw != null) {
                        emailPassCreateAccount(emails, passw);
                    }

                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emails, passw;

                    emails = email.getText().toString();
                    passw = pass.getText().toString();
                    if (emails != null && passw != null) {
                        emailPassSignIn(emails, passw);

                    }

                }
            });


    }
    @Override
    protected void onStart() {
        super.onStart();
       /* FirebaseUser currentUser = mAuth.getCurrentUser();
        //  updateUI(currentUser);
        if(currentUser!=null) {
            Log.i("facebookCurrUserStatus:", currentUser.toString());

        }
        else {
            Log.i("facebookCurrUserStatus:", "no current user");
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);//check for existing user
        if(account!=null)
        {

        }*/
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            callToHome();
                            Log.i("SignedInUser",user.toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void fbLogout()
    {
        FirebaseAuth.getInstance().signOut();

    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticateod UI.

            //  updateUI(account);
            callToHome();
            Log.i("SibnInAccount",account.toString());
           googleProfileInfo();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignInResult", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    public void googleProfileInfo()
    {
        GoogleSignInAccount  account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!= null) {
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
            credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            Log.i("credentialFromLogin",credential.toString());
        }
    }

    private void googleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Log.i("SignOut","successfull");

                    }
                });
    }

    private void googleRevokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                        Log.i("DeletedAccount","DeletedSuccessfully");

                    }
                });
    }


    public void emailPassCreateAccount(final String email,final String password)
    {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            credential = EmailAuthProvider.getCredential(email, password);
                            Log.i("credentialFromLogin",credential.toString());
                            Toast.makeText(getApplicationContext(),"created succesfully user:"+user.toString(),Toast.LENGTH_LONG).show();
                            callToHome();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void emailPassSignIn(final String email,final String password)
    {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "signed succesfully:"+user.toString(),
                                    Toast.LENGTH_SHORT).show();
                            credential = EmailAuthProvider.getCredential(email, password);
                            Log.i("credentialFromLogin",credential.toString());
                            callToHome();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                        }

                        // ...
                    }
                });

    }

    private void callToHome()
    {
        Intent  intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    // app/src/main/java/com/auth0/samples/MainActivity.java

    private void login() {

        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
                .start(LoginActivity.this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull final Dialog dialog) {
                        // Show error Dialog to user
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure( final AuthenticationException exception) {
                        // Show error to user
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onSuccess(@NonNull Credentials credentials) {
                        // Store credentials
                        // Navigate to your main activity
                        callToHome();
                    }
                });
    }

}

