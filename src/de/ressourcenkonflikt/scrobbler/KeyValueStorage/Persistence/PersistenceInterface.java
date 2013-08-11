package de.ressourcenkonflikt.scrobbler.KeyValueStorage.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 12.08.13
 * Time: 01:28
 * To change this template use File | Settings | File Templates.
 */
public interface PersistenceInterface {
    public String getValue(String key);

    public void setValue(String key, String value);
}
