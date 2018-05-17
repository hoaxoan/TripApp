package com.winter.dreamhub.gcm;

import android.content.Context;

/**
 * Created by hoaxoan on 12/26/2016.
 */

public abstract class GCMCommand {
    public abstract void execute(Context context, String type, String extraData);
}