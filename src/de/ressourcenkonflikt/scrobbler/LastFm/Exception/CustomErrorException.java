package de.ressourcenkonflikt.scrobbler.LastFm.Exception;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 12.08.13
 * Time: 00:40
 * To change this template use File | Settings | File Templates.
 */
public class CustomErrorException extends Exception {
    private String message;

    public CustomErrorException(String message) {
        this.message = message;
    }
}
