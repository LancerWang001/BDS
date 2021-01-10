package com.example.bds.layouts;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;

public class NumberalEdit extends android.support.v7.widget.AppCompatEditText {
    static String TAG = "NumberalEdit";

    public NumberalEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] numberScope = initAttrs(attrs);
        this.setMinWidth(60);
        this.setGravity(Gravity.CENTER);
        this.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.setInputType(InputType.TYPE_CLASS_NUMBER);
        this.setSingleLine(true);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setTextSize(22);
        this.setFilters(new NumberalInputFilter[]{new NumberalInputFilter(numberScope)});
    }

    private int[] initAttrs (AttributeSet attrs) {
        int maxNum = 255;
        int minNum = 0;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (attrName.equals("maxNum")) {
                try {
                    maxNum = Integer.parseInt(attrValue);
                } catch (Exception e) {
                    Log.d(TAG, "maxNum : format of " + attrValue + " is not valid.");
                }
            }
            if (attrName.equals("minNum")) {
                try {
                    minNum = Integer.parseInt(attrValue);
                } catch (Exception e) {
                    Log.d(TAG, "minNum : format of " + attrValue + " is not valid.");
                }
            }
        }
        return new int[]{maxNum, minNum};
    }

    public static class NumberalInputFilter implements InputFilter {
        int maxNum = 255;
        int minNum = 0;

        public NumberalInputFilter (int[] numberScope) {
            maxNum = numberScope[0];
            minNum = numberScope[1];
        }

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            String sourceText = charSequence.toString();
            String spannedText = spanned.toString();
            try {
                int sum = Integer.parseInt(spannedText + sourceText);
                if (sum > maxNum || sum < minNum) {
                    return "";
                }
                return null;
            } catch (Exception e) {
                return "";
            }
        }
    }
}
