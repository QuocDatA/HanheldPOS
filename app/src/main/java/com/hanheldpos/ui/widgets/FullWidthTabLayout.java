package com.hanheldpos.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.hanheldpos.R;

/**
 * This is used for tab layout with margin between them
 * Using FullWidthTabLayoutWithoutMargin instead if this is not the above case
 */
public class FullWidthTabLayout extends TabLayout {
    private int marginAmongTabs;

    public FullWidthTabLayout(Context context) {
        super(context);
    }

    public FullWidthTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.fullWidthTabLayoutStyle);
    }

    public FullWidthTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue(context, attrs, defStyleAttr);
    }

    private void initValue(Context context, AttributeSet attrs, int defStyleAttr) {
        final Resources.Theme theme = context.getTheme();

        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.FullWidthTabLayout, defStyleAttr, 0);
        marginAmongTabs = a.getDimensionPixelSize(R.styleable.FullWidthTabLayout_marginAmongTabs, 0);
        setMarginAmongTabs(marginAmongTabs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup tabLayout = (ViewGroup) getChildAt(0);
        int childCount = tabLayout.getChildCount();

        if (childCount != 0) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int tabMinWidth = displayMetrics.widthPixels / childCount;

            for (int i = 0; i < childCount; ++i) {
                tabLayout.getChildAt(i).setMinimumWidth(tabMinWidth);
                if (i != childCount - 1) {
                    MarginLayoutParams params = (MarginLayoutParams) tabLayout.getChildAt(i).getLayoutParams();
                    params.rightMargin = marginAmongTabs;
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMarginAmongTabs(int margin) {
        this.marginAmongTabs = margin;
        invalidate();
    }
}
