package de.ressourcenkonflikt.scrobbler.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
public class MediaBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String artist;
        String track;
        String album;
        Intent new_event;

        artist = intent.getStringExtra("artist");
        album = intent.getStringExtra("album");
        track = intent.getStringExtra("track");

        if (intent.getAction().contentEquals("com.android.music.metachanged")) {
            if (artist != null && track != null) {
                // Send new event
                new_event = new Intent();
                new_event.setAction("de.ressourcenkonflikt.scrobbler.queue.newitem");
                new_event.putExtra("song_artist", artist);
                new_event.putExtra("song_album", album);
                new_event.putExtra("song_track", track);

                context.sendBroadcast(new_event);
            }
        }
    }
}
