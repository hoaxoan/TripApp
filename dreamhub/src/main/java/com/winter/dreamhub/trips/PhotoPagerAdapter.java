package com.winter.dreamhub.trips;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.WinterService;
import com.winter.dreamhub.api.service.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 11/8/2017.
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private Context context;
    private final LayoutInflater layoutInflater;
    private View rootView;
    private ImageView imageView;
    private TextView attributionText;
    private View overlay;
    private String name;
    private List<Image> images;

    public PhotoPagerAdapter(Context context, String name, List<Image> images) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.name = name;
        if (images != null) {
            this.images = images;
        } else {
            this.images = new ArrayList<Image>();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View layout = getPage(position, collection);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private View getPage(int position, ViewGroup parent) {
        this.rootView = layoutInflater.inflate(R.layout.fixed_aspect_ratio_illustration, parent, false);
        this.imageView = ((ImageView) rootView.findViewById(R.id.image));
        this.overlay = rootView.findViewById(R.id.overlay);
        this.attributionText = (TextView) rootView.findViewById(R.id.photo_attribution_text);

        Image image = images.get(position);
        String imgUrl = WinterService.ENDPOINT + image.url;
        Glide.with(this.context).load(imgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this.imageView);
        this.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("name", name);
                intent.putParcelableArrayListExtra("images", new ArrayList<>(images));
                context.startActivity(intent);
            }
        });

        return this.rootView;
    }
}