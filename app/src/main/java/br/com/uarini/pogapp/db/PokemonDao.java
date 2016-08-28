package br.com.uarini.pogapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "POKEMON".
*/
public class PokemonDao extends AbstractDao<Pokemon, Long> {

    public static final String TABLENAME = "POKEMON";

    /**
     * Properties of entity Pokemon.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Number = new Property(2, Integer.class, "number", false, "NUMBER");
        public final static Property Image = new Property(3, Integer.class, "image", false, "IMAGE");
    }


    public PokemonDao(DaoConfig config) {
        super(config);
    }
    
    public PokemonDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"POKEMON\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"NUMBER\" INTEGER," + // 2: number
                "\"IMAGE\" INTEGER);"); // 3: image
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"POKEMON\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Pokemon entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Integer number = entity.getNumber();
        if (number != null) {
            stmt.bindLong(3, number);
        }
 
        Integer image = entity.getImage();
        if (image != null) {
            stmt.bindLong(4, image);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Pokemon entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Integer number = entity.getNumber();
        if (number != null) {
            stmt.bindLong(3, number);
        }
 
        Integer image = entity.getImage();
        if (image != null) {
            stmt.bindLong(4, image);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Pokemon readEntity(Cursor cursor, int offset) {
        Pokemon entity = new Pokemon( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // number
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // image
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Pokemon entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNumber(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setImage(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Pokemon entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Pokemon entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Pokemon entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
