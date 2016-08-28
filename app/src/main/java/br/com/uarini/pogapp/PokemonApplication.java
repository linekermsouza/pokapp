package br.com.uarini.pogapp;

import android.app.Application;

import br.com.uarini.pogapp.db.DaoMaster;
import br.com.uarini.pogapp.db.DaoSession;

/**
 * Created by marcos on 27/08/16.
 */
public class PokemonApplication extends Application {
    private DaoMaster daoMaster;

    public static PokemonApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "pokemon-db", null);
        this.daoMaster = new DaoMaster(helper.getWritableDb());
        instance = this;
    }

    public DaoSession getDaoSession(){
        return this.daoMaster.newSession();
    }
}
