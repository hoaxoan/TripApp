package com.winter.dreamhub.auth;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.model.Auth;
import com.winter.dreamhub.api.service.model.User;
import com.winter.dreamhub.util.AnimUtils;
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

public class LoginFragment extends DialogFragment {

    private static final int PERMISSIONS_REQUEST_GET_ACCOUNTS = 0;

    boolean isDismissing = false;
    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.dialog_title)
    TextView title;
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
    @BindView(R.id.actions_container)
    FrameLayout actionsContainer;
    @BindView(R.id.signup)
    Button signup;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.loading)
    ProgressBar loading;
    private WinterPrefs winterPrefs;
    private boolean shouldPromptForPermission = false;


    /*
     * Create a new instance of SignOutDialogFragment
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sign_in, container, false);
        winterPrefs = WinterPrefs.get(getActivity());
        ButterKnife.bind(this, view);

       /* if (!FabTransform.setup(getActivity(), container)) {
            MorphTransform.setup(getActivity(), container,
                    ContextCompat.getColor(getActivity(), R.color.background_light),
                    getResources().getDimensionPixelSize(R.dimen.dialog_corners));
        }
        if (getActivity().getWindow().getSharedElementEnterTransition() != null) {
            getActivity().getWindow().getSharedElementEnterTransition().addListener(new AnimUtils
                    .TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    finishSetup();
                }
            });
        } else {
            finishSetup();
        }
*/
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(view);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin(view);
            }
        });

        return view;
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
                if (shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                    setupPermissionPrimer();
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

    public void signup(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.designernews.co/users/new")));
    }

    public void dismiss(View view) {
        isDismissing = true;
        //setResult(Activity.RESULT_CANCELED);
    }

    /**
     * Postpone some of the setup steps so that we can run it after the enter transition
     * (if there is one). Otherwise we may show the permissions dialog or account dropdown
     * during the enter animation which is jarring.
     */
    private void finishSetup() {
        if (shouldPromptForPermission) {
            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISSIONS_REQUEST_GET_ACCOUNTS);
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
        TransitionManager.beginDelayedTransition(container);
        title.setVisibility(View.GONE);
        usernameLabel.setVisibility(View.GONE);
        permissionPrimer.setVisibility(View.GONE);
        passwordLabel.setVisibility(View.GONE);
        actionsContainer.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void showLogin() {
        TransitionManager.beginDelayedTransition(container);
        title.setVisibility(View.VISIBLE);
        usernameLabel.setVisibility(View.VISIBLE);
        passwordLabel.setVisibility(View.VISIBLE);
        actionsContainer.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private void getAccessToken() {
        final Call<Auth> call = winterPrefs.getApi().login(username.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful()) {
                    Auth auth = response.body();
                    if(auth != null){
                        winterPrefs.setAccessToken(auth.jwt);
                        showLoggedInUser(auth.user);
                        //setResult(Activity.RESULT_OK);
                        //finish();
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

    private void showLoginFailed() {
        Snackbar.make(container, "Log in failed", Snackbar.LENGTH_SHORT).show();
        showLogin();
        password.requestFocus();
    }

    private void showLoggedInUser(User user) {
        winterPrefs.setLoggedInUser(user);

        final Toast confirmLogin = new Toast(getActivity());
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout
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

    private void setupAccountAutocomplete() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.GET_ACCOUNTS) ==
                PackageManager.PERMISSION_GRANTED) {
            permissionPrimer.setVisibility(View.GONE);
            final Account[] accounts = AccountManager.get(getActivity()).getAccounts();
            final Set<String> emailSet = new HashSet<>();
            for (Account account : accounts) {
                if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                    emailSet.add(account.name);
                }
            }
            username.setAdapter(new ArrayAdapter<>(getActivity(),
                    R.layout.account_dropdown_item, new ArrayList<>(emailSet)));
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                setupPermissionPrimer();
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
                    requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                            PERMISSIONS_REQUEST_GET_ACCOUNTS);
                }
            }
        });
    }
}
