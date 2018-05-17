package com.winter.dreamhub.reviews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.prefs.WinterPrefs;
import com.winter.dreamhub.api.service.model.User;
import com.winter.dreamhub.api.service.request.ReviewRequest;
import com.winter.dreamhub.api.service.response.ReviewResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 10/14/2017.
 */

public class SubmitReviewFragment extends Fragment {
    public static final String TAG = makeLogTag(SubmitReviewFragment.class);
    public static final int SAVE_REVIEW = 1;
    public static final int DELETE_REVIEW = 2;
    public static final int CANCEL_REVIEW = 3;

    private View rootView;

    private ImageView mAuthorImage;
    private TextView mAuthorName;
    private RatingBar mRatingBar;
    private EditText mReviewResponseText;

    private long id;
    private String userName;
    private float rating;

    private WinterPrefs winterPrefs;

    private ReviewCallback mCallback;

    /**
     * Create a new instance of the fragment
     */
    public static SubmitReviewFragment newInstance(ReviewCallback callback, long id, String userName, float rating) {
        SubmitReviewFragment fragment = new SubmitReviewFragment();
        fragment.mCallback = callback;
        fragment.id = id;
        fragment.userName = userName;
        fragment.rating = rating;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        winterPrefs = WinterPrefs.get(getActivity());

        mAuthorImage = (ImageView) this.rootView.findViewById(R.id.author_image);
        mAuthorName = (TextView) this.rootView.findViewById(R.id.authorname_textbox);
        mRatingBar = (RatingBar) this.rootView.findViewById(R.id.review_textbox);
        mReviewResponseText = (EditText) this.rootView.findViewById(R.id.ownerresponse_textbox);

        mAuthorName.setText(this.userName);
        mRatingBar.setRating(this.rating);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.review_submit_fragment, container, false);
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

    public void postReview() {
        String reviewText = mReviewResponseText.getText().toString();
        float rating = mRatingBar.getRating();
        if (TextUtils.isEmpty(reviewText)) {
            return;
        }

        //ReviewRequest request = ReviewRequest.createWithReview(rating, reviewText);
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.sending_label), true);
        progressDialog.show();
        User user = WinterPrefs.get(getActivity()).getUser();
        ReviewRequest request = null;
        if (user != null) {
            request = new ReviewRequest(reviewText, reviewText, rating, user.username, id, user.id);
        } else {
            request = new ReviewRequest(reviewText, reviewText, rating, null, id, 0);
        }

        if (request == null) {
            progressDialog.dismiss();
            Toast.makeText(getContext(),
                    "Failed to post review :(", Toast.LENGTH_SHORT).show();
        }

        final Call<ReviewResponse> postReviewCall = winterPrefs.getApi().postReview(request);
        postReviewCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                ReviewResponse review = response.body();
                if (mCallback == null || review == null) {
                    return;
                }
                mCallback.onPostReviewSuccess(review);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),
                        "Failed to post review :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface ReviewCallback {
        void onPostReviewSuccess(ReviewResponse response);
    }
}
