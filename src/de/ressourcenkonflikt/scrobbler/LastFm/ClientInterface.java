package de.ressourcenkonflikt.scrobbler.LastFm;

import de.ressourcenkonflikt.scrobbler.LastFm.Exception.NotAuthenticatedException;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 13.08.13
 * Time: 07:30
 * To change this template use File | Settings | File Templates.
 */
public interface ClientInterface {
    public boolean scrobbleTrack(String artist, String track, Date date) throws NotAuthenticatedException;
}
