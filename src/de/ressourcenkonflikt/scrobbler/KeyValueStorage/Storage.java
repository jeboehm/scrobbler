package de.ressourcenkonflikt.scrobbler.KeyValueStorage;

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

    public static Storage getInstance() {
        return instance;
    }

    private KeyValue findObjectWithKey(String key) {
        for (KeyValue obj : this.key_values) {
            if (obj.getKey().equals(key)) {
                return obj;
            }
        }

        return null;
    }

    public String getValue(String key) {
        KeyValue obj = this.findObjectWithKey(key);

        if (obj != null) {
            return obj.getValue();
        }

        return null;
    }

    public void setValue(String key, String value) {
        KeyValue obj = this.findObjectWithKey(key);

        if (obj != null) {
            obj.setValue(value);
        } else {
            obj = new KeyValue(key, value);
            this.key_values.add(obj);
        }
    }
}
