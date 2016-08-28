package br.com.uarini.pogapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.uarini.pogapp.fragment.ListPokemonFragment;
import br.com.uarini.pogapp.fragment.PokemonDataFragment;

public class PokemonDataActivity extends AppCompatActivity {

    public static final String ARG_PK_NUMBER = "pk_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_data);

        final Fragment fragment = PokemonDataFragment.newInstance(this.getIntent().getIntExtra(ARG_PK_NUMBER, -1));
        this.getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}
