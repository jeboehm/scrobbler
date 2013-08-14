package de.ressourcenkonflikt.scrobbler.Activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import de.ressourcenkonflikt.scrobbler.R;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 12.08.13
 * Time: 20:37
 * To change this template use File | Settings | File Templates.
 */
public class PrefsActivity extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}