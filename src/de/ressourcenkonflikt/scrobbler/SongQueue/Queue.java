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
    private ArrayList<Song> queue_history = new ArrayList<Song>();

    public static Queue getInstance() {
        return ourInstance;
    }

    private Queue() {
    }

    public int getSize() {
        return queue.size();
    }

    public boolean add(Song new_song) {
        ArrayList<Song> merged_queue = new ArrayList<Song>();
        merged_queue.addAll(queue);
        merged_queue.addAll(queue_history);

        for (Song queued_song : merged_queue) {
            if (queued_song.isSameSong(new_song)) {
                /*
                 * The same song is already queued.
                 * If the queued song was played in the last
                 * 30 seconds, don't add the new song.
                 */
                if ((new_song.getPlayedAt().getTime() - queued_song.getPlayedAt().getTime()) < 30 * 1000) {
                    return false;
                }
            }
        }

        queue.add(new_song);

        return true;
    }

    /**
     * Get first song in queue.
     */
    public Song get() {
        if (getSize() > 0) {
            return queue.get(0);
        }

        return null;
    }

    /**
     * Remove the given song from the queue.
     */
    public boolean remove(Song song) {
        queue_history.add(song);

        return queue.remove(song);
    }
}
