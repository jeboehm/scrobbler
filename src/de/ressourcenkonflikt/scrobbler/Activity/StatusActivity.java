package de.ressourcenkonflikt.scrobbler.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.R;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.Util.ConnectivityChecker;
import de.ressourcenkonflikt.scrobbler.Util.ScrobbleHandler;

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
        setScrobbleCounter(Client.getInstance().getSuccessCounter());
        setQueueCounter(Queue.getInstance().getSize());
        setSendQueueButtonState();
    }

    public void onSettingsClick(View view) {
        Intent i = new Intent(view.getContext(), PreferencesActivity.class);
        startActivityForResult(i, 0);
    }

    public void onSendQueueClick(View view) {
        ScrobbleHandler handler =
                new ScrobbleHandler(new ConnectivityChecker(getApplicationContext()), getApplicationContext());

        while (Queue.getInstance().getSize() > 0) {
            int result = handler.scrobbleSong(Queue.getInstance().get());

            if (result == ScrobbleHandler.RESULT_STOP) {
                break;
            }
        }

        onResume();
    }

    public void setScrobbleCounter(Integer counter) {
        TextView view = (TextView) findViewById(R.id.status_text_scrobblecounter);
        view.setText(
                getString(R.string.status_scrobblecount_text, counter.toString())
        );
    }

    public void setQueueCounter(Integer counter) {
        TextView view = (TextView) findViewById(R.id.status_text_queuecounter);
        view.setText(
                getString(R.string.status_queuecount_text, counter.toString())
        );
    }

    public void setSendQueueButtonState() {
        Button button = (Button) findViewById(R.id.status_button_flush_queue);

        if (Queue.getInstance().getSize() > 0) {
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }
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
            case R.id.menu_main_preferences:
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                break;

            case R.id.menu_main_about:
                break;
        }

        return true;
    }

    /**
     * Refresh the view.
     */
    @Override
    protected void onResume() {
        super.onResume();

        setQueueCounter(Queue.getInstance().getSize());
        setScrobbleCounter(Client.getInstance().getSuccessCounter());
        setSendQueueButtonState();
    }
}