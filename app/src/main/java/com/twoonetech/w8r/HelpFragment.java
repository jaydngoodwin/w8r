package com.twoonetech.w8r;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HelpFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(view1 -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            RobotsFragment robotsFragment = new RobotsFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container,robotsFragment).commit();
        });
    }

}
