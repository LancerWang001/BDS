package com.example.bds.homefragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bds.R;
import com.example.events.MessageEvent;
import com.example.events.configparams.SendConfigParamsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParamsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText accelerationText;
    private EditText waterTimeText;
    private EditText attitudDeterminationTimeText;

    public ParamsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParamsFragment newInstance(String param1, String param2) {
        ParamsFragment fragment = new ParamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_params, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button paramSetBtn = (Button) getActivity().findViewById(R.id.param_set);
        if(null != paramSetBtn) Log.d("paramSetBtn:", "initial success!");
        else Log.d("paramSetBtn:", "initial fail!");

        paramSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accelerationText = (EditText) getActivity().findViewById(R.id.accespeedRange);
                String a = String.valueOf(accelerationText.getText());
                Log.d("a",a);
                waterTimeText = (EditText) getActivity().findViewById(R.id.outWaterTime);
                String b = String.valueOf(waterTimeText.getText());
                attitudDeterminationTimeText = (EditText) getActivity().findViewById(R.id.timeInterVal);
                String c = String.valueOf(attitudDeterminationTimeText.getText());
                Log.d("========参数设置发送", "accelerationText====" + a + "waterTimeText====" + b + "attitudDeterminationTimeText====" + c);
                EventBus.getDefault().post(new SendConfigParamsEvent(a, b, c));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // dispatch data
        Log.d("Event Bus: ", event.message);
    }
}