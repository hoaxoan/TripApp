package com.winter.dreamhub.auth;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.BaseActivity;
import com.winter.dreamhub.activity.HomeActivity;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.model.Auth;
import com.winter.dreamhub.api.service.model.User;
import com.winter.dreamhub.util.AnimUtils;
import com.winter.dreamhub.util.Utils;
import com.winter.dreamhub.widget.transitions.FabTransform;
import com.winter.dreamhub.widget.transitions.MorphTransform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 12/3/2016.
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = makeLogTag(LoginActivity.class);

    private static final int PERMISSIONS_REQUEST_GET_ACCOUNTS = 0;

    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.content)
    ViewGroup content;
    @BindView(R.id.username_float_label)
    TextInputLayout usernameLabel;
    @BindView(R.id.username)
    AutoCompleteTextView username;
    @BindView(R.id.permission_primer)
    CheckBox permissionPrimer;
    @BindView(R.id.password_float_label)
    TextInputLayout passwordLabel;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.link_register)
    TextView linkRegister;

    private WinterPrefs winterPrefs;
    private boolean shouldPromptForPermission = false;
    boolean isDismissing = false;
    private Toolbar mActionBarToolbar;

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_SIGN_IN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        winterPrefs = WinterPrefs.get(this);

        mActionBarToolbar = getActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_grey600_24);
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.quantum_black_secondary_text));
        mActionBarToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setTitle(this.getResources().getString(R.string.register_button_label));

        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!FabTransform.setup(this, container)) {
            MorphTransform.setup(this, container,
                    ContextCompat.getColor(this, R.color.background_light),
                    getResources().getDimensionPixelSize(R.dimen.dialog_corners));
        }
        if (Utils.isLOrLater()) {
            if (getWindow().getSharedElementEnterTransition() != null) {
                getWindow().getSharedElementEnterTransition().addListener(new AnimUtils
                        .TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        finishSetup();
                    }
                });
            } else {
                finishSetup();
            }
        } else {
            finishSetup();
        }

        loading.setVisibility(View.GONE);
        setupAccountAutocomplete();
        username.addTextChangedListener(loginFieldWatcher);
        // the primer checkbox messes with focus order so force it
        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    password.requestFocus();
                    return true;
                }
                return false;
            }
        });
        password.addTextChangedListener(loginFieldWatcher);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && isLoginValid()) {
                    login.performClick();
                    return true;
                }
                return false;
            }
        });

        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
    }

    private final void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public final void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_GET_ACCOUNTS) {
            TransitionManager.beginDelayedTransition(container);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupAccountAutocomplete();
                username.requestFocus();
                username.showDropDown();
            } else {
                // if permission was denied check if we should ask again in the future (i.e. they
                // did not check 'never ask again')
                if (Utils.isMOrLater()) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                        setupPermissionPrimer();
                    } else {
                        // denied & shouldn't ask again. deal with it (•_•) ( •_•)>⌐■-■ (⌐■_■)
                        TransitionManager.beginDelayedTransition(container);
                        permissionPrimer.setVisibility(View.GONE);
                    }
                } else {
                    // denied & shouldn't ask again. deal with it (•_•) ( •_•)>⌐■-■ (⌐■_■)
                    TransitionManager.beginDelayedTransition(container);
                    permissionPrimer.setVisibility(View.GONE);
                }
            }
        }
    }

    public void doLogin(View view) {
        showLoading();
        getAccessToken();
    }

    private void getAccessToken() {
        final Call<Auth> call = winterPrefs.getApi().login(username.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful()) {
                    Auth auth = response.body();
                    if(auth != null){
                        setUser(auth.user);
                        winterPrefs.setAccessToken(auth.jwt);
                        showLoggedInUser(user);
                        setResult(Activity.RESULT_OK);
                        startHomeActivity();
                    }
                } else {
                    showLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Log.e(getClass().getCanonicalName(), t.getMessage(), t);
                showLoginFailed();
            }
        });
    }

    private void showLoggedInUser(User user) {
        winterPrefs.setLoggedInUser(user);

        final Toast confirmLogin = new Toast(getApplicationContext());
        final View v = LayoutInflater.from(LoginActivity.this).inflate(R.layout
                .toast_logged_in_confirmation, null, false);
        ((TextView) v.findViewById(R.id.name)).setText(user.username);
        // need to use app context here as the activity will be destroyed shortly
//        Glide.with(getApplicationContext())
//                .load(user.portrait_url)
//                .placeholder(R.drawable.avatar_placeholder)
//                .transform(new CircleTransform(getApplicationContext()))
//                .into((ImageView) v.findViewById(R.id.avatar));
//        v.findViewById(R.id.scrim).setBackground(ScrimUtil
//                .makeCubicGradientScrimDrawable(
//                        ContextCompat.getColor(LoginActivity.this, R.color.scrim),
//                        5, Gravity.BOTTOM));
        confirmLogin.setView(v);
        confirmLogin.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        confirmLogin.setDuration(Toast.LENGTH_LONG);
        confirmLogin.show();
        showLogin();
    }

    private void showLoginFailed() {
        Snackbar.make(container, "Log in failed", Snackbar.LENGTH_SHORT).show();
        showLogin();
        password.requestFocus();
    }

    /**
     * Postpone some of the setup steps so that we can run it after the enter transition
     * (if there is one). Otherwise we may show the permissions dialog or account dropdown
     * during the enter animation which is jarring.
     */
    private void finishSetup() {
        if (Utils.isMOrLater()) {
            if (shouldPromptForPermission) {
                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                        PERMISSIONS_REQUEST_GET_ACCOUNTS);
                shouldPromptForPermission = false;
            }
        } else {
            shouldPromptForPermission = false;
        }
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                maybeShowAccounts();
            }
        });
        maybeShowAccounts();
    }

    private TextWatcher loginFieldWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            login.setEnabled(isLoginValid());
        }
    };

    private void maybeShowAccounts() {
        if (username.hasFocus()
                && username.isAttachedToWindow()
                && username.getAdapter() != null
                && username.getAdapter().getCount() > 0) {
            username.showDropDown();
        }
    }

    private boolean isLoginValid() {
        return username.length() > 0 && password.length() > 0;
    }

    private void showLoading() {
        //TransitionManager.beginDelayedTransition(container);
        content.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void showLogin() {
        //TransitionManager.beginDelayedTransition(container);
        content.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }


    private void setupAccountAutocomplete() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) ==
                PackageManager.PERMISSION_GRANTED) {
            permissionPrimer.setVisibility(View.GONE);
            final Account[] accounts = AccountManager.get(this).getAccounts();
            final Set<String> emailSet = new HashSet<>();
            for (Account account : accounts) {
                if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                    emailSet.add(account.name);
                }
            }
            username.setAdapter(new ArrayAdapter<>(this,
                    R.layout.account_dropdown_item, new ArrayList<>(emailSet)));
        } else {
            if (Utils.isMOrLater()) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                    setupPermissionPrimer();
                } else {
                    permissionPrimer.setVisibility(View.GONE);
                    shouldPromptForPermission = true;
                }
            } else {
                permissionPrimer.setVisibility(View.GONE);
                shouldPromptForPermission = true;
            }
        }
    }

    private void setupPermissionPrimer() {
        permissionPrimer.setChecked(false);
        permissionPrimer.setVisibility(View.VISIBLE);
        permissionPrimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Utils.isMOrLater()) {
                        requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                                PERMISSIONS_REQUEST_GET_ACCOUNTS);
                    }
                }
            }
        });
    }
}
