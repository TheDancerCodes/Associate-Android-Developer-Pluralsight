package com.thedancercodes.tarachaconsulting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.thedancercodes.tarachaconsulting.models.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register Activity";

    private static final String DOMAIN_NAME = "gmail.com";

    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private Button mRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mRegister = (Button) findViewById(R.id.btn_register);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if(!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())){

                    //check if user has a company email address. (Valid Domain)
                    if(isValidDomain(mEmail.getText().toString())){

                        //check if passwords match
                        if(doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())){

                            // Register New User
                            registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());

                        }else{
                            Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Please Register with Company Email", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hideSoftKeyboard();
    }

    /**
     * Method that registers a new email
     */
    private void registerNewEmail(final String email, String password) {
        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "onComplete: onComplete " + task.isSuccessful());

                                if (task.isSuccessful()) {

                                    // New user's userID
                                    Log.d(TAG, "onComplete: AuthState: "
                                            + FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    // Send email verification.
                                    sendVerificationEmail();

                                    // Create a new instance of the User class & set properties.
                                    User user = new User();
                                    user.setName(email.substring(0, email.indexOf("@")));
                                    user.setPhone("1");
                                    user.setProfile_image("");
                                    user.setSecurity_level("1");
                                    user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    // Insert Data into the Firebase DB
                                    FirebaseDatabase.getInstance().getReference()
                                            .child(getString(R.string.dbnode_users))
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            // Sign Out user & redirect to login screen
                                            FirebaseAuth.getInstance().signOut();
                                            redirectLoginScreen();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            // Sign Out user & redirect to login screen
                                            FirebaseAuth.getInstance().signOut();
                                            redirectLoginScreen();

                                            Toast.makeText(RegisterActivity.this,
                                                    "Something went wrong", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                }
                                if (!task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,
                                            "Unable to Register", Toast.LENGTH_SHORT).show();
                                }
                                hideDialog();
                            }
                        }
                );
    }

    /**
     * Sends a verification email once a new user is registered.
     */
    private void sendVerificationEmail() {

        // User object
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Check to ensure User object is not null
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,
                                        "Sent Verification Email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "Couldn't send Verification Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    /**
     * Returns True if the user's email contains '@tabian.ca'
     * @param email
     * @return
     */
    private boolean isValidDomain(String email){
        Log.d(TAG, "isValidDomain: verifying email has correct domain: " + email);
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        Log.d(TAG, "isValidDomain: users domain: " + domain);
        return domain.equals(DOMAIN_NAME);
    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
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

}
