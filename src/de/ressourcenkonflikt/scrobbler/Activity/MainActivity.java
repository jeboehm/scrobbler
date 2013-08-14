package de.ressourcenkonflikt.scrobbler.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.ressourcenkonflikt.scrobbler.LastFm.Client;
import de.ressourcenkonflikt.scrobbler.R;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 11.08.13
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        setScrobbleCounter(Client.getInstance().getSuccessCounter());
        setQueueCounter(Queue.getInstance().getSize());
    }

    public void onSettingsClick(View view) {
        Intent i = new Intent(view.getContext(), PrefsActivity.class);
        startActivityForResult(i, 0);
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

    /**
     * Refresh the view.
     */
    @Override
    protected void onResume() {
        super.onResume();

        setQueueCounter(Queue.getInstance().getSize());
        setScrobbleCounter(Client.getInstance().getSuccessCounter());
    }
}