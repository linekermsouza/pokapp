package br.com.uarini.pogapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import br.com.uarini.pogapp.MainActivity;
import br.com.uarini.pogapp.PokemonNotificationActivity;
import br.com.uarini.pogapp.R;
import br.com.uarini.pogapp.view.ManagerPokemonData;

/**
 * Created by marcos on 28/08/16.
 */
public class PokemonDataBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private ManagerPokemonData managerPokemonData  = new ManagerPokemonData();

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_pokemon_data, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        this.managerPokemonData.setupView(contentView, getActivity() instanceof  PokemonNotificationActivity);
        this.managerPokemonData.setOnClickPokemonName(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.managerPokemonData.onCreate(savedInstanceState, this.getArguments());
    }

    @Override
    public void onPause() {
        super.onPause();
        this.managerPokemonData.storageRecord();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if ( getActivity() instanceof PokemonNotificationActivity ) {
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK ){
            this.managerPokemonData.onNewPokemonSelected(data.getExtras());
        }

    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra(ManagerPokemonData.ARG_RETURN_SELECTED, true);
        this.getActivity().startActivityFromFragment(this, intent, 1);
    }
}