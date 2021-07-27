package com.hanheldpos.utils.binding;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class BindingAdapterUtils {

    @BindingAdapter({"groupSize"})
    public static void setGroupSize(TextInputEditText inputEditText, int groupSize) {
        if (inputEditText == null) return;

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Pattern special= Pattern.compile("[^A-Z0-9]", Pattern.CASE_INSENSITIVE);
                if(special.matcher(editable.toString()).find()){
                    String remain = editable.toString().replaceAll("[^A-Z0-9]", "");
                    inputEditText.setText(remain);
                    inputEditText.setSelection(remain.length());
                    return;
                }
                Object[] paddingSpans = editable.getSpans(0, editable.length(), SpaceSpan.class);
                for (Object span : paddingSpans) {
                    editable.removeSpan(span);
                }

                addSpans(editable);
            }

            private void addSpans(Editable editable) {
                final int length = editable.length();
                for (int i = 1; i * (groupSize) < length; i++) {
                    int index = i * groupSize;
                    editable.setSpan(new SpaceSpan(), index - 1, index,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            class SpaceSpan extends ReplacementSpan {
                @Override
                public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
                    float padding = paint.measureText(" ", 0, 1);
                    float textSize = paint.measureText(text, start, end);
                    return (int) (padding + textSize);
                }

                @Override
                public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
                    canvas.drawText(text.subSequence(start, end) + " ", x, y, paint);
                }
            }
        });
    }
}
