package de.ressourcenkonflikt.scrobbler.Util;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import de.ressourcenkonflikt.scrobbler.Activity.MainActivity;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CouldNotConnectException;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CustomErrorException;
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

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private boolean authenticateClient() {
        String username = getPreferences().getString("username", "");
        String password = getPreferences().getString("password", "");

        try {
            Client.getInstance().authenticate(username, password);
            Log.i(getClass().getCanonicalName(), "Successfully authenticated.");

            return true;
        } catch (CouldNotConnectException e) {
            Log.e(getClass().getCanonicalName(), "Could not connect to Last.fm, aborting..");
        } catch (CustomErrorException e) {
            if (e.getMessage() != null) {
                sendNotification("Error", String.format("Can't scrobble track: %1$s", e.getMessage()), "Scrobble error");
            } else {
                sendNotification("Error", "Can't scrobble track, check settings!", "Scrobble error");
            }

            Log.e(getClass().getCanonicalName(), String.format("Could not authenticate: %1$s", e.getMessage()));
        }

        return false;
    }

    private boolean getScrobblerIsEnabled() {
        return getPreferences().getBoolean("enabled", false);
    }

    public int scrobbleSong(Song song) {
        if (getScrobblerIsEnabled()) {
            if (con_checker.getIsOnline()) {
                if (song != null) {
                    if (authenticateClient()) {
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
        } else {
            Log.i(getClass().getCanonicalName(), "Scrobbler is not enabled, aborting..");
        }

        return RESULT_STOP;
    }

    private void sendNotification(String title, String text, String ticker) {
        NotificationManager notification_manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification new_notification = new Notification(R.drawable.sym_def_app_icon, ticker, System.currentTimeMillis());

        Intent target_intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, target_intent, Intent.FLAG_ACTIVITY_NEW_TASK);

        new_notification.defaults |= Notification.DEFAULT_SOUND;
        new_notification.flags |= Notification.FLAG_AUTO_CANCEL;
        new_notification.setLatestEventInfo(context, title, text, pendingIntent);
        notification_manager.notify(1, new_notification);
    }
}
