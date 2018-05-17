package com.winter.dreamhub.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 6/11/2016.
 */
public class OnboarFragment extends Fragment {

    private int[] mLayouts = new int[]{
            R.layout.oobe_screen_3,
            R.layout.oobe_screen_4
    };
    private LinearLayout mClusterList;
    private FrameLayout mScreenContainer;
    private FrameLayout mFrameLayout;
    private LinearLayout mExplanationContainer;
    private ImageView mIcon;
    private TextView mText;

    public static OnboarFragment newInstance(int position) {
        OnboarFragment fragment = new OnboarFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int position = getArguments().getInt("position", -1);
        if(position == 2){
            View view = inflater.inflate(R.layout.onboarding_bundle_configurator, container, false);
            mClusterList = (LinearLayout) view.findViewById(R.id.bundle_configurator_cluster_list);

            View row1 = inflater.inflate(R.layout.onboarding_bundle_configurator_cluster_row, container, false);
            mClusterList.addView(row1);
            View row2 = inflater.inflate(R.layout.onboarding_bundle_configurator_cluster_row, container, false);
            mClusterList.addView(row2);
            return view;
        } else{
            View view = inflater.inflate(R.layout.onboarding_screen, container, false);
            mScreenContainer = (FrameLayout) view.findViewById(R.id.onboarding_screen_device_render_container);
            mFrameLayout = (FrameLayout) view.findViewById(R.id.onboarding_screen_device_screen_container);
            mExplanationContainer = (LinearLayout) view.findViewById(R.id.onboarding_screen_explanation_container);
            mIcon = (ImageView) view.findViewById(R.id.onboarding_screen_icons);
            mText = (TextView) view.findViewById(R.id.onboarding_screen_text);

            if (position >= 0 && position < mLayouts.length) {
                View screen = inflater.inflate(mLayouts[position], container, false);
                mFrameLayout.addView(screen);
            }

            switch (position) {
                case 0:
                    mExplanationContainer.setBackgroundColor(getResources().getColor(R.color.onboarding_screen_reminders_explanation_background));
                    mIcon.setImageResource(R.drawable.ic_group_white_48dp);
                    mText.setText(getString(R.string.onboarding_screen_reminders_snippet));
                    break;
                case 1:
                    mExplanationContainer.setBackgroundColor(getResources().getColor(R.color.onboarding_screen_smartmail_explanation_background));
                    mIcon.setImageResource(R.drawable.ic_balance_white_48dp);
                    mText.setText(getString(R.string.onboarding_screen_smartmail_snippet_subsequent_times));
                    break;
            }
            return view;
        }

    }
}
