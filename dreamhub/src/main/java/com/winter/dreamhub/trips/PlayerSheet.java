package com.winter.dreamhub.trips;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.PaginatedDataManager;
import com.winter.dreamhub.api.service.model.Landmark;
import com.winter.dreamhub.api.service.model.User;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoaxoan on 2/25/2017.
 */

public class PlayerSheet extends Activity {

    private static final int MODE_SHOT_LIKES = 1;
    private static final int MODE_FOLLOWERS = 2;
    private static final int DISMISS_DOWN = 0;
    private static final int DISMISS_CLOSE = 1;
    private static final String EXTRA_MODE = "EXTRA_MODE";
    private static final String EXTRA_SHOT = "EXTRA_SHOT";
    private static final String EXTRA_USER = "EXTRA_USER";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MODE_SHOT_LIKES,
            MODE_FOLLOWERS
    })
    @interface PlayerSheetMode { }

    /*@BindView(R.id.bottom_sheet) BottomSheet bottomSheet;
    @BindView(R.id.bottom_sheet_content)
    ViewGroup content;
    @BindView(R.id.title_bar) ViewGroup titleBar;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.player_list)
    RecyclerView playerList;
    @BindDimen(R.dimen.large_avatar_size) int largeAvatarSize;
    private @Nullable
    Landmark shot;
    private @Nullable
    User player;
    private PaginatedDataManager dataManager;
    private LinearLayoutManager layoutManager;
    private PlayerAdapter adapter;
    private int dismissState = DISMISS_DOWN;*/

    public static void start(Activity launching, Landmark shot) {
        Intent starter = new Intent(launching, PlayerSheet.class);
        starter.putExtra(EXTRA_MODE, MODE_SHOT_LIKES);
        starter.putExtra(EXTRA_SHOT, shot);
        launching.startActivity(starter,
                ActivityOptions.makeSceneTransitionAnimation(launching).toBundle());
    }

    public static void start(Activity launching, User player) {
        Intent starter = new Intent(launching, PlayerSheet.class);
        starter.putExtra(EXTRA_MODE, MODE_FOLLOWERS);
        starter.putExtra(EXTRA_USER, player);
        launching.startActivity(starter,
                ActivityOptions.makeSceneTransitionAnimation(launching).toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.player_sheet);*/
        ButterKnife.bind(this);
    }
}
