package com.winter.dreamhub.trips;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.winter.dreamhub.R;
import com.winter.dreamhub.util.DisplayUtils;
import com.winter.dreamhub.util.ImageUtils;
import com.winter.dreamhub.widget.ObservableScrollView;

/**
 * Created by hoaxoan on 10/27/2016.
 */

public class ScrollableFragment extends Fragment {
    ImageUtils imageUtils;
    LinearLayout itemsContainer;
    ObservableScrollView rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.imageUtils = new ImageUtils(getActivity(), new DisplayUtils(getActivity().getWindowManager()));
        this.rootView = ((ObservableScrollView) inflater.inflate(R.layout.scrollable_fragment, container, false));
        this.itemsContainer = ((LinearLayout) this.rootView.findViewById(R.id.items_container));
        //this.rootView.setPadding(0, this.imageUtils.getHeaderHeight(), 0, 0);
        return this.rootView;
    }
}
