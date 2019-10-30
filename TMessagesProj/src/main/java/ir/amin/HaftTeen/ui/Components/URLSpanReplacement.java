/*
 * This is the source code of aan for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package ir.amin.HaftTeen.ui.Components;

import android.net.Uri;
import android.text.style.URLSpan;
import android.view.View;

import ir.amin.HaftTeen.messenger.browser.Browser;

public class URLSpanReplacement extends URLSpan {

    public URLSpanReplacement(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Browser.openUrl(widget.getContext(), uri);
    }
}