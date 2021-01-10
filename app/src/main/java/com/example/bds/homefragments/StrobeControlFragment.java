package com.example.bds.homefragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bds.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StrobeControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StrobeControlFragment extends Fragment {

    public StrobeControlFragment() {
        // Required empty public constructor
    }

    public static StrobeControlFragment newInstance(String param1, String param2) {
        StrobeControlFragment fragment = new StrobeControlFragment();
        new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_strobe_control, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}