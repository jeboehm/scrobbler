package de.ressourcenkonflikt.scrobbler.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.SongQueue.Song;
import de.ressourcenkonflikt.scrobbler.Util.ScrobbleHandler;

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
public class QueueNewSongReceiver extends BroadcastReceiver {
    ScrobbleHandler handler;

    public void onReceive(Context context, Intent intent) {
        if (queueSong(intent.getStringExtra("song_artist"), intent.getStringExtra("song_track"))) {
            if (handler == null) {
                handler = new ScrobbleHandler(context);
            }

            while (Queue.getInstance().getSize() > 0) {
                int result;

                result = handler.scrobbleSong(Queue.getInstance().get());

                if (result == ScrobbleHandler.RESULT_STOP) {
                    break;
                }
            }
        }
    }

    private boolean queueSong(String artist, String track) {
        Queue queue;
        Song song;

        queue = Queue.getInstance();
        song = new Song(artist, track);

        if (queue.add(song)) {
            Log.i(getClass().getCanonicalName(),
                    String.format("Added '%1$s - %2$s' to the queue.", artist, track));

            return true;
        } else {
            Log.i(getClass().getCanonicalName(),
                    String.format("Could not add '%1$s - %2$s' to the queue, recently played.",
                            artist, track));
        }

        return false;
    }
}
