package br.com.uarini.pogapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.uarini.pogapp.R;
import br.com.uarini.pogapp.view.ManagerPokemonData;

public class PokemonDataFragment extends Fragment {

    private ManagerPokemonData managerPokemonData  = new ManagerPokemonData();

    public PokemonDataFragment() {
    }

    public static PokemonDataFragment newInstance(Integer pkNumber) {
        PokemonDataFragment fragment = new PokemonDataFragment();
        Bundle args = new Bundle();
        args.putInt(ManagerPokemonData.ARG_PK_NUMBER, pkNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.managerPokemonData.onCreate(savedInstanceState, this.getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pokemon_data, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.managerPokemonData.setupView(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.managerPokemonData.storageRecord();
    }

}
