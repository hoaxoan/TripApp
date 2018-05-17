package com.winter.dreamhub.profiles;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.model.Profile;
import com.winter.dreamhub.auth.LoginActivity;
import com.winter.dreamhub.auth.SignOutDialogFragment;
import com.winter.dreamhub.util.DrawableUtils;

import java.util.ArrayList;
import java.util.List;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

public class ProfilesFragment extends Fragment {

    public static final String TAG = makeLogTag(ProfilesFragment.class);
    public static final int ID_ITEM_SIGN_IN = 1;
    public static final int ID_ITEM_SIGN_UP = 2;
    public static final int ID_ITEM_SETTING = 3;
    public static final int ID_ITEM_ABOUT = 4;

    private List<Profile> profiles = new ArrayList<Profile>();

    private View rootView;
    public Toolbar mActionBarToolbar;
    private ProfilesAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private WinterPrefs winterPrefs;

    /**
     * Create a new instance of the fragment
     */
    public static ProfilesFragment newInstance() {
        ProfilesFragment fragment = new ProfilesFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winterPrefs = WinterPrefs.get(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionBarToolbar = (Toolbar) this.rootView.findViewById(R.id.toolbar_actionbar);
        final Drawable drawable = DrawableUtils
                .withContext(getActivity())
                .withColor(R.color.indigo)
                .withDrawable(R.drawable.ic_account_circle)
                .tint()
                .get();

        mActionBarToolbar.setNavigationIcon(drawable);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        mActionBarToolbar.setTitle(getString(R.string.tab_profile));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutDialog();
            }
        });

        this.mRecyclerView = ((RecyclerView) this.rootView.findViewById(R.id.recycler_view));

        if (!winterPrefs.isLoggedIn()) {
            profiles.add(new Profile(ID_ITEM_SIGN_IN, "Sign In", 1, ""));
            profiles.add(new Profile(ID_ITEM_SIGN_UP, "Sign Up", 2, ""));
        }

        profiles.add(new Profile(ID_ITEM_SETTING, "Setting", 3, ""));
        profiles.add(new Profile(ID_ITEM_ABOUT, "About", 4, ""));

        this.mAdapter = new ProfilesAdapter(getActivity(), profiles);
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.profile_fragment, container, false);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void showSignOutDialog() {
        if (winterPrefs.isLoggedIn()) {
            SignOutDialogFragment newFragment = SignOutDialogFragment.newInstance();
            newFragment.show(getFragmentManager(), "dialog");
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);

        }
    }

}
