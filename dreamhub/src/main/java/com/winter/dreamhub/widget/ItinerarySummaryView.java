package com.winter.dreamhub.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.winter.dreamhub.R;

/**
 * Created by hoaxoan on 11/8/2016.
 */

public class ItinerarySummaryView extends ToggleFrameLayout {

    //public RecyclerView recyclerView;

    public ItinerarySummaryView(Context context) {
        super(context);
        init(context);
    }

    public ItinerarySummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItinerarySummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private final void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.firstView = inflater.inflate(R.layout.itinerary_summary, this, false);
        ((ImageView) this.firstView.findViewById(R.id.toggle)).setImageResource(R.drawable.quantum_ic_expand_more_grey600_24);

        this.secondView = inflater.inflate(R.layout.itinerary_summary, this, false);
        ((ImageView) this.secondView.findViewById(R.id.toggle)).setImageResource(R.drawable.quantum_ic_expand_less_grey600_24);

        //this.recyclerView = (RecyclerView) this.secondView.findViewById(R.id.itinerary_summary_recycler_view);

        addView(this.firstView, this.firstView.getLayoutParams());
        addView(this.secondView, this.secondView.getLayoutParams());

    }

   /* @Override
    public final void onClick(View view) {
        super.onClick(view);
        if ((this.secondView.getVisibility() == View.VISIBLE)) {
            this.recyclerView.setVisibility(View.VISIBLE);
        } else {
            this.recyclerView.setVisibility(View.GONE);
        }
    }*/
}
