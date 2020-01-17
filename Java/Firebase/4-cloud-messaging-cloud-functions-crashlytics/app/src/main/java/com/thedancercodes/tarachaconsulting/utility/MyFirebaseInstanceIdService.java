package com.thedancercodes.tarachaconsulting.utility;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.thedancercodes.tarachaconsulting.R;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseInstanceId";

    @Override
    public void onNewToken(String s) {
//        super.onNewToken(s);
//        Log.d(TAG, "onNewToken: ", s);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "onNewToken: Refreshed token: " + refreshedToken);

        // Send the token to database
        sendRegistrationToServer(refreshedToken);



        /*
         * TODO: Replace FirebaseInstanceIdService with FirebaseMessagingService & replace getToken
         * FirebaseInstanceIdService is deprecated
         * https://medium.com/android-school/firebaseinstanceidservice-is-deprecated-50651f17a148
         */
//                FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
//                            @Override
//                            public void onSuccess(InstanceIdResult instanceIdResult) {
//                                String newToken = instanceIdResult.getToken();
//                                Log.e(TAG, "onSuccess: ", newToken);
//                            }
//                        }
//                );
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);

        // Get DB reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // Reference user's node to insert token
        reference.child(getString(R.string.dbnode_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.field_messaging_token))
                .setValue(token);
    }
}
