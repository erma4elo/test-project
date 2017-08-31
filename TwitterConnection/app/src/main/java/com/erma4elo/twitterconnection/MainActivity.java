package com.erma4elo.twitterconnection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TwitterCollection;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private TwitterLoginButton loginTwitterButton;

    // все что я здесь использовал я взял с официальной документации на сайте разработчиков twitter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("7onjbfSsG19s8QtbPwvbduYbe", "AccrUCxOOxCc1D4Fa5HxJbKVLOgKDUyEEM0JUahCxBtA5jXoks"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_main);


        loginTwitterButton = (TwitterLoginButton) findViewById(R.id.loginButton);
        loginTwitterButton.setCallback(new Callback<TwitterSession>() {

            //если пользователь успешно авторизован он попадает в этот кусок кода

            @Override
            public void success(Result<TwitterSession> result) {

                Intent intent = new Intent(MainActivity.this, TweetsList.class);
                intent.putExtra("userName", result.data.getUserName());
                intent.putExtra("userId", result.data.getUserId());
                startActivity(intent);
                finish();

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginTwitterButton.onActivityResult(requestCode, resultCode, data);

    }

}
