package de.ressourcenkonflikt.scrobbler.LastFm;

import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CouldNotConnectException;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CustomErrorException;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.NotAuthenticatedException;
import de.ressourcenkonflikt.scrobbler.Secrets;
import de.ressourcenkonflikt.scrobbler.Util.Unixtime;
import de.umass.lastfm.*;
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
    private int countTracksScrobbled = 0;

    private boolean isAuthenticated = false;
    private String authenticatedUsername;
    private String authenticatedPassword;

    public static Client getInstance() {
        return ourInstance;
    }

    public Client() {
        Caller.getInstance().setUserAgent("scrobbler for Parrot ASTEROID Smart");
        Caller.getInstance().setCache(new MemoryCache());
    }

    /**
     * Authenticate to the last.fm service.
     */
    public boolean authenticate(String username, String password) throws CouldNotConnectException, CustomErrorException {
        if (isAuthenticated && username.equals(authenticatedUsername) && password.equals(authenticatedPassword)) {
            return true;
        }

        try {
            session = Authenticator.getMobileSession(username, password, Secrets.lastfm_api_key, Secrets.lastfm_api_secret);

            if (session == null) {
                throw new CustomErrorException(Caller.getInstance().getLastResult().getErrorMessage().trim());
            } else {
                isAuthenticated = true;
                authenticatedUsername = username;
                authenticatedPassword = password;

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

        ScrobbleResult scrobbleResult = Track.scrobble(artist, track, Unixtime.getUnixtime(date), session);

        if (scrobbleResult.isSuccessful()) {
            countTracksScrobbled++;
        }

        return scrobbleResult.isSuccessful();
    }

    public int getTracksScrobbledCount() {
        return countTracksScrobbled;
    }
}
