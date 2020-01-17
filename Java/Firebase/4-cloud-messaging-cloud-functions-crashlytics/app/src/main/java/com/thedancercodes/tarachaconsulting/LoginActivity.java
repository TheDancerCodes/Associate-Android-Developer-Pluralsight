package com.thedancercodes.tarachaconsulting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Constants
    private static final int ERROR_DIALOG_REQUEST = 9001;

    // Object responsible for retrieving the Authentication State.
    private FirebaseAuth.AuthStateListener mAuthListener;

    // widgets
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // App will be actively listening for the authentication state
        setUpFirebaseAuth();

        if (servicesOK()) {
            init();
        }
        hideSoftKeyboard();
    }

    private void init() {

        Button signIn = (Button) findViewById(R.id.email_sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the fields are filled out
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())){
                    Log.d(TAG, "onClick: attempting to authenticate.");

                    // Show Dialog and initiate the authentication request
                    showDialog();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Use OnCompleteListener to listen for task completion.
                                    hideDialog();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,
                                    "Authentication Failed", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }
                    });


                }else{
                    Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView register = (TextView) findViewById(R.id.link_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        TextView resetPassword = (TextView) findViewById(R.id.forgot_password);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView resendEmailVerification = (TextView) findViewById(R.id.resend_verification_email);
        resendEmailVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendVerificationDialog dialog = new ResendVerificationDialog();
                dialog.show(getSupportFragmentManager(), "dialog_resend_email_verification");
            }
        });
    }

    public boolean servicesOK() {
        Log.d(TAG, "servicesOK: Checking Google Services.");

        int isAvailable = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(LoginActivity.this);

        if (isAvailable == ConnectionResult.SUCCESS) {

            // Everything is ok and the user can make mapping requests
            Log.d(TAG, "servicesOK: Play Services is OK");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)) {

            // An error occurred, but it's resolvable
            Log.d(TAG, "servicesOK: an error occurred, but it's resolvable.");

            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(LoginActivity.this, isAvailable, ERROR_DIALOG_REQUEST);

            dialog.show();
        }
        else {
            Toast.makeText(this, "Can't connect to mapping services", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        ----------------------------- Firebase setup ---------------------------------
     */

    // Instantiate an object responsible for retrieving the Authentication State
    private void setUpFirebaseAuth() {

        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // New FirebaseUser object
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // If user is not null, we have an authenticated user.
                if (user != null) {

                    // Check that a user email is verified.
                    if (user.isEmailVerified()) {
                        Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                        Toast.makeText(LoginActivity.this,
                                "Authenticated with: " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();

                        // Use an Intent & navigate to SignedInActivity
                        // if both the checks are confirmed.
                        Intent intent = new Intent(LoginActivity.this, SignedInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        // Prevents user from navigating back to Login screen after authentication.
                        finish();


                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Email is not Verified.\nCheck your Email Inbox for a Verification Link.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Add AuthStateListener
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Check the AuthStateListener is not null, then remove it.
        // If it is null and you try to remove it, app will crash.
        if (mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
