package com.winter.dreamhub.widget;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.api.service.model.Landmark;

import java.io.File;

/**
 * Created by hoaxoan on 2/24/2017.
 */

public class ShareImageTask extends AsyncTask<Void, Void, File> {

    private final Activity activity;
    private final Entity entity;

    public ShareImageTask(Activity activity, Entity entity) {
        this.activity = activity;
        this.entity = entity;
    }

    @Override
    protected File doInBackground(Void... params) {
        String url = "";
        if (entity.photos != null && entity.photos.size() > 0) {
            url = WinterService.ENDPOINT + entity.photos.get(0).url;
        }
        try {
            return Glide
                    .with(activity)
                    .load(url)
                    .downloadOnly((int) 32, (int) 32)
                    .get();
        } catch (Exception ex) {
            Log.w("SHARE", "Sharing " + url + " failed", ex);
            return null;
        }
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            return;
        }
        // glide cache uses an unfriendly & extension-less name,
        // massage it based on the original
        String fileName = "";
        if (entity.photos != null && entity.photos.size() > 0) {
            fileName = entity.photos.get(0).name;
        }
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        File renamed = new File(result.getParent(), fileName);
        result.renameTo(renamed);
        Uri uri = FileProvider.getUriForFile(activity,
                activity.getString(R.string.share_authority), renamed);
        ShareCompat.IntentBuilder.from(activity)
                .setText(getShareText())
                .setType(getImageMimeType(fileName))
                .setSubject(entity.name)
                .setStream(uri)
                .startChooser();
    }

    private String getShareText() {
        String url = "";
        if (entity.photos != null && entity.photos.size() > 0) {
            url = WinterService.ENDPOINT + entity.photos.get(0).url;
        }
        return new StringBuilder()
                .append("“")
                .append(entity.name)
                .append("” by ")
                .append("\n")
                .append(url)
                .toString();
    }

    private String getImageMimeType(@NonNull String fileName) {
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }
}

