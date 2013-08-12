package de.ressourcenkonflikt.scrobbler;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.ressourcenkonflikt.scrobbler.Media.MediaBroadcastReceiver;
import de.ressourcenkonflikt.scrobbler.ScrobbleQueue.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 11.08.13
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends Activity {
    private static MediaBroadcastReceiver media_broadcast_receiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        setScrobbleCounter(0);
        setQueueCounter(Queue.getInstance().getSize());

        if (media_broadcast_receiver == null) {
            media_broadcast_receiver = new MediaBroadcastReceiver();
            registerReceiver(media_broadcast_receiver, getIntentFilterForMediaBroadcast());
        }

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

    private IntentFilter getIntentFilterForMediaBroadcast() {
        IntentFilter intent_filter = new IntentFilter();

        intent_filter.addAction("com.android.music.metachanged");
        intent_filter.addAction("com.android.music.playstatechanged");
        intent_filter.addAction("com.android.music.playbackcomplete");
        intent_filter.addAction("com.android.music.queuechanged");

        return intent_filter;
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