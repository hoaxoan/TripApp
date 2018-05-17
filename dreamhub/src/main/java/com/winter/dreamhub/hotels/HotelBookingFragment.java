package com.winter.dreamhub.hotels;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.HotelBooking;

import java.util.ArrayList;
import java.util.List;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 10/14/2017.
 */

public class HotelBookingFragment extends Fragment {
    public static final String TAG = makeLogTag(HotelBookingFragment.class);
    private View rootView;

    private List<HotelBooking> hotelBookings = new ArrayList<HotelBooking>();

    private HotelBookingsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    /**
     * Create a new instance of the fragment
     */
    public static HotelBookingFragment newInstance(List<HotelBooking> hotelBookings) {
        HotelBookingFragment fragment = new HotelBookingFragment();
        fragment.hotelBookings = hotelBookings;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mRecyclerView = ((RecyclerView) this.rootView.findViewById(R.id.recycler_view));

        this.mAdapter = new HotelBookingsAdapter(getActivity(), hotelBookings);
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.amenities_fragment, container, false);
        return this.rootView;
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

}
