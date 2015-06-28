package momenify.proconnect.navigationviewpagerliveo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by shahbazkhan on 3/23/15.
 */
public class OrionCentricApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "ssIqti6an7anOenvJvIXBDPUurX70V6rXyKxONcx", "LwzXfVOaoe26QKvUCNia8jisgul1nS3c2Lk2whMX");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}
