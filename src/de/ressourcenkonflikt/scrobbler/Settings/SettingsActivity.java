package de.ressourcenkonflikt.scrobbler.Settings;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import de.ressourcenkonflikt.scrobbler.R;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 10.08.13
 * Time: 22:55
 * To change this template use File | KeyValueStorage | File Templates.
 */
public class SettingsActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private String[] option_names = new String[]{
            "Username",
            "Password",
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_settings);
        this.setListAdapter(createAdapter());

        this.getListView().setOnItemClickListener(this);
    }

    /**
     * Fill list view with option names.
     *
     * @return List
     */
    protected ListAdapter createAdapter() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.option_names);
    }

    protected SettingsDialogBuilder getDialogBuilder(String name) {
        View layout = this.getLayoutInflater().inflate(R.layout.dialog_settings,
                (ViewGroup) this.findViewById(R.layout.activity_settings));

        return new SettingsDialogBuilder(this, layout, name);
    }

    /**
     * On item click:
     * - Load the setting
     * - Open a dialog window
     * - Fill it from the loaded setting
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String option_name = this.option_names[i];

        this.getDialogBuilder(option_name).getDialog().show();
    }
}