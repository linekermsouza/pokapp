package br.com.uarini.pogapp.uitls;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marcos on 28/08/16.
 */
public class PokemonPreferences {

    private static final String KEY_LAST_POKEMON = "last_pokemon";

    public static void putLastPokemon(Context context, Integer number ){
        SharedPreferences sharedPref = PokemonPreferences.getSharedPreference(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_LAST_POKEMON, number);
        editor.commit();
    }

    public static int getLastPokemon(Context context) {
        SharedPreferences sharedPref = PokemonPreferences.getSharedPreference(context);
        return sharedPref.getInt(KEY_LAST_POKEMON, -1);
    }
    private static SharedPreferences getSharedPreference(Context context){
        return  context.getSharedPreferences("pokemon.xml", Context.MODE_PRIVATE);
    }
}
