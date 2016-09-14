package br.com.uarini.pogapp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.uarini.pogapp.PokemonApplication;
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

    private TextView tvQttPokemon,  tvTransfer, tvMaxQttOfEvolutions;

    private TextView tvPkName;

    public static final String ARG_PK_NUMBER = "pk_number";

    public static final String ARG_RETURN_SELECTED = "is_return";

    private View.OnClickListener onClickPokemonNameListener;

    public void onCreate(Bundle savedInstanceState, Bundle args){
        if ( args == null ) {
            return;
        }
        final Integer pkNumber = args.getInt(ManagerPokemonData.ARG_PK_NUMBER);

        this.loadPokemon(pkNumber);
    }

    private void loadPokemon(Integer pkNumber) {
        final DaoSession session = PokemonApplication.instance.getDaoSession();
        this.pokemon = session.getPokemonDao().queryBuilder().where(PokemonDao.Properties.Number.eq(pkNumber)).unique();
        this.pokemonData =session.getPokemonDataDao().queryBuilder().where(PokemonDataDao.Properties.PokemonNumber.eq(pkNumber)).unique();
    }

    public void setupView(View view) {
        this.setupView(view, false);
    }

    public void setupView(View view, boolean isFromDialog) {
        this.tvPkName = (TextView) view.findViewById(R.id.tvPokemon);
        this.tvPkName.setOnClickListener(this);
        this.setPokemonName();
        this.tvMaxQttOfEvolutions = (TextView) view.findViewById(R.id.tvResult01);
        this.tvTransfer = (TextView) view.findViewById(R.id.tvResultTransfer);
        this.tvQttPokemon = (TextView) view.findViewById(R.id.tvQttPokemon);

        this.npQtd = (MyNumberPicker) view.findViewById(R.id.npQtd);
        this.npQtdCandy = (MyNumberPicker) view.findViewById(R.id.npQtdCandy);

        this.npQtd.init();
        this.npQtdCandy.init();

        this.npQtd.setValueListener(this);
        this.npQtdCandy.setValueListener(this);

        this.bindValues();

        this.calculeBestEfficient();
        this.tvQttPokemon.setText( view.getContext().getString(R.string.qtt_pokemon, this.pokemon.getName()) );
    }

    private void setPokemonName() {
        this.tvPkName.setText(this.pokemon.getName());
    }

    private void bindValues(){
        this.npQtd.setValue(this.pokemonData.getQtd());
        this.npQtdCandy.setValue(this.pokemonData.getQtdCandy());
    }

    @Override
    public boolean onValue(MyNumberPicker picker, int value) {
        if (picker.getId() == this.npQtd.getId() ){//Quantity of pokemons
            this.pokemonData.setQtd(value);
        } else if (picker.getId() == this.npQtdCandy.getId() ){
            this.pokemonData.setQtdCandy(value);
        }
        this.calculeBestEfficient();

        return true;
    }

    private void calcule(){
        final Integer qttCandyAfterTransfer = this.pokemonData.getTransfer() + this.pokemonData.getQtdCandy();

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
        if ( view.getId() == this.tvPkName.getId() ) {
            this.onClickPokemonNameListener.onClick(view);
        }
    }


    public void onNewPokemonSelected(Bundle mBundle){
        final Integer pkNumber = mBundle.getInt(ManagerPokemonData.ARG_PK_NUMBER);
        if ( this.pokemon.getNumber() == pkNumber ){
            return;
        }
        this.loadPokemon(pkNumber);
        this.setPokemonName();
        this.bindValues();
        this.calculeBestEfficient();
        this.tvQttPokemon.setText("Qtd. de " + this.pokemon.getName() + ":");
    }

    public void setOnClickPokemonName(View.OnClickListener onClickPokemonName) {
        this.onClickPokemonNameListener = onClickPokemonName;
    }

    private void calculeBestEfficient() {
        int qttTransfer = 0;
        if ( this.pokemonData.getQtdCandyEvolve() == 0 ) {
            return;
        }
        int qttEvolve = this.pokemonData.getQtdCandy() / this.pokemonData.getQtdCandyEvolve();
        while ( (qttTransfer+qttEvolve) < this.pokemonData.getQtd()){
            qttTransfer++;
            if (qttTransfer+qttEvolve >= this.pokemonData.getQtd()){
                break;
            }
            qttEvolve = (this.pokemonData.getQtdCandy()+qttTransfer) / this.pokemonData.getQtdCandyEvolve();
        }

        this.pokemonData.setTransfer(qttTransfer);
        this.tvTransfer.setText(String.valueOf(this.pokemonData.getTransfer()));
        calcule();

    }
}
