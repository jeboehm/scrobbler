package de.ressourcenkonflikt.scrobbler.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.SongQueue.Song;

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
                if (queueSong(artist, track)) {
                    // Send new event
                    Intent new_event = new Intent();
                    new_event.setAction("de.ressourcenkonflikt.scrobbler.queue.newitem");
                    context.sendBroadcast(new_event);
                }
            }
        }
    }

    private boolean queueSong(String artist, String track) {
        Queue queue = Queue.getInstance();
        Song song = new Song(artist, track);

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
