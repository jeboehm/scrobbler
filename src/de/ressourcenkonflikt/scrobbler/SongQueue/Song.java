package de.ressourcenkonflikt.scrobbler.SongQueue;

import java.util.Date;

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
public class Song {
    private String artist;
    private String track;
    private Date playedAt;

    public Song(String artist, String track) {
        this.artist = artist;
        this.track = track;
        this.playedAt = new Date();
    }

    public Song(String artist, String track, Date playedAt) {
        this.artist = artist;
        this.track = track;
        this.playedAt = playedAt;
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

    public String toString() {
        return String.format("%1s - %2s", getArtist(), getTrack());
    }
}
