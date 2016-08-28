package br.com.uarini.pogapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.uarini.pogapp.cloud.ApiPokemon;
import br.com.uarini.pogapp.cloud.CloudPokemon;
import br.com.uarini.pogapp.db.DaoMaster;
import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.fragment.ListPokemonFragment;
import br.com.uarini.pogapp.service.LoadPokemonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ListPokemonFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadPokemonService.startSyncContent(this);

        setContentView(R.layout.activity_main);

        this.getSupportFragmentManager().beginTransaction().replace(R.id.content, new ListPokemonFragment()).commit();

    }


    @Override
    public void onListFragmentInteraction(Pokemon item) {
        final Intent intent = new Intent(this, PokemonDataActivity.class);
        intent.putExtra(PokemonDataActivity.ARG_PK_NUMBER, item.getNumber());
        this.startActivity(intent);
    }
}
