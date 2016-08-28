package br.com.uarini.pogapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import br.com.uarini.pogapp.PokemonApplication;
import br.com.uarini.pogapp.cloud.PokemonSyncCloud;

public class LoadPokemonService extends IntentService {

    private final PokemonSyncCloud load = new PokemonSyncCloud();
    public LoadPokemonService() {
        super("LoadPokemonService");
    }


    public static void startSyncContent(Context context) {
        Intent intent = new Intent(context, LoadPokemonService.class);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && PokemonApplication.instance.getDaoSession().getPokemonDao().queryBuilder().count() > 0 ){
            this.load.sync();
        }
    }

}
