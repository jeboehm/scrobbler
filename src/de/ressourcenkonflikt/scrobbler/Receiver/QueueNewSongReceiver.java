package de.ressourcenkonflikt.scrobbler.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.NotAuthenticatedException;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.SongQueue.Song;
import de.ressourcenkonflikt.scrobbler.Util.ConnectivityChecker;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 14.08.13
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class QueueNewSongReceiver extends BroadcastReceiver {
    private Client client = new Client();

    public void onReceive(Context context, Intent intent) {
        Song song = Queue.getInstance().get();
        ConnectivityChecker con_checker = new ConnectivityChecker(context);

        if (con_checker.getIsOnline()) {
            if (song != null) {
                if (client.getIsAuthenticated() || authenticateClient(context)) {
                    try {
                        if (client.scrobbleTrack(song.getArtist(), song.getTrack(), song.getPlayedAt())) {
                            Queue.getInstance().remove(song);
                            Log.i(getClass().getCanonicalName(), "Track scrobbled successfully.");
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
    }

    private boolean authenticateClient(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        try {
            if (!client.authenticate(username, password)) {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), "Could not authenticate, aborting..");
            return false;
        }

        Log.i(getClass().getCanonicalName(), "Successfully authenticated.");
        return true;
    }
}
