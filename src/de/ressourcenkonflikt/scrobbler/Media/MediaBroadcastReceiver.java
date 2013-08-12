package de.ressourcenkonflikt.scrobbler.Media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.ressourcenkonflikt.scrobbler.ScrobbleQueue.Queue;
import de.ressourcenkonflikt.scrobbler.ScrobbleQueue.Song;

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
                queueSong(artist, track);
            }
        }
    }

    private void queueSong(String artist, String track) {
        Queue queue = Queue.getInstance();
        Song song = new Song(artist, track);

        if (queue.add(song)) {
            Log.i("ScrobbleQueue", String.format("Added '%1$s - %2$s' to the queue.", artist, track));
        } else {
            Log.i("ScrobbleQueue", String.format("Could not add '%1$s - %2$s' to the queue, recently played.",
                    artist, track));
        }
    }
}
