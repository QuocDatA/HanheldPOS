package com.hanheldpos.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.hanheldpos.R;

/**
 * This is used for tab layout with margin between them
 * Using FullWidthTabLayoutWithoutMargin instead if this is not the above case
 */
public class CustomWidthTabLayout extends TabLayout {

    private int marginAmongTabs;
    private int tabWidth;

    public CustomWidthTabLayout(Context context) {
        super(context);
    }

    public CustomWidthTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.customWidthTabLayoutStyle);
    }

    public CustomWidthTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue(context, attrs, defStyleAttr);
    }

    private void initValue(Context context, AttributeSet attrs, int defStyleAttr) {
        final Resources.Theme theme = context.getTheme();

        TypedArray customWidthTabAttr = theme.obtainStyledAttributes(attrs, R.styleable.CustomWidthTabLayout, defStyleAttr, 0);
        TypedArray fullWidthTabAttr = theme.obtainStyledAttributes(attrs, R.styleable.FullWidthTabLayout, defStyleAttr, 0);

        marginAmongTabs = fullWidthTabAttr.getDimensionPixelSize(R.styleable.FullWidthTabLayout_marginAmongTabs, 0);
        tabWidth = customWidthTabAttr.getDimensionPixelSize(R.styleable.CustomWidthTabLayout_tabWidth, 0);

        setMarginAmongTabs(marginAmongTabs);
        setTabWidth(tabWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ViewGroup tabLayout = (ViewGroup) getChildAt(0);
        int childCount = tabLayout.getChildCount();

        if (childCount != 0) {
            for (int i = 0; i < childCount; ++i) {
                tabLayout.getChildAt(i).setMinimumWidth(tabWidth);
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

    public void setTabWidth(int width) {
        this.tabWidth = width;
        invalidate();
    }
}
