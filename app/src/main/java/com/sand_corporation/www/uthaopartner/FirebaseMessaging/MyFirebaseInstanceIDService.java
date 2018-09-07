package com.sand_corporation.www.uthaopartner.FirebaseMessaging;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sand_corporation.www.uthaopartner.FirebaseMessaging.ModelClass.Token;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;

/**
 * Created by HP on 1/30/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateRefreshedTokenToServer(refreshedToken);
    }

    private void updateRefreshedTokenToServer(String refreshedToken) {
        //FCM_LATEST_TOKEN
        Common.refreshedFCMToken = refreshedToken;

        Token token = new Token(refreshedToken);
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            DatabaseReference mDriverRefreshedToken = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(Common.driverUID)
                    .child("FCM_LATEST_TOKEN");
            mDriverRefreshedToken.setValue(token);
        }

    }

}
