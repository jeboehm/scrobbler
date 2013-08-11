package de.ressourcenkonflikt.scrobbler.KeyValueStorage;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 11.08.13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class KeyValue {
    private String key;
    private String value;

    /**
     * Constructor.
     *
     * @param key   Key
     * @param value Value
     */
    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
