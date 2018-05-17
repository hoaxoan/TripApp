package com.winter.dreamhub.trips;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.google.common.base.Preconditions;

/**
 * Created by hoaxoan on 3/12/2017.
 */

public final class UriIntendingOnClickListener
        implements View.OnClickListener {
    private final String action;
    private final Activity activity;
    private final Uri uri;

    public UriIntendingOnClickListener(Activity activity, Uri uri) {
        this(activity, uri, "android.intent.action.VIEW");
    }

    public UriIntendingOnClickListener(Activity activity, Uri uri, String action) {
        this.uri = uri;
        this.activity = ((Activity) Preconditions.checkNotNull(activity));
        this.action = ((String) Preconditions.checkNotNull(action));
    }

    public final void onClick(View view) {
        if (this.uri == null) {
            return;
        }
        Intent intent = new Intent(this.action);
        intent.setData(this.uri);
        if (intent.resolveActivity(this.activity.getPackageManager()) != null) {
            this.activity.startActivity(intent);
        }
    }
}