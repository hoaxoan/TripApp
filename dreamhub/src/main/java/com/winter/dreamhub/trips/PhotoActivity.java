package com.winter.dreamhub.trips;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.api.service.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 11/8/2016.
 */

public class PhotoActivity extends BaseActivity {

    private Toolbar mActionBarToolbar;

    private List<Image> images = new ArrayList<>();
    private ProgressDialog pDialog;
    private PhotoCarouselAdapter mAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_carousel);

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        String name = getIntent().getStringExtra("name");
        mActionBarToolbar.setTitle(name);
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.images = getIntent().getParcelableArrayListExtra("images");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pDialog = new ProgressDialog(this);
        mAdapter = new PhotoCarouselAdapter(this, images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new PhotoCarouselAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new PhotoCarouselAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", new ArrayList<Image>(images));
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                PhotoCarouselFragment newFragment = PhotoCarouselFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

}
