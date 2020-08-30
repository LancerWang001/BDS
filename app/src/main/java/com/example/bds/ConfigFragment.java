package com.example.bds;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.events.ChangeCmntWayEvent;
import com.example.events.configparams.SendConfigParamsEvent;
import com.example.events.selfcheck.SendSelfControlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText accelerationText;
    private EditText waterTimeText;
    private EditText attitudDeterminationTimeText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment configFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button paramBtn = (Button) getActivity().findViewById(R.id.menu_item_config_params);
        Button cmntBtn = (Button) getActivity().findViewById(R.id.menu_item_config_cmntway);
        Button strobeBtn = (Button) getActivity().findViewById(R.id.menu_item_config_flash);

        bindTransaction(paramBtn, new Listener(new ParamsFragment()));
        bindTransaction(cmntBtn, new Listener(new CmntWayFragment()));
        bindTransaction(strobeBtn, new Listener(new StrobeControlFragment()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config, container, false);
    }
    private class Listener implements View.OnClickListener {
        Fragment targetFragment;

        public Listener(Fragment targetFragment) {
            this.targetFragment = targetFragment;
        }

        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, targetFragment)
                    .commit();
        }
    }
    private void bindTransaction(View bindView, Listener listener) {
        if (null != bindView && null != listener)
            bindView.setOnClickListener(listener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String mess) {

    }
}