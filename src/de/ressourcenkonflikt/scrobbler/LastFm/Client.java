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
 * This file is part of scrobbler for ASTEROID.
 *
 * https://github.com/jeboehm/scrobbler
 *
 * Copyright (C) 2013 Jeffrey Boehm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Client {
    private static Client ourInstance = new Client();
    private Session session;
    private int success_counter = 0;

    private boolean isAuthenticated = false;
    private String authenticated_username;
    private String authenticated_password;

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
    public boolean authenticate(String username, String password) throws CouldNotConnectException, CustomErrorException {
        if (isAuthenticated && username.equals(authenticated_username) && password.equals(authenticated_password)) {
            return true;
        }

        try {
            session = Authenticator.getMobileSession(username, password, Secrets.lastfm_api_key, Secrets.lastfm_api_secret);

            if (session == null) {
                throw new CustomErrorException(Caller.getInstance().getLastResult().getErrorMessage().trim());
            } else {
                isAuthenticated = true;
                authenticated_username = username;
                authenticated_password = password;

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

        if (scrobble_result.isSuccessful()) {
            success_counter++;
        }

        return scrobble_result.isSuccessful();
    }

    public boolean scrobbleTrack(String artist, String track) throws NotAuthenticatedException {
        return scrobbleTrack(artist, track, new Date());
    }

    public int getSuccessCounter() {
        return success_counter;
    }
}
