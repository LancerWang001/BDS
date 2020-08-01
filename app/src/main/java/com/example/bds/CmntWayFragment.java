package com.example.bds;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AlertDialog dialog;
    private int checkedId;

    public CmntWayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CmntWayFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cmnt_way, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.dialog = createDialog();
        getActivity().findViewById(R.id.send_cmntway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup rg = (RadioGroup)getActivity().findViewById(R.id.cmnt_way_rg);
                CmntWayFragment.this.checkedId = rg.getCheckedRadioButtonId();
                CmntWayFragment.this.dialog.show();
            }
        });
    }

    private AlertDialog createDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.card_dialog_alert);
        builder.setPositiveButton(R.string.card_dialog_alert_Y, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int cmntWay = R.string.card_cmnt_dt;
                switch (CmntWayFragment.this.checkedId) {
                    case R.id.card_cmnt_bd:
                        cmntWay = R.string.card_cmnt_bd;
                        break;
                    case R.id.card_cmnt_dt:
                        cmntWay = R.string.card_cmnt_dt;
                }
                TextView tv = (TextView) getActivity().findViewById(R.id.cmnt_way);
                tv.setText(cmntWay);
            }
        });
        builder.setNegativeButton(R.string.card_dialog_alert_N, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }
}