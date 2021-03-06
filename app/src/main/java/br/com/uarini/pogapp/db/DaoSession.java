package br.com.uarini.pogapp.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import br.com.uarini.pogapp.db.Pokemon;
import br.com.uarini.pogapp.db.PokemonData;

import br.com.uarini.pogapp.db.PokemonDao;
import br.com.uarini.pogapp.db.PokemonDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig pokemonDaoConfig;
    private final DaoConfig pokemonDataDaoConfig;

    private final PokemonDao pokemonDao;
    private final PokemonDataDao pokemonDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        pokemonDaoConfig = daoConfigMap.get(PokemonDao.class).clone();
        pokemonDaoConfig.initIdentityScope(type);

        pokemonDataDaoConfig = daoConfigMap.get(PokemonDataDao.class).clone();
        pokemonDataDaoConfig.initIdentityScope(type);

        pokemonDao = new PokemonDao(pokemonDaoConfig, this);
        pokemonDataDao = new PokemonDataDao(pokemonDataDaoConfig, this);

        registerDao(Pokemon.class, pokemonDao);
        registerDao(PokemonData.class, pokemonDataDao);
    }
    
    public void clear() {
        pokemonDaoConfig.clearIdentityScope();
        pokemonDataDaoConfig.clearIdentityScope();
    }

    public PokemonDao getPokemonDao() {
        return pokemonDao;
    }

    public PokemonDataDao getPokemonDataDao() {
        return pokemonDataDao;
    }

}
