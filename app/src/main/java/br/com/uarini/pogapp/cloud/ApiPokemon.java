package br.com.uarini.pogapp.cloud;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by marcos on 27/08/16.
 */
public interface ApiPokemon {
    @GET("pokemons.json?alt=media&token=b5e878b8-8fb0-4d37-9e39-f286e6c10d59")
    Call<List<CloudPokemon>> loadContent();
}
