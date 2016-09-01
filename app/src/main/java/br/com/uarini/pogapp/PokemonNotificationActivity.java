package br.com.uarini.pogapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.fragment.PokemonDataBottomSheetDialogFragment;
import br.com.uarini.pogapp.service.PokemonService;
import br.com.uarini.pogapp.uitls.PokemonPreferences;
import br.com.uarini.pogapp.view.ManagerPokemonData;

public class PokemonNotificationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Integer numberLastPokemon = PokemonPreferences.getLastPokemon(this);

        final PokemonService service = new PokemonService();
        Pokemon pokemon = null;
        if ( numberLastPokemon < 0 ) {
            pokemon = service.getFirstPokemon();
        } else {
            pokemon = service.getPokemonByNumber(numberLastPokemon);
        }
        if ( pokemon == null ) {
            Toast.makeText(this, R.string.pokemon_not_found_from_notification, Toast.LENGTH_SHORT).show();
        } else {
            final Bundle mBundle = new Bundle();
            mBundle.putInt(ManagerPokemonData.ARG_PK_NUMBER, pokemon.getNumber());


            PokemonDataBottomSheetDialogFragment bottomSheetDialogFragment = new PokemonDataBottomSheetDialogFragment();
            bottomSheetDialogFragment.setArguments(mBundle);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), "--fragment");

        }
    }
}
