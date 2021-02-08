package com.example.bds.mainfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.Constants;
import com.example.bds.HomeActivity;
import com.example.bds.R;
import com.example.bds.layouts.CardMatchPair;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DevOpsFragment extends Fragment {

    static String TAG = "DevOpsFragment";

    TableLayout pairTable;

    RadioGroup cmntGroup;

    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dev_ops, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = Objects.requireNonNull(getActivity()).getSharedPreferences(Constants.DEVOPSCONFIG, Context.MODE_PRIVATE);
        pairTable = (TableLayout) getActivity().findViewById(R.id.pairTable);
        cmntGroup = (RadioGroup) getActivity().findViewById(R.id.devopsCmnt);
        initDevOpsConfig();
        // bind event to add pairs
        getActivity().findViewById((R.id.devOpsAdd)).setOnClickListener((v) -> {
            CardMatchPair pair = (CardMatchPair) View.inflate(getContext(), R.layout.layout_card_match_pair, null);
            pairTable.addView(pair, pairTable.getChildCount());
        });

        getActivity().findViewById(R.id.confirmDevConfig).setOnClickListener((v) -> {
            saveDevOpsData();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void saveDevOpsData () {
        Map<String, String> pairMap = new HashMap<>();
        Gson gson = new Gson();
        // save BDS device config
        if (pairTable.getChildCount() > 3) {
            for (int i = 2; i < pairTable.getChildCount(); i ++) {
                CardMatchPair pair = (CardMatchPair) pairTable.getChildAt(i);
                pairMap.put(pair.getDeviceId(), pair.getBDSCard());
            }
        }
        // save cmnt way
        String cmntway = "BD";
        if (cmntGroup.getCheckedRadioButtonId() == R.id.devopsCmnt_defaultChecked) {
            cmntway = "DT";
        }
        try {
            String json = gson.toJson(pairMap);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constants.BDSDeviceConfig, json);
            editor.putString(Constants.ComntWay, cmntway);
            editor.apply();
            Toast.makeText(getContext(), "开发数据导入成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "开发数据导入失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initDevOpsConfig () {
        HashMap<String, String> pairMap;
        Gson gson = new Gson();
        try {
            String json = sp.getString(Constants.BDSDeviceConfig, "");
            JsonParser jsonParser = new JsonParser();
            assert json != null;
            JsonObject obj = jsonParser.parse(json).getAsJsonObject();
            pairMap = gson.fromJson(obj, HashMap.class);
            Set<Map.Entry<String, String>> set = pairMap.entrySet();
            for (Map.Entry<String, String> entry : set) {
                CardMatchPair pair = (CardMatchPair) View.inflate(getContext(), R.layout.layout_card_match_pair, null);
                pair.setDeviceId(entry.getKey());
                pair.setBDSCardId(entry.getValue());
                pairTable.addView(pair, pairTable.getChildCount());
            }
            String cmntway = sp.getString(Constants.ComntWay, "");
            if (cmntway.equals("BD")) cmntGroup.check(R.id.devopsCmnt_bd);
            Toast.makeText(getContext(), "开发数据导入成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "开发数据导入失败", Toast.LENGTH_SHORT).show();
        }

    }
}