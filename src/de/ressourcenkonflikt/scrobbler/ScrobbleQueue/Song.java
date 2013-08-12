package de.ressourcenkonflikt.scrobbler.ScrobbleQueue;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 12.08.13
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
public class Song {
    private String artist;
    private String track;
    private Date playedAt;

    public Song(String artist, String track) {
        this.artist = artist;
        this.track = track;
        this.playedAt = new Date();
    }

    public String getArtist() {
        return artist;
    }

    public String getTrack() {
        return track;
    }

    public Date getPlayedAt() {
        return playedAt;
    }

    /**
     * Check, if the given song is the same as this one.
     */
    public boolean isSameSong(Song compare_song) {
        return compare_song.getArtist().equalsIgnoreCase(artist)
                && compare_song.getTrack().equalsIgnoreCase(track);
    }
}
