package br.com.uarini.pogapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.uarini.pogapp.PokemonApplication;
import br.com.uarini.pogapp.PokemonDataActivity;
import br.com.uarini.pogapp.R;
import br.com.uarini.pogapp.db.DaoSession;
import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.db.PokemonDao;
import br.com.uarini.pogapp.db.PokemonData;
import br.com.uarini.pogapp.db.PokemonDataDao;

/**
 * Created by marcos on 28/08/16.
 */
public class ManagerPokemonData implements MyNumberPicker.OnValueChangedListener {
    private Pokemon pokemon;
    private PokemonData pokemonData;
    private MyNumberPicker npQtd;
    private MyNumberPicker npQtdCandy;
    private MyNumberPicker npQtdCandyEvolve;
    private MyNumberPicker npQtdTransfer;

    private TextView tvResult01, tvResult02;

    public void onCreate(Bundle savedInstanceState, Bundle args){
        if ( args == null ) {
            return;
        }
        final Integer pkNumber = args.getInt(PokemonDataActivity.ARG_PK_NUMBER);
        final DaoSession session = PokemonApplication.instance.getDaoSession();
        this.pokemon = session.getPokemonDao().queryBuilder().where(PokemonDao.Properties.Number.eq(pkNumber)).unique();
        this.pokemonData =session.getPokemonDataDao().queryBuilder().where(PokemonDataDao.Properties.PokemonNumber.eq(pkNumber)).unique();
    }

    public void setupView(View view) {
        this.setupView(view, false);
    }

    public void setupView(View view, boolean isFromDialog) {

        if ( isFromDialog ) {
            ((TextView) view.findViewById(R.id.tvPokemon)).setText(this.pokemon.getName());
        } else {
            view.findViewById(R.id.tvPokemon).setVisibility(View.GONE);
        }
        this.tvResult01 = (TextView) view.findViewById(R.id.tvResult01);
        this.tvResult02 = (TextView) view.findViewById(R.id.tvResult02);

        this.npQtd = (MyNumberPicker) view.findViewById(R.id.npQtd);
        this.npQtdCandy = (MyNumberPicker) view.findViewById(R.id.npQtdCandy);
        this.npQtdCandyEvolve = (MyNumberPicker) view.findViewById(R.id.npQtdCandyEnvolve);
        this.npQtdTransfer = (MyNumberPicker) view.findViewById(R.id.npQtdTransfer);

        this.npQtd.init();
        this.npQtdCandy.init();
        this.npQtdCandyEvolve.init();
        this.npQtdTransfer.init();

        this.npQtd.setValueListener(this);
        this.npQtdTransfer.setValueListener(this);
        this.npQtdCandyEvolve.setValueListener(this);
        this.npQtdCandy.setValueListener(this);

        this.bindValues();

        this.calcule();
    }

    private void bindValues(){
        this.npQtdCandy.setValue(this.pokemonData.getQtdCandy());
        this.npQtd.setValue(this.pokemonData.getQtd());
        this.npQtdCandyEvolve.setValue(this.pokemonData.getQtdCandyEvolve());
        this.npQtdTransfer.setValue(this.pokemonData.getTransfer());
    }

    @Override
    public void onValue(MyNumberPicker picker, int value) {
        if (picker.getId() == this.npQtd.getId() ){
            this.pokemonData.setQtd(value);
        } else if (picker.getId() == this.npQtdCandy.getId() ){
            this.pokemonData.setQtdCandy(value);
        } else if (picker.getId() == this.npQtdCandyEvolve.getId() ){
            this.pokemonData.setQtdCandyEvolve(value);
        } else if (picker.getId() == this.npQtdTransfer.getId() ){
            this.pokemonData.setTransfer(value);
        }
        this.calcule();
    }

    private void calcule(){
        final Integer result01 = this.pokemonData.getTransfer() + this.pokemonData.getQtdCandy();
        this.tvResult01.setText(result01.toString());

        if ( this.pokemonData.getQtdCandyEvolve() == 0 ) {
            this.tvResult02.setText("0");
        } else {
            Integer result02 = result01 / this.pokemonData.getQtdCandyEvolve();
            if (result02 < 0) {
                result02 = 0;
            }
            this.tvResult02.setText(result02.toString());
        }
    }

    public void storageRecord(){
        try {
            final DaoSession session = PokemonApplication.instance.getDaoSession();
            session.update(this.pokemonData);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
