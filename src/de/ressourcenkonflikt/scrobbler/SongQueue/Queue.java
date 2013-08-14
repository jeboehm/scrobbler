package de.ressourcenkonflikt.scrobbler.SongQueue;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 12.08.13
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
public class Queue {
    private static Queue ourInstance = new Queue();
    private ArrayList<Song> queue = new ArrayList<Song>();

    public static Queue getInstance() {
        return ourInstance;
    }

    private Queue() {
    }

    public int getSize() {
        return queue.size();
    }

    public boolean add(Song new_song) {
        for (Song queued_song : queue) {
            if (queued_song.isSameSong(new_song)) {
                /*
                 * The same song is already queued.
                 * If the queued song was played in the last
                 * 30 seconds, don't add the new song.
                 */

                long new_song_seconds = new_song.getPlayedAt().getTime() / 1000;
                long queued_song_seconds = queued_song.getPlayedAt().getTime() / 1000;

                if ((new_song_seconds - queued_song_seconds) < 30) {
                    return false;
                }
            }
        }

        queue.add(new_song);

        return true;
    }

    /**
     * Pull the first song from the queue.
     */
    public Song shift() {
        Song return_song;

        if (getSize() > 0) {
            return_song = queue.get(0);
            queue.remove(0);

            return return_song;
        }

        return null;
    }
}
