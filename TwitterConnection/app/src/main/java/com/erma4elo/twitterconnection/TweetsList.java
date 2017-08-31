package com.erma4elo.twitterconnection;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresPermission;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TweetsList extends ListActivity {

    private TweetTimelineListAdapter adapter;
    private UserTimeline userTimeline;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_list);

        String name = getIntent().getStringExtra("userName");

        Button buttonAddNewTweet = (Button) findViewById(R.id.addNewTweet);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        userTimeline = new UserTimeline.Builder()
                .screenName(name)
                .build();
        adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
        updateListAdapter();

        // обновляю tweets свайпом

        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(true);
            adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                @Override
                public void success(Result<TimelineResult<Tweet>> result) {
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void failure(TwitterException exception) {
                }
            });
        });

        //что бы создать новый tweet, twitter предоставляет свою активность

        buttonAddNewTweet.setOnClickListener(v -> {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            final Intent intent = new ComposerActivity.Builder(TweetsList.this)
                    .session(session)
                    .createIntent();
            startActivity(intent);
        });

    }

    //Метод для обновления списка(Список обновляется каждую минуту)

    private void updateListAdapter() {
        new Handler().postDelayed(() -> {

            //пришлось продублировать этот маленький кусок кода что бы адаптер обновлялся
            adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                @Override
                public void success(Result<TimelineResult<Tweet>> result) {
                    updateListAdapter();
                }

                @Override
                public void failure(TwitterException exception) {

                }
            });

        }, 1000 * 60);

    }

}
