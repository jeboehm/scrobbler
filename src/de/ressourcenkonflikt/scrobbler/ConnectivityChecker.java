package de.ressourcenkonflikt.scrobbler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 11.08.13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public class ConnectivityChecker {
    private Context context;

    public ConnectivityChecker(Context context) {
        this.context = context;
    }

    /**
     * Check network connectivity.
     *
     * @return True if we have access to the internet (i hope so).
     */
    public boolean getIsOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            for (NetworkInfo info : cm.getAllNetworkInfo()) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }

        return false;
    }
}
