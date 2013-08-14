package de.ressourcenkonflikt.scrobbler.LastFm;

import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CouldNotConnectException;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CustomErrorException;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.NotAuthenticatedException;
import de.ressourcenkonflikt.scrobbler.Secrets;
import de.umass.lastfm.*;
import de.umass.lastfm.cache.Cache;
import de.umass.lastfm.cache.MemoryCache;
import de.umass.lastfm.scrobble.ScrobbleResult;

import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 11.08.13
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    private static Client ourInstance = new Client();
    private Session session;
    private boolean isAuthenticated = false;

    public static Client getInstance() {
        return ourInstance;
    }

    public Client() {
        Caller.getInstance().setUserAgent("scrobbler for Parrot Asteroid Smart");
        Caller.getInstance().setCache(getCache());
    }

    private Cache getCache() {
        return new MemoryCache();
    }

    /**
     * Get unixtime.
     */
    private int getTime(Date date) {
        long unixtime = date.getTime() / 1000L;

        return (int) unixtime;
    }

    /**
     * Authenticate to the last.fm service.
     */
    public boolean authenticate(String username, String password)
            throws CouldNotConnectException, CustomErrorException {
        try {
            session = Authenticator.getMobileSession(username, password, Secrets.lastfm_api_key, Secrets.lastfm_api_secret);

            if (session == null) {
                throw new CustomErrorException(Caller.getInstance().getLastResult().getErrorMessage());
            } else {
                isAuthenticated = true;
                return true;
            }
        } catch (CallException e) {
            if (e.getCause() instanceof UnknownHostException) {
                throw new CouldNotConnectException();
            } else {
                throw e;
            }
        }
    }

    public boolean scrobbleTrack(String artist, String track, Date date) throws NotAuthenticatedException {
        if (!isAuthenticated) {
            throw new NotAuthenticatedException();
        }

        ScrobbleResult scrobble_result = Track.scrobble(artist, track, getTime(date), session);

        return scrobble_result.isSuccessful();
    }

    public boolean scrobbleTrack(String artist, String track) throws NotAuthenticatedException {
        return scrobbleTrack(artist, track, new Date());
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }
}
