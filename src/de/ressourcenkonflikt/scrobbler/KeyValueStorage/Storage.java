package de.ressourcenkonflikt.scrobbler.KeyValueStorage;

import de.ressourcenkonflikt.scrobbler.KeyValueStorage.Persistence.DummyPersistence;
import de.ressourcenkonflikt.scrobbler.KeyValueStorage.Persistence.PersistenceInterface;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 11.08.13
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class Storage {
    private ArrayList<KeyValue> key_values = new ArrayList<KeyValue>();
    private static final Storage instance = new Storage();
    private PersistenceInterface persistence;

    public static Storage getInstance() {
        return instance;
    }

    public Storage() {
        persistence = new DummyPersistence();
    }

    public void setPersistence(PersistenceInterface persistence) {
        this.persistence = persistence;
    }

    private KeyValue findObjectWithKey(String key) {
        for (KeyValue obj : key_values) {
            if (obj.getKey().equals(key)) {
                return obj;
            }
        }

        return null;
    }

    public String getValue(String key) {
        KeyValue obj = findObjectWithKey(key);

        if (obj != null) {
            return obj.getValue();
        } else {
            String pers_value = persistence.getValue(key);

            if (pers_value != null) {
                obj = new KeyValue(key, pers_value);
                key_values.add(obj);

                return pers_value;
            }
        }

        return null;
    }

    public void setValue(String key, String value) {
        KeyValue obj = findObjectWithKey(key);

        if (obj != null) {
            obj.setValue(value);
        } else {
            obj = new KeyValue(key, value);
            key_values.add(obj);
        }

        persistence.setValue(key, value);
    }
}
