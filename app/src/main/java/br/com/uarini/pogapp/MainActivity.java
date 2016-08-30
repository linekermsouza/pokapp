package br.com.uarini.pogapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.fragment.ListPokemonFragment;
import br.com.uarini.pogapp.fragment.PokemonDataBottomSheetDialogFragment;
import br.com.uarini.pogapp.notification.MyNotificationManager;
import br.com.uarini.pogapp.service.LoadPokemonService;
import br.com.uarini.pogapp.uitls.PokemonPreferences;

public class MainActivity extends AppCompatActivity implements ListPokemonFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadPokemonService.startSyncContent(this);

        setContentView(R.layout.activity_main);

        MyNotificationManager.pin(this);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.content, new ListPokemonFragment()).commit();

    }


    @Override
    public void onListFragmentInteraction(Pokemon item) {
        if (this.getIntent().hasExtra("return")){
            this.finish();
        }
        PokemonPreferences.putLastPokemon(this, item.getNumber());
        final Bundle mBundle = new Bundle();
        mBundle.putInt(PokemonDataActivity.ARG_PK_NUMBER, item.getNumber());


        PokemonDataBottomSheetDialogFragment bottomSheetDialogFragment = new PokemonDataBottomSheetDialogFragment();
        bottomSheetDialogFragment.setArguments(mBundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), "--fragment");
    }
}
