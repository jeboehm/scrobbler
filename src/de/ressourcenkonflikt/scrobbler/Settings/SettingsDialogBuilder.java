package de.ressourcenkonflikt.scrobbler.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.ressourcenkonflikt.scrobbler.KeyValueStorage.Storage;
import de.ressourcenkonflikt.scrobbler.R;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 11.08.13
 * Time: 20:23
 * To change this template use File | Settings | File Templates.
 */
public class SettingsDialogBuilder {
    private Activity parent_activity;
    private View layout;
    private String option_name;
    private Storage storage;
    private TextView label_view;
    private EditText value_edit;

    public SettingsDialogBuilder(Activity parent_activity, View layout, String option_name) {
        this.parent_activity = parent_activity;
        this.layout = layout;
        this.option_name = option_name;

        storage = Storage.getInstance();

        label_view = (TextView) layout.findViewById(R.id.dialog_settings_option_label);
        value_edit = (EditText) layout.findViewById(R.id.dialog_settings_option_value);
    }

    private String getEditValue() {
        return value_edit.getText().toString();
    }

    public AlertDialog getDialog() {
        fillDialogElements();
        AlertDialog.Builder dialog_builder = createBuilder();

        return dialog_builder.create();
    }

    private void fillDialogElements() {
        label_view.setText(option_name);
        value_edit.setText(storage.getValue(option_name));
    }

    private AlertDialog.Builder createBuilder() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(parent_activity);
        dialog_builder
                .setView(layout)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        storage.setValue(option_name, getEditValue());
                        Toast.makeText(layout.getContext(),
                                R.string.setting_saved, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return dialog_builder;
    }
}
