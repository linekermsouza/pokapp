package br.com.uarini.pogapp.cloud;

import android.util.Log;

import org.greenrobot.greendao.DaoException;

import java.io.IOException;
import java.util.List;

import br.com.uarini.pogapp.PokemonApplication;
import br.com.uarini.pogapp.db.DaoSession;
import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.db.PokemonDao;
import br.com.uarini.pogapp.db.PokemonData;
import br.com.uarini.pogapp.db.PokemonDataDao;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by marcos on 27/08/16.
 */
public class PokemonSyncCloud {
    public void sync() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firebasestorage.googleapis.com/v0/b/pgapp-5bc17.appspot.com/o/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ApiPokemon service = retrofit.create(ApiPokemon.class);


        final Call<List<CloudPokemon>> call = service.loadContent();
        try {
            Response<List<CloudPokemon>> items = call.execute();
            Log.d("SYNC", items.message());
            Log.d("SYNC", "CODE " +items.code());


            if (items.isSuccessful()){
             //   Log.d("SYNC", items.raw().body().string());
                final List<CloudPokemon> values = items.body();
                CloudPokemon pokemonCloud = null;


                final DaoSession session = PokemonApplication.instance.getDaoSession();
                try {
                    session.getDatabase().beginTransaction();

                    final PokemonDao dao = session.getPokemonDao();
                    final PokemonDataDao dataDao = session.getPokemonDataDao();

                    Pokemon pokemon = null;
                    PokemonData data = null;

                    Log.d("SYNC", "length " + values.size());

                    for (int i = 0; i < values.size(); i++) {
                        pokemonCloud = values.get(i);
                        int number = Integer.valueOf(pokemonCloud.number);
                        try {
                            pokemon = dao.queryBuilder().where(PokemonDao.Properties.Number.eq(number)).uniqueOrThrow();
                            pokemon.setName(pokemonCloud.name);
                            dao.update(pokemon);
                            data = dataDao.queryBuilder().where(PokemonDataDao.Properties.PokemonNumber.eq(number)).uniqueOrThrow();
                            data.setQtdCandyEvolve(pokemonCloud.candy);
                            dataDao.update(data);
                        } catch (DaoException exception) {
                            if ( pokemonCloud.status == 0 ) {
                                continue;
                            }
                            pokemon = new Pokemon();
                            pokemon.setName(pokemonCloud.name);
                            pokemon.setNumber(number);
                            dao.save(pokemon);
                            data = new PokemonData();
                            data.setQtd(0);
                            data.setTransfer(0);
                            data.setQtdCandy(0);
                            data.setQtdCandyEvolve(pokemonCloud.candy);

                            data.setPokemonNumber(number);
                            dataDao.save(data);
                        }
                    }
                    session.getDatabase().setTransactionSuccessful();
                } finally {
                    session.getDatabase().endTransaction();
                }
            } else {
                Log.d("SYNC", items.errorBody().string());
                throw  new IOException("empty");
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
