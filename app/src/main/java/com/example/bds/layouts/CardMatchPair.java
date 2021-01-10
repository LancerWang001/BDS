package com.example.bds.layouts;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bds.R;

public class CardMatchPair extends TableRow {

    public CardMatchPair(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        TableRow tb = (TableRow)this.getChildAt(2);
        TextView tv = (TextView)tb.getChildAt(0);
        tv.setOnClickListener(v -> {
            TableLayout tl = ((TableLayout) getParent());
            tl.removeView(this);
            Toast.makeText(getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
        });
    }

    public void setDeviceId (String deviceId) {
        TableRow tb = (TableRow)this.getChildAt(0);
        EditText et = (EditText)tb.getChildAt(0);
        et.setText(deviceId);
    }

    public String getDeviceId () {
        TableRow tb = (TableRow)this.getChildAt(0);
        EditText et = (EditText)tb.getChildAt(0);
        return et.getText().toString();
    }

    public void setBDSCardId (String cardId) {
        TableRow tb = (TableRow)this.getChildAt(1);
        EditText et = (EditText)tb.getChildAt(0);
        et.setText(cardId);
    }

    public String getBDSCard () {
        TableRow tb = (TableRow)this.getChildAt(1);
        EditText et = (EditText)tb.getChildAt(0);
        return et.getText().toString();
    }
}
