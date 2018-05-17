package com.winter.dreamhub.profiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.Profile;
import com.winter.dreamhub.auth.LoginActivity;
import com.winter.dreamhub.auth.RegisterActivity;
import com.winter.dreamhub.util.DrawableUtils;

import java.util.List;

import static com.winter.dreamhub.profiles.ProfilesFragment.ID_ITEM_ABOUT;
import static com.winter.dreamhub.profiles.ProfilesFragment.ID_ITEM_SETTING;
import static com.winter.dreamhub.profiles.ProfilesFragment.ID_ITEM_SIGN_IN;
import static com.winter.dreamhub.profiles.ProfilesFragment.ID_ITEM_SIGN_UP;

/**
 * Created by hoaxoan on 11/8/2016.
 */

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ViewHolder> {

    private List<Profile> profiles;
    private Context mContext;

    public ProfilesAdapter(Context context, List<Profile> profiles) {
        mContext = context;
        this.profiles = profiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profiles_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        int itemId = (int) profile.position;
        if (itemId < 0 || itemId >= ICON_RES_ID.length) {
            itemId = 0;
        }
        final Drawable drawable = DrawableUtils
                .withContext(mContext)
                .withColor(R.color.indigo)
                .withDrawable(ICON_RES_ID[itemId])
                .tint()
                .get();

        holder.image.setImageDrawable(drawable);
        holder.title.setText(profile.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(profile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void startActivity(Profile profile) {
        Intent intent = null;
        if (profile.id == ID_ITEM_SIGN_IN) {
            intent = new Intent(mContext, LoginActivity.class);
        } else if (profile.id == ID_ITEM_SIGN_UP) {
            intent = new Intent(mContext, RegisterActivity.class);
        } else if (profile.id == ID_ITEM_SETTING) {
            intent = new Intent(mContext, SettingsActivity.class);
        } else if (profile.id == ID_ITEM_ABOUT) {
            intent = new Intent(mContext, AboutActivity.class);
        }

        if (intent == null) {
            return;
        }
        mContext.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public static final int[] ICON_RES_ID = new int[]{
            R.drawable.ic_account_circle_white_24dp,
            R.drawable.ic_account_circle_white_24dp,
            R.drawable.ic_round_settings_white_48dp,
            R.drawable.ic_round_help_white_48dp,
    };
}