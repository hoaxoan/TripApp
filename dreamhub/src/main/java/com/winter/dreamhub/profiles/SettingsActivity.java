package com.winter.dreamhub.profiles;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.HomeActivity;
import com.winter.dreamhub.util.Utils;

import java.util.List;

/**
 * Created by hoaxoan on 1/15/2017.
 */

public class SettingsActivity extends PreferenceActivity {

    private TextView mTitleText;
    private View mUpArrow;

    private static final int CHECK_ACCOUNTS_DELAY = 3000;
    private Account[] mAccounts;
    Runnable mCheckAccounts = new Runnable() {
        @Override
        public void run() {
            if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Account[] accounts = AccountManager.get(SettingsActivity.this).getAccounts();
            if (accounts != null && !accounts.equals(mAccounts)) {
                invalidateHeaders();
            }
        }
    };
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* LinearLayout rootView = (LinearLayout) getLayoutInflater().inflate(R.layout.action_bar_custom_view_preferences, null);
        this.mTitleText = ((TextView) rootView.findViewById(R.id.title_text));
        this.mUpArrow = rootView.findViewById(R.id.up_arrow);
        this.mUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
        setHeaderTitle(getString(R.string.preferences_title));
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(rootView);
        actionBar.setBackgroundDrawable(((Resources) getResources()).getDrawable(R.drawable.actionbar_background));*/


        LinearLayout rootView = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        LinearLayout view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.action_bar_custom_view_preferences2, rootView, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        rootView.addView(view, 0); // insert at top
        toolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        toolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        toolbar.setTitle(getResources().getString(R.string.preferences_title));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                //onBackPressed();
            }
        });
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new AppCompatEditText(this, attrs);
                case "Spinner":
                    return new AppCompatSpinner(this, attrs);
                case "CheckBox":
                    return new AppCompatCheckBox(this, attrs);
                case "RadioButton":
                    return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView":
                    return new AppCompatCheckedTextView(this, attrs);
            }
        }
        return null;
    }


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.calendar_settings_headers, target);
    }

    @Override
    public void onResume() {
        if (mHandler != null) {
            mHandler.postDelayed(mCheckAccounts, CHECK_ACCOUNTS_DELAY);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mCheckAccounts);
        }
        super.onPause();
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return false;
    }

    private void setHeaderTitle(CharSequence text) {
        if ((!Utils.isLOrLater()) && (this.mTitleText != null)) {
            this.mTitleText.setText(text);
            this.mUpArrow.setContentDescription(getString(R.string.preferences_back_description, new Object[]{text}));
        }
    }
}
