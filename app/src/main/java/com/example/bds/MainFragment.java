package com.example.bds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.events.uppercontrol.SendUpperControlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public static final String status = "a";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView t1;
    TextView locationText;
    SharedPreferences context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
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
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CardView cardView = (CardView) getActivity().findViewById(R.id.fragmentcard);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.background));
        LinearLayout selfBtn = (LinearLayout) getActivity().findViewById(R.id.card_main_selfcheck);

        selfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity home = (HomeActivity) getActivity();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new SelfCheckFragment())
                        .commit();
            }
        });

        LinearLayout trackMoniBtn = (LinearLayout) getActivity().findViewById(R.id.card_main_position);
        trackMoniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new TrackMonitorFragment())
                        .commit();
                //取变量
                HomeActivity activity = (HomeActivity) getActivity();
                int way = activity.bdsService.COMMUNICATE_WAY;
                String dt;
                String bd;
                if (R.string.card_cmnt_dt == way) {
                    dt = "Y";
                } else {
                    dt = "N";
                }
                if (R.id.card_cmnt_bd == way) {
                    bd = "Y";
                } else {
                    bd = "N";
                }
                Log.d("dt=====", dt + " bd=======" + bd);
                Log.d("way=====", String.valueOf(way));
                EventBus.getDefault().post(new SendUpperControlEvent(bd, "60", dt, "2"));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String mess) {
    }
}