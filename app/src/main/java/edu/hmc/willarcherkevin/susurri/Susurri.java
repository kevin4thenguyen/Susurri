package edu.hmc.willarcherkevin.susurri;

import android.app.Application;
import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by archerwheeler on 3/29/15.
 */
public class Susurri extends Application {

    //Store the device unique user ID
    public static String androidId;

    //Create a global ParseUser object
    public ParseUser theUser = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        setUpUsers();
        // Initialize the Parse SDK.
        Parse.initialize(this, "9nWnCUTdcZrrXtlGQKOjgPJWayPRKyMSQzU2bXhX", "dCjilcjkIqYAlyx55CIwFqyVjzl1GvKAuML64sXo");

        // allows read and write access to all users
        if (ParseUser.getCurrentUser() != null) {
            ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
            postACL.setPublicReadAccess(true);
            postACL.setPublicWriteAccess(true);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        logoutUsers();
    }


    // setUpUsers first checks to see if a ParseUser exists and if so it will log the user in
    // otherwise, it creates a new ParseUser with the username and password being the androidID
    private void setUpUsers() {
        ParseUser.logInInBackground(androidId, androidId, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    theUser = user;

                    // allows read and write access to all users
                    ParseACL postACL = new ParseACL(theUser);
                    postACL.setPublicReadAccess(true);
                    postACL.setPublicWriteAccess(true);
                } else {
                    theUser = new ParseUser();
                    theUser.setUsername(androidId);
                    theUser.setPassword(androidId);

                    // other fields can be set just like with ParseObject
                    theUser.put("avatar", "bear");
                    // screen name counter: you get three screen name changes
                    theUser.put("SNC", 3);
                    theUser.put("screenName", "Default");

                    theUser.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                // allows read and write access to all users
                                ParseACL postACL = new ParseACL(theUser);
                                postACL.setPublicReadAccess(true);
                                postACL.setPublicWriteAccess(true);
                            } else {
                                Log.d("Error", "Failure to successfully sign up user");
                            }
                        }
                    });
                }
            }
        });
    }

    // never used but may be a nice function to have in certain cases
    private void resumeUsers() {
        theUser = ParseUser.getCurrentUser();
        if (theUser != null) {
            // Cool Beans the user is restored
        } else {
            setUpUsers();
        }
    }

    private void logoutUsers() {
        ParseUser.logOut();
        theUser = ParseUser.getCurrentUser(); // this will now be null
    }

}
