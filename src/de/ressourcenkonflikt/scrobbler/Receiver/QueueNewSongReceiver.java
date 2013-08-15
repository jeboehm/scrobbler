package de.ressourcenkonflikt.scrobbler.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.ressourcenkonflikt.scrobbler.SongQueue.Queue;
import de.ressourcenkonflikt.scrobbler.Util.ConnectivityChecker;
import de.ressourcenkonflikt.scrobbler.Util.ScrobbleHandler;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 14.08.13
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class QueueNewSongReceiver extends BroadcastReceiver {
    ScrobbleHandler handler;

    public void onReceive(Context context, Intent intent) {
        if (handler == null) {
            handler = new ScrobbleHandler(new ConnectivityChecker(context), context);
        }

        while (Queue.getInstance().getSize() > 0) {
            int result = handler.scrobbleSong(Queue.getInstance().get());

            if (result == ScrobbleHandler.RESULT_STOP) {
                break;
            }
        }
    }
}
