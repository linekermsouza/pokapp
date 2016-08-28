package br.com.uarini.pogapp.service;

import java.util.List;

import br.com.uarini.pogapp.PokemonApplication;
import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.db.PokemonDao;

/**
 * Created by marcos on 28/08/16.
 */
public class PokemonService {

    public Pokemon getPokemonByNumber(Integer number){
        return PokemonApplication.instance.getDaoSession().getPokemonDao().queryBuilder().where(PokemonDao.Properties.Number.eq(number)).unique();
    }

    public Pokemon getFirstPokemon() {
        final List<Pokemon> listPokemon =  PokemonApplication.instance.getDaoSession().getPokemonDao().queryBuilder().list();
        if ( listPokemon.isEmpty() ) {
            return null;
        }
        return listPokemon.get(0);
    }
}
