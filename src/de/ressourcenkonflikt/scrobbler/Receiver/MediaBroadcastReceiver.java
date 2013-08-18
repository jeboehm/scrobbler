package de.ressourcenkonflikt.scrobbler.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 12.08.13
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public class MediaBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String track = intent.getStringExtra("track");

        if (intent.getAction().contentEquals("com.android.music.metachanged")) {
            if (artist != null && track != null) {
                // Send new event
                Intent new_event = new Intent();

                new_event.setAction("de.ressourcenkonflikt.scrobbler.queue.newitem");

                new_event.putExtra("song_artist", artist);
                new_event.putExtra("song_album", album);
                new_event.putExtra("song_track", track);

                context.sendBroadcast(new_event);
            }
        }
    }
}
