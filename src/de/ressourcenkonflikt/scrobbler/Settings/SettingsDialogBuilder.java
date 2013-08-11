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
        this.storage = Storage.getInstance();

        this.label_view = (TextView) layout.findViewById(R.id.dialog_settings_option_label);
        this.value_edit = (EditText) layout.findViewById(R.id.dialog_settings_option_value);
    }

    private String getEditValue() {
        return this.value_edit.getText().toString();
    }

    public AlertDialog getDialog() {
        this.fillDialogElements();
        AlertDialog.Builder dialog_builder = this.createBuilder();

        return dialog_builder.create();
    }

    private void fillDialogElements() {
        this.label_view.setText(this.option_name);
        this.value_edit.setText(this.storage.getValue(this.option_name));
    }

    private AlertDialog.Builder createBuilder() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this.parent_activity);
        dialog_builder
                .setView(this.layout)
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
