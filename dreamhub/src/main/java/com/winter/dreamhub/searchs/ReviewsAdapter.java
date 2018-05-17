package com.winter.dreamhub.searchs;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.winter.dreamhub.R;
import com.winter.dreamhub.api.service.model.Review;
import com.winter.dreamhub.util.DateTimeUtils;
import com.winter.dreamhub.util.HtmlUtils;
import com.winter.dreamhub.util.ImeUtils;
import com.winter.dreamhub.util.ViewUtils;
import com.winter.dreamhub.widget.AuthorTextView;
import com.winter.dreamhub.widget.CheckableImageButton;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.winter.dreamhub.util.AnimUtils.getFastOutSlowInInterpolator;

/**
 * Created by hoaxoan on 2/23/2017.
 */

public class ReviewsAdapter extends ArrayAdapter<Review> {

    private final LayoutInflater inflater;
    private final Transition change;
    private int expandedCommentPosition = ListView.INVALID_POSITION;

    public ReviewsAdapter(Context context, int resource, List<Review> reviews) {
        super(context, resource, reviews);
        inflater = LayoutInflater.from(context);
        change = new AutoTransition();
        change.setDuration(200L);
        change.setInterpolator(getFastOutSlowInInterpolator(context));
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            view = newNewReviewView(position, container);
        }
        bindReview(getItem(position), position, view);
        return view;
    }

    private View newNewReviewView(int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.landmark_details_review_item, parent, false);
        view.setTag(R.id.player_avatar, view.findViewById(R.id.player_avatar));
        view.setTag(R.id.comment_author, view.findViewById(R.id.comment_author));
        view.setTag(R.id.comment_time_ago, view.findViewById(R.id.comment_time_ago));
        view.setTag(R.id.comment_text, view.findViewById(R.id.comment_text));
        view.setTag(R.id.comment_reply, view.findViewById(R.id.comment_reply));
        view.setTag(R.id.comment_like, view.findViewById(R.id.comment_like));
        view.setTag(R.id.comment_likes_count, view.findViewById(R.id.comment_likes_count));
        return view;
    }

    private void bindReview(final Review review, final int position, final View view) {
        final ImageView avatar = (ImageView) view.getTag(R.id.player_avatar);
        final AuthorTextView author = (AuthorTextView) view.getTag(R.id.comment_author);
        final TextView timeAgo = (TextView) view.getTag(R.id.comment_time_ago);
        final TextView commentBody = (TextView) view.getTag(R.id.comment_text);
        final ImageButton reply = (ImageButton) view.getTag(R.id.comment_reply);
        final CheckableImageButton likeHeart =
                (CheckableImageButton) view.getTag(R.id.comment_like);
        final TextView likesCount = (TextView) view.getTag(R.id.comment_likes_count);

        if (review.reviewerName != null) {
            author.setText(review.reviewerName);
        }
        //author.setOriginalPoster(isOP(review.user.id));
        if(review.created_at == null){
            timeAgo.setText("");
        } else {
            Date date = DateTimeUtils.parseDateFromStr(review.created_at);
            timeAgo.setText(date == null ? "" :
                    DateUtils.getRelativeTimeSpanString(DateTimeUtils.parseDateFromStr(review.created_at).getTime(),
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS)
                            .toString().toLowerCase());
        }

        HtmlUtils.setTextWithNiceLinks(commentBody, review.text);

        view.setActivated(position == expandedCommentPosition);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isExpanded = reply.getVisibility() == View.VISIBLE;
                //TransitionManager.beginDelayedTransition(fragment.commentsList, change);
                view.setActivated(!isExpanded);
                if (!isExpanded) { // do expand
                    expandedCommentPosition = position;

                    // work around issue where avatar of selected comment not shown during
                    // shared element transition (returning from player screen)
                    avatar.setOutlineProvider(null);
                    avatar.setElevation(getContext().getResources().getDimension(R.dimen.z_card));

                    reply.setVisibility(View.VISIBLE);
                    likeHeart.setVisibility(View.VISIBLE);
                    likesCount.setVisibility(View.VISIBLE);
                    /*if (comment.liked == null) {
                        final Call<Like> liked = dribbblePrefs.getApi()
                                .likedComment(shot.id, comment.id);
                        liked.enqueue(new Callback<Like>() {
                            @Override
                            public void onResponse(Call<Like> call, Response<Like> response) {
                                comment.liked = response.isSuccessful();
                                likeHeart.setChecked(comment.liked);
                                likeHeart.jumpDrawablesToCurrentState();
                            }

                            @Override public void onFailure(Call<Like> call, Throwable t) { }
                        });
                    }*/
                    /*if (fragment.enterComment != null && fragment.enterComment.hasFocus()) {
                        fragment.enterComment.clearFocus();
                        ImeUtils.hideIme(fragment.enterComment);
                    }*/
                    view.requestFocus();
                } else { // do collapse
                    expandedCommentPosition = ListView.INVALID_POSITION;
                    avatar.setOutlineProvider(ViewUtils.CIRCULAR_OUTLINE);
                    avatar.setElevation(0f);
                    reply.setVisibility(View.GONE);
                    likeHeart.setVisibility(View.GONE);
                    likesCount.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    /*private boolean isOP(long playerId) {
        return landmark.user != null && landmark.user.id == playerId;
    }*/
}
