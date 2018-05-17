package com.winter.dreamhub.auth;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;

public class SignOutDialogFragment extends DialogFragment {

    private Button mNotNow;
    private Button mSignOut;

    private WinterPrefs winterPrefs;

    /*
     * Create a new instance of SignOutDialogFragment
     */
    public static SignOutDialogFragment newInstance() {
        SignOutDialogFragment fragment = new SignOutDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winterPrefs = WinterPrefs.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sign_out, container, false);
        this.mNotNow = (Button) view.findViewById(R.id.not_now);
        this.mSignOut = (Button) view.findViewById(R.id.sign_out);

        this.mNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        this.mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winterPrefs.logOut();
                dismiss();
            }
        });


        return view;
    }

}
