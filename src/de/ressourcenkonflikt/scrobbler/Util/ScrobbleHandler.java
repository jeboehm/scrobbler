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
import de.ressourcenkonflikt.scrobbler.Activity.StatusActivity;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CouldNotConnectException;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.CustomErrorException;
import de.ressourcenkonflikt.scrobbler.LastFm.Exception.NotAuthenticatedException;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.SongQueue.Song;

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
public class ScrobbleHandler {
    private ConnectivityChecker con_checker;
    private Context context;

    public final static int RESULT_NEXT = 0;
    public final static int RESULT_STOP = 1;

    public ScrobbleHandler(Context context) {
        this.con_checker = new ConnectivityChecker(context);
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
                            if (Client.getInstance().scrobbleTrack(song)) {
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

        Intent target_intent = new Intent(context, StatusActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, target_intent, Intent.FLAG_ACTIVITY_NEW_TASK);

        new_notification.defaults |= Notification.DEFAULT_SOUND;
        new_notification.flags |= Notification.FLAG_AUTO_CANCEL;
        new_notification.setLatestEventInfo(context, title, text, pendingIntent);
        notification_manager.notify(1, new_notification);
    }
}
