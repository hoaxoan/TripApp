package com.winter.dreamhub.widget;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public interface VerticalScrollListener {
    void onScrolled(int position);

    void slideHeaderOut();

    void slideHeaderToFirstCardTop(int position);
}
