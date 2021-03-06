package de.ressourcenkonflikt.scrobbler.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import de.ressourcenkonflikt.scrobbler.R;

/**
 * This file is part of scrobbler for ASTEROID.
 *
 * https://github.com/jeboehm/scrobbler
 *
 * Copyright (C) 2013 Jeffrey Boehm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setAboutText();
    }

    protected void setAboutText() {
        TextView textview;

        textview = (TextView) findViewById(R.id.about_textview);
        textview.setText(Html.fromHtml(getResources().getString(R.string.text_about)));
    }
}