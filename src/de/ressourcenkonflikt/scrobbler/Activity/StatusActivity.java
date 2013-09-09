package de.ressourcenkonflikt.scrobbler.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.R;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.SongQueue.Song;
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
    private ArrayList<Song> trackList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        ListView listview;
        listview = (ListView) findViewById(R.id.status_list_view);
        registerForContextMenu(listview);

        refreshView();
    }

    private void flushQueue() {
        ScrobbleHandler handler;
        Toast toast;

        handler = new ScrobbleHandler(this);

        while (Queue.getInstance().getSize() > 0) {
            int result;
            result = handler.scrobbleSong(Queue.getInstance().get());

            if (result == ScrobbleHandler.RESULT_STOP) {
                toast = Toast.makeText(this, getResources().getString(R.string.message_could_not_flush_queue), 5);
                toast.show();
                return;
            }
        }

        toast = Toast.makeText(this, getResources().getString(R.string.message_queue_successfully_flushed), 5);
        toast.show();

        refreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater;

        inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent startIntent;

        switch (item.getItemId()) {
            case R.id.menu_main_flush_queue:
                flushQueue();
                break;

            case R.id.menu_main_preferences:
                startIntent = new Intent(this, PreferencesActivity.class);
                startActivity(startIntent);
                break;

            case R.id.menu_main_about:
                startIntent = new Intent(this, AboutActivity.class);
                startActivity(startIntent);
                break;

            case R.id.menu_main_refresh:
                refreshTrackListInBackground();
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
        TextView statusText;
        String text;

        text = getText(R.string.status_text).toString();
        statusText = (TextView) findViewById(R.id.status_text);

        statusText.setText(
                String.format(
                        text,
                        Queue.getInstance().getSize(),
                        Client.getInstance().getTracksScrobbledCount()
                )
        );

        refreshTrackListInBackground();
    }

    protected static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread thread;

        thread = new Thread() {
            @Override
            public void run() {
                runnable.run();
            }
        };

        thread.start();
        return thread;
    }

    protected void refreshTrackListInBackground() {
        ProgressBar progressBar;
        ListView trackList;
        Runnable runnable;

        progressBar = (ProgressBar) findViewById(R.id.status_progressbar);
        trackList = (ListView) findViewById(R.id.status_list_view);

        progressBar.setVisibility(View.VISIBLE);
        trackList.setAdapter(null);

        runnable = new Runnable() {
            @Override
            public void run() {
                refreshTrackList();
            }
        };

        performOnBackgroundThread(runnable);
    }

    protected void refreshTrackList() {
        ScrobbleHandler handler;

        handler = new ScrobbleHandler(this);
        trackList = handler.getLastTracks();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar;
                ListView trackList;
                ArrayAdapter<String> adapter;

                progressBar = (ProgressBar) findViewById(R.id.status_progressbar);
                trackList = (ListView) findViewById(R.id.status_list_view);

                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1);

                for (Song song : StatusActivity.this.trackList) {
                    adapter.add(song.toString());
                }

                trackList.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String[] menuItems;

        if (v.getId() == R.id.status_list_view) {
            menu.setHeaderTitle(trackList.get(info.position).toString());
            menuItems = getResources().getStringArray(R.array.status_contextmenu);

            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
}