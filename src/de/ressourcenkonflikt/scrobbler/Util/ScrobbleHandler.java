package de.ressourcenkonflikt.scrobbler.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.NotAuthenticatedException;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.SongQueue.Song;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 15.08.13
 * Time: 00:53
 * To change this template use File | Settings | File Templates.
 */
public class ScrobbleHandler {
    private ConnectivityChecker con_checker;
    private Context context;

    public final static int RESULT_NEXT = 0;
    public final static int RESULT_STOP = 1;

    public ScrobbleHandler(ConnectivityChecker con_checker, Context context) {
        this.con_checker = con_checker;
        this.context = context;
    }

    private boolean authenticateClient() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        try {
            if (!Client.getInstance().authenticate(username, password)) {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), "Could not authenticate, aborting..");
            return false;
        }

        Log.i(getClass().getCanonicalName(), "Successfully authenticated.");
        return true;
    }

    public int scrobbleSong(Song song) {
        if (con_checker.getIsOnline()) {
            if (song != null) {
                if (Client.getInstance().getIsAuthenticated() || authenticateClient()) {
                    try {
                        if (Client.getInstance().scrobbleTrack(song.getArtist(), song.getTrack(), song.getPlayedAt())) {
                            Queue.getInstance().remove(song);
                            Log.i(getClass().getCanonicalName(), "Track scrobbled successfully.");

                            return RESULT_NEXT;
                        } else {
                            Log.e(getClass().getCanonicalName(), "Can't scrobble track, aborting..");
                        }
                    } catch (NotAuthenticatedException e) {
                        Log.e(getClass().getCanonicalName(), "Could not authenticate, aborting..");
                    }
                }
            } else {
                Log.i(getClass().getCanonicalName(), "No song in queue, aborting..");
            }
        } else {
            Log.i(getClass().getCanonicalName(), "We're not online, aborting..");
        }

        return RESULT_STOP;
    }
}
