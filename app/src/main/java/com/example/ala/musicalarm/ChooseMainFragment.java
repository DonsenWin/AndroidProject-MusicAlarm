package com.example.ala.musicalarm;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;

public class ChooseMainFragment extends Fragment {

    MainFragmentInterface mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

              final ScrollView scrollView =
                (ScrollView) inflater.inflate(R.layout.fragment_choose_main, parent, false);

        if (mCallback.isPlayingNow()) {
            ImageButton imageButton = (ImageButton) scrollView.findViewById(R.id.playButton);
            imageButton.setImageResource(R.drawable.pause_selector);
        } else {
            ImageButton imageButton = (ImageButton) scrollView.findViewById(R.id.playButton);
            imageButton.setImageResource(R.drawable.play_selector);
        }

        if (mCallback.isRandomized()) {
            ImageButton imageButton = (ImageButton) scrollView.findViewById(R.id.shuffleButton);
            imageButton.setImageResource(R.drawable.random_selector);
        } else {
            ImageButton imageButton = (ImageButton) scrollView.findViewById(R.id.shuffleButton);
            imageButton.setImageResource(R.drawable.normal_selector);
        }

        return scrollView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       setRetainInstance(true);
    }


    //activity communication
    public interface MainFragmentInterface{

        boolean isPlayingNow();
        boolean isRandomized();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (MainFragmentInterface) activity;
        }catch (ClassCastException ex){
            Log.e("MainFragment", "must implement MainFragmentInterface", ex );
        }

    }

}