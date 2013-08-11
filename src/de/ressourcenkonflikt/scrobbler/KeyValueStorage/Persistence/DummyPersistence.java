package de.ressourcenkonflikt.scrobbler.KeyValueStorage.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 12.08.13
 * Time: 01:31
 * To change this template use File | Settings | File Templates.
 */
public class DummyPersistence implements PersistenceInterface {

    @Override
    public String getValue(String key) {
        return null;
    }

    @Override
    public void setValue(String key, String value) {

    }
}
