package de.ressourcenkonflikt.scrobbler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.ressourcenkonflikt.scrobbler.ScrobbleQueue.Queue;

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
        setScrobbleCounter(0);
        setQueueCounter(Queue.getInstance().getSize());

        //moveTaskToBack(true);
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
    }
}