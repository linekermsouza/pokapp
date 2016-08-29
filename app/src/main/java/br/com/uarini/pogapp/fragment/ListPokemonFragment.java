package br.com.uarini.pogapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import br.com.uarini.pogapp.PokemonApplication;
import br.com.uarini.pogapp.R;
import br.com.uarini.pogapp.cloud.PokemonSyncCloud;
import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.db.PokemonDao;

public class ListPokemonFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Pokemon>>, SearchView.OnQueryTextListener  {

    private OnListFragmentInteractionListener mListener;
    private PokemonRecyclerViewAdapter adapter;

    private SearchView search;

    private PokemonLoader loader;

    public ListPokemonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pokemon_item_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.adapter = new PokemonRecyclerViewAdapter(mListener);
        recyclerView.setAdapter(this.adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.search = (SearchView) view.findViewById(R.id.searchView);
        this.search.onActionViewExpanded();
        this.search.setOnQueryTextListener(this);
        this.search.setQueryHint(getActivity().getBaseContext().getString(R.string.type_pokemon_name));
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
        this.loader = new PokemonLoader(this.getActivity(), args != null? args.getString("query") : null);
        return this.loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Pokemon>> loader, List<Pokemon> data) {
        this.adapter.replaceItems(data);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Pokemon>> loader) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (this.loader != null) {
            this.loader.setQuery(newText);
        }
        Bundle bundle = new Bundle();
        bundle.putString("query", newText);
        getLoaderManager().initLoader(0, bundle, ListPokemonFragment.this).forceLoad();
        return true;
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

            List<Pokemon> listPokemon = this.doSearch();
            if ( listPokemon.isEmpty() && TextUtils.isEmpty(query)) {
                load.sync();
                listPokemon = this.doSearch();
            }
            return listPokemon;
        }

        private List<Pokemon> doSearch(){

            final QueryBuilder builder = dao.queryBuilder();
            if ( !TextUtils.isEmpty(query) ) {
                builder.where(PokemonDao.Properties.Name.like("%" + query + "%"));
            }

            return builder.list();
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }
}
