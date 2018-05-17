package com.winter.dreamhub.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.winter.dreamhub.R;

/**
 * Created by hoaxoan on 11/6/2016.
 */

public class TntAccordion extends ToggleFrameLayout {
    public OnExpandListener listener;

    public TntAccordion(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        this.firstView = inflater.inflate(R.layout.tnt_accordion_card, this, false);
        ((ImageView) this.firstView.findViewById(R.id.accordion_status_image)).setImageResource(R.drawable.quantum_ic_expand_more_grey600_24);
        this.firstView.setPadding(getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_16), getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_20), getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_16), getResources().getDimensionPixelSize(R.dimen.tnt_item_padding_20));
        this.secondView = inflater.inflate(R.layout.tnt_accordion_card, this, false);
        ((ImageView) this.secondView.findViewById(R.id.accordion_status_image)).setImageResource(R.drawable.quantum_ic_expand_less_grey600_24);
        addView(this.firstView, this.firstView.getLayoutParams());
        addView(this.secondView, this.secondView.getLayoutParams());
    }

    @Override
    public final void onClick(View view) {
        super.onClick(view);
        if ((this.secondView.getVisibility() == View.VISIBLE) && (this.listener != null)) {
            this.listener.onExpand(this);
        }
    }

    public interface OnExpandListener {
        void onExpand(TntAccordion tntAccordion);
    }
}
