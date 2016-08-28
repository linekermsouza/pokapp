package br.com.uarini.pogapp.gen;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

import java.io.File;

public class GenModel {
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {

        Schema schema = new Schema(1, "br.com.uarini.pogapp.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + File.separator + "app" + File.separator + "src" + File.separator + "main" +File.separator + "java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addPokemon(schema);
        addRepo(schema);
    }

    private static void addPokemon(final Schema schema) {
        Entity pokemon = schema.addEntity("Pokemon");
        pokemon.addIdProperty().primaryKey().autoincrement();
        pokemon.addStringProperty("name");
        pokemon.addIntProperty("number");
        pokemon.addIntProperty("image");
    }

    private static void addRepo(final Schema schema) {
        Entity data = schema.addEntity("PokemonData");
        data.addIdProperty().primaryKey().autoincrement();
        data.addIntProperty("qtd");
        data.addIntProperty("qtdCandy").columnName("qtd_candy");
        data.addIntProperty("qtdCandyEvolve").columnName("qtd_candy_evolve");
        data.addIntProperty("transfer");
        data.addIntProperty("pokemonNumber").columnName("pokemon_number");
    }
}
