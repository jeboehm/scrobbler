package de.ressourcenkonflikt.scrobbler.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.R;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.Util.ScrobbleHandler;

import java.util.ArrayList;

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
public class StatusActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        refreshView();
    }

    private void flushQueue() {
        ScrobbleHandler handler =
                new ScrobbleHandler(this);

        while (Queue.getInstance().getSize() > 0) {
            int result = handler.scrobbleSong(Queue.getInstance().get());

            if (result == ScrobbleHandler.RESULT_STOP) {
                Toast toast = Toast.makeText(this, getResources().getString(R.string.message_could_not_flush_queue), 5);
                toast.show();
                return;
            }
        }

        Toast toast = Toast.makeText(this, getResources().getString(R.string.message_queue_successfully_flushed), 5);
        toast.show();

        refreshView();
    }

    public void setScrobbleCounter(Integer counter) {
        TextView view = (TextView) findViewById(R.id.status_text_scrobblecounter);
        view.setText(counter.toString());
    }

    public void setQueueCounter(Integer counter) {
        TextView view = (TextView) findViewById(R.id.status_text_queuecounter);
        view.setText(counter.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_flush_queue:
                flushQueue();
                break;

            case R.id.menu_main_preferences:
                Intent activity_preferences = new Intent(this, PreferencesActivity.class);
                startActivity(activity_preferences);
                break;

            case R.id.menu_main_about:
                Intent activity_about = new Intent(this, AboutActivity.class);
                startActivity(activity_about);
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    protected void refreshView() {
        setQueueCounter(Queue.getInstance().getSize());
        setScrobbleCounter(Client.getInstance().getTracksScrobbledCount());
        refreshTrackList();
    }

    protected void refreshTrackList() {
        ScrobbleHandler handler = new ScrobbleHandler(this);

        ListView trackList = (ListView) findViewById(R.id.status_list_view);

        ArrayList<String> tracks = handler.getLastTracks();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tracks);
        trackList.setAdapter(adapter);
    }
}