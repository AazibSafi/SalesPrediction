package sajid.bussinesssale.Database;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by hp on 4/2/2017.
 */

public class Realm_ORM extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("AndroidDB.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        io.realm.Realm.setDefaultConfiguration(realmConfiguration);
    }
}
