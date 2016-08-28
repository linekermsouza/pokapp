package br.com.uarini.pogapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.uarini.pogapp.PokemonApplication;
import br.com.uarini.pogapp.R;
import br.com.uarini.pogapp.cloud.PokemonSyncCloud;
import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.db.PokemonDao;

public class ListPokemonFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pokemon>>  {

    private OnListFragmentInteractionListener mListener;
    private PokemonRecyclerViewAdapter adapter;

    public ListPokemonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pokemon_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            this.adapter = new PokemonRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(this.adapter);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<List<Pokemon>> onCreateLoader(int id, Bundle args) {
        return new PokemonLoader(this.getActivity(), null);
    }

    @Override
    public void onLoadFinished(Loader<List<Pokemon>> loader, List<Pokemon> data) {
        this.adapter.replaceItems(data);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Pokemon>> loader) {

    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Pokemon item);
    }

    public static class PokemonLoader extends AsyncTaskLoader<List<Pokemon>> {

        String query;

        final PokemonDao dao;

        final PokemonSyncCloud load = new PokemonSyncCloud();

        public PokemonLoader(Context context, String query) {
            super(context);
            this.query = query;
            this.dao = PokemonApplication.instance.getDaoSession().getPokemonDao();
        }

        @Override
        public List<Pokemon> loadInBackground() {

            List<Pokemon> listPokemon = dao.queryBuilder().list();
            if ( listPokemon.isEmpty() ) {
                load.sync();
                listPokemon = dao.queryBuilder().list();
            }
            return listPokemon;
        }
    }
}
