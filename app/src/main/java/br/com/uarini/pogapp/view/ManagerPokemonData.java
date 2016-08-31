package br.com.uarini.pogapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.uarini.pogapp.MainActivity;
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
    private MyNumberPicker npQtdCandyEvolve;
    private MyNumberPicker npQtdTransfer;

    private TextView tvResult01, tvResult02, tvPkName;

    public Activity activity;

    public static final String ARG_PK_NUMBER = "pk_number";

    public static final String ARG_RETURN_SELECTED = "is_return";

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
        if ( isFromDialog ) {
            this.tvPkName.setText(this.pokemon.getName());
        } else {
            this.tvPkName.setVisibility(View.GONE);
        }
        this.tvResult01 = (TextView) view.findViewById(R.id.tvResult01);
        this.tvResult02 = (TextView) view.findViewById(R.id.tvResult02);

        this.npQtd = (MyNumberPicker) view.findViewById(R.id.npQtd);
        this.npQtdCandyEvolve = (MyNumberPicker) view.findViewById(R.id.npQtdCandyEnvolve);
        this.npQtdTransfer = (MyNumberPicker) view.findViewById(R.id.npQtdTransfer);

        this.npQtd.init();
        this.npQtdCandyEvolve.init();
        this.npQtdTransfer.init();

        this.npQtd.setValueListener(this);
        this.npQtdTransfer.setValueListener(this);
        this.npQtdCandyEvolve.setValueListener(this);

        this.bindValues();

        this.calcule();
    }

    private void bindValues(){
        this.npQtd.setValue(this.pokemonData.getQtd());
        this.npQtdCandyEvolve.setValue(this.pokemonData.getQtdCandyEvolve());
        this.npQtdTransfer.setValue(this.pokemonData.getTransfer());
    }

    @Override
    public void onValue(MyNumberPicker picker, int value) {
        if (picker.getId() == this.npQtd.getId() ){
            this.pokemonData.setQtd(value);
        } else if (picker.getId() == this.npQtdCandyEvolve.getId() ){
            this.pokemonData.setQtdCandyEvolve(value);
        } else if (picker.getId() == this.npQtdTransfer.getId() ){
            this.pokemonData.setTransfer(value);
        }
        this.calcule();
    }

    @Override
    public boolean onValueValid(MyNumberPicker picker, int value) {
        if (picker.getId() == this.npQtdTransfer.getId() && this.calculeQtdCandyAposTransferir(value) > this.pokemonData.getQtd() ){
            return false;
        }
        return true;
    }

    private void calcule(){
        int transfer = this.calculeQtdCandyAposTransferir(this.pokemonData.getTransfer());
        int evolve = this.calculeDaPraEvoluir(transfer, this.pokemonData.getQtdCandyEvolve());
        this.tvResult01.setText(String.valueOf(transfer));
        this.tvResult02.setText(String.valueOf(evolve));
    }

    private int calculeDaPraEvoluir(int transfer, int qtdCandyEvolve){
        if ( qtdCandyEvolve == 0 ) {
            return 0;
        }
        Integer result02 = transfer / qtdCandyEvolve;
        if (result02 < 0) {
            result02 = 0;
        }
        return result02;
    }

    private Integer calculeQtdCandyAposTransferir(int transfer){
        return transfer + this.pokemonData.getQtdCandy();
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
            final Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra(ARG_RETURN_SELECTED, true);
            this.activity.startActivityForResult(intent, 1);
        }
    }

    private void calculeSugerir(){
        int qtdTransferir = 0;
        int qtdEvoluir = (int)(this.pokemonData.getQtdCandy() / this.pokemonData.getQtdCandyEvolve());
        int qtdTranferir = 0;
        while ( (qtdTransferir+qtdEvoluir) < this.pokemonData.getQtd()){
            qtdTranferir++;
            if ((qtdTransferir + qtdEvoluir) >= this.pokemonData.getQtd()){
                break;
            }
            qtdEvoluir = (int) ( (this.pokemonData.getQtdCandy() + qtdTranferir) / this.pokemonData.getQtdCandyEvolve());
        }

        this.tvResult01.setText(String.valueOf(qtdTransferir));
        this.tvResult02.setText(String.valueOf(qtdEvoluir));

    }

    public void onNewPokemonSelected(Bundle mBundle){
        final Integer pkNumber = mBundle.getInt(ManagerPokemonData.ARG_PK_NUMBER);
        if ( this.pokemon.getNumber() == pkNumber ){
            return;
        }
        this.loadPokemon(pkNumber);
        this.bindValues();
        this.calcule();
    }
}
