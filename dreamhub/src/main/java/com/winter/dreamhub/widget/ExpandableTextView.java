package com.winter.dreamhub.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.winter.dreamhub.R;

/**
 * Created by hoaxoan on 10/31/2016.
 */

public class ExpandableTextView extends TextView {
    int collapsedLineCount;
    boolean isAlwaysExpanded;
    boolean isExpanded;

    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEllipsize(TextUtils.TruncateAt.END);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.collapsedLineCount = a.getInt(R.styleable.ExpandableTextView_collapsedLineCount, 0);
        a.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxlines = 0;
                if (!ExpandableTextView.this.isExpanded) {
                    maxlines = Integer.MAX_VALUE;
                    ExpandableTextView.this.isExpanded = true;
                } else {
                    maxlines = ExpandableTextView.this.collapsedLineCount;
                    ExpandableTextView.this.isExpanded = false;
                }

                ExpandableTextView.this.setMaxLines(maxlines);
            }
        });

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        int maxlines = 0;
        if (this.isExpanded) {
            maxlines = Integer.MAX_VALUE;
        } else {
            maxlines = this.collapsedLineCount;
        }
        setMaxLines(maxlines);
    }

    public static final class ExpandableTextViewState {
        boolean isAlwaysExpanded;
        boolean isExpanded;
    }
}
