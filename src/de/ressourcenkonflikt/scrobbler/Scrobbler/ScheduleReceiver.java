package de.ressourcenkonflikt.scrobbler.Scrobbler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 13.08.13
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleReceiver extends BroadcastReceiver {
    /**
     * Start the manager service after system boot.
     */
    public void onReceive(Context context, Intent intent) {
        Log.i("Scrobbler onBoot", "Scrobbler onBoot Event called, starting service.");
        Intent service = new Intent(context, ManagerService.class);
        context.startService(service);
    }
}
