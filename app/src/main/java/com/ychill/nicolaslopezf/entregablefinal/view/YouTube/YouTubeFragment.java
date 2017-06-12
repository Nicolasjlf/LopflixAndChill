package com.ychill.nicolaslopezf.entregablefinal.view.YouTube;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ychill.nicolaslopezf.entregablefinal.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by santiagoiraola on 11/13/16.
 */

public class YouTubeFragment extends Fragment {

    private static final String API_KEY = "AIzaSyCEdOZ1_jseuld1zJZhkiAK5rDF87VVHfU";
    private static final Integer RECOVERY_REQUEST = 1;
    private String url;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    YouTubePlayerSupportFragment youTubePlayerSupportFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewDevuelta = inflater.inflate(R.layout.fragment_youtube_player,container,false);

        youTubePlayerSupportFragment = YouTubePlayerSupportFragment.newInstance();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.youtubeFragment_youtube,youTubePlayerSupportFragment);
        fragmentTransaction.commit();

        Bundle unBundle = getArguments();
        try{
            url = unBundle.getString("url");
        }
        catch (Exception e){
            Log.d("url",e.getStackTrace().toString());
        }




        youTubePlayerSupportFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

                if(!wasRestored){


                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.loadVideo(url);
                    youTubePlayer.cueVideo(url);
                    youTubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {

                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("youtubeError",errorMessage);
            }
        });



        return viewDevuelta;
    }
}
