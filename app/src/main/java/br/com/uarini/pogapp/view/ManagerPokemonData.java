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
public class ManagerPokemonData implements MyNumberPicker.OnValueChangedListener, View.OnClickListener {
    private Pokemon pokemon;
    private PokemonData pokemonData;
    private MyNumberPicker npQtd;
    private MyNumberPicker npQtdCandy;
    private MyNumberPicker npQtdCandyEvolve;
    private MyNumberPicker npQtdTransfer;

    private TextView tvQttCandyAfterTransfer, tvMaxQttOfEvolutions;

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
        this.tvQttCandyAfterTransfer = (TextView) view.findViewById(R.id.tvResult01);
        this.tvMaxQttOfEvolutions = (TextView) view.findViewById(R.id.tvResult02);

        this.npQtd = (MyNumberPicker) view.findViewById(R.id.npQtd);
        this.npQtdCandy = (MyNumberPicker) view.findViewById(R.id.npQtdCandy);
        this.npQtdCandyEvolve = (MyNumberPicker) view.findViewById(R.id.npQtdCandyEnvolve);
        this.npQtdTransfer = (MyNumberPicker) view.findViewById(R.id.npQtdTransfer);
        View btnBestEfficient = view.findViewById(R.id.btn_best_efficient);
        btnBestEfficient.setOnClickListener(this);

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
    public boolean onValue(MyNumberPicker picker, int value) {
        if (picker.getId() == this.npQtd.getId() ){//Quantity of pokemons
            this.pokemonData.setQtd(value);
        } else if (picker.getId() == this.npQtdCandy.getId() ){//Quantity of candies
            this.pokemonData.setQtdCandy(value);
        } else if (picker.getId() == this.npQtdCandyEvolve.getId() ){//TODO: retrieve from json
            this.pokemonData.setQtdCandyEvolve(value);
        } else if (picker.getId() == this.npQtdTransfer.getId() ){//Quantity to transfer
            if (value <= this.pokemonData.getQtd() ) {
                this.pokemonData.setTransfer(value);
            }else{
                return  false;
            }
        }
        this.calcule();

        return true;
    }

    private void calcule(){
        final Integer qttCandyAfterTransfer = this.pokemonData.getTransfer() + this.pokemonData.getQtdCandy();
        this.tvQttCandyAfterTransfer.setText(qttCandyAfterTransfer.toString());

        if ( this.pokemonData.getQtdCandyEvolve() == 0 ) {
            this.tvMaxQttOfEvolutions.setText("0");
        } else {
            Integer maxQttOfEvolutions = qttCandyAfterTransfer / this.pokemonData.getQtdCandyEvolve();
            if (maxQttOfEvolutions < 0) {
                maxQttOfEvolutions = 0;
            }
            if (maxQttOfEvolutions > this.pokemonData.getQtd() - this.pokemonData.getTransfer()){
                maxQttOfEvolutions = this.pokemonData.getQtd() - this.pokemonData.getTransfer();
            }

            this.tvMaxQttOfEvolutions.setText(maxQttOfEvolutions.toString());
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_best_efficient){
            calculeBestEfficient();
        }
    }

    private void calculeBestEfficient() {
        int qttTransfer = 0;
        int qttEvolve = this.pokemonData.getQtdCandy() / this.pokemonData.getQtdCandyEvolve();
        while ( (qttTransfer+qttEvolve) < this.pokemonData.getQtd()){
            qttTransfer++;
            if (qttTransfer+qttEvolve >= this.pokemonData.getQtd()){
                break;
            }
            qttEvolve = (this.pokemonData.getQtdCandy()+qttTransfer) / this.pokemonData.getQtdCandyEvolve();
        }

        this.pokemonData.setTransfer(qttTransfer);
        this.npQtdTransfer.setValue(this.pokemonData.getTransfer());
        calcule();

    }
}
