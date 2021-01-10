package com.example.bds.homefragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bds.HomeActivity;
import com.example.bds.R;
import com.example.beans.CmntIntervalBean;
import com.example.events.uppercontrol.SendUpperControlEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CmntWayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CmntWayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String cmtWay = "";
    SharedPreferences mContextSp;
    SharedPreferences mActivitySp;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AlertDialog dialog;
    private int checkedId;

    public CmntWayFragment() {
        // Required empty public constructor
    }

    public static CmntWayFragment newInstance(String param1, String param2) {
        CmntWayFragment fragment = new CmntWayFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CmntIntervalBean intervals = ((HomeActivity) Objects.requireNonNull(getActivity())).intervals;
        EditText dtInterval = getActivity().findViewById(R.id.dt_interval);
        EditText bdInterval = getActivity().findViewById(R.id.bd_interval);
        dtInterval.setText(intervals.getDtInterval());
        bdInterval.setText(intervals.getBdInterval());

        Button confirmBtn = getActivity().findViewById(R.id.param_set);
        confirmBtn.setOnClickListener((v) -> {
            int cmntWay = ((HomeActivity) getActivity()).bdsService.COMMUNICATE_WAY;
            String bdPermit, bdIntervalVal, dtPermit, dtIntervalVal;
            if (cmntWay == R.string.card_cmnt_dt) {
                bdPermit = "N";
                dtPermit = "Y";
            } else {
                bdPermit = "Y";
                dtPermit = "N";
            }
            bdIntervalVal = String.valueOf(bdInterval.getText());
            dtIntervalVal = String.valueOf(dtInterval.getText());
            intervals.setBdInterval(bdIntervalVal);
            intervals.setDtInterval(dtIntervalVal);
            EventBus.getDefault().post(new SendUpperControlEvent(bdPermit, bdIntervalVal, dtPermit, dtIntervalVal));
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cmnt_way, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}