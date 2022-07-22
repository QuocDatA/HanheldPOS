package com.hanheldpos.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.diadiem.pos_components.PTextView;
import com.diadiem.pos_components.enumtypes.FontStyleEnum;
import com.diadiem.pos_components.enumtypes.TextHeaderEnum;
import com.hanheldpos.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class TableLayoutFixedHeader extends RelativeLayout {

    public static class Title {
        public String name;
        public int color;

        public Title(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public Title(String name) {
            this.name = name;
            this.color = Color.parseColor("#333333");
        }
    }

    public static class Row {
        public Object value;
        public List<Title> titles;

        public Row(List<Title> titles) {
            this.titles = titles;
        }

        public Row(List<Title> titles, Object value) {
            this.titles = titles;
            this.value = value;
        }
    }

    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    ImageView directionLeft;
    ImageView directionRight;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;

    View dividerHorizontal;

    Context context;

    @Nullable
    OnRowClickListener onRowClickListener;

    private final List<Title> headers = new ArrayList<>();

    private final List<Row> rows = new ArrayList<>();

    private final List<Integer> columnAligns = new ArrayList<>();

    private int headerCellWidth = 0;

    private int numberColumns = 3;

    private boolean wrapText = false;

    public TableLayoutFixedHeader(Context context) {
        super(context);

    }

    public TableLayoutFixedHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();

        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
    }

    public TableLayoutFixedHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TableLayoutFixedHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        this.headerCellWidth = displayMetrics.widthPixels / numberColumns;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }




    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    public void addHeader(Title header) {
        headers.add(header);
        setupHeader();
    }

    public void addRangeHeaders(List<Title> headers) {
        this.headers.addAll(headers);
        setupHeader();
    }

    public void clearHeader() {
        headers.clear();
        setupHeader();
    }

    public void addRow(Row item) {
        rows.add(item);
        setupRow();
    }

    public void addRangeRows(List<Row> items) {
        rows.addAll(items);
        setupRow();
    }

    public void setColumnAligns(List<Integer> columnAligns) {
        this.columnAligns.clear();
        this.columnAligns.addAll(columnAligns);
        setupHeader();
        setupRow();
    }

    public void clearRow() {
        rows.clear();
        setupRow();
    }

    public void setOnClickRowListener(@Nullable OnRowClickListener l) {
        this.onRowClickListener = l;
        setupOnClickListener();
    }

    private void setupOnClickListener() {
        ViewGroup viewC = (ViewGroup) scrollViewC.getChildAt(0);
        if (viewC.getChildCount() <= 0) return;
        ViewGroup viewD = (ViewGroup) ((ViewGroup) scrollViewD.getChildAt(0)).getChildAt(0);
        for (int i = 0; i < viewC.getChildCount(); i++) {
            int finalI = i;
            if (onRowClickListener != null) {
                viewC.getChildAt(finalI).setOnClickListener(v -> {
                    onRowClickListener.onClick(rows.get(finalI));
                });
                viewD.getChildAt(finalI).setOnClickListener(v -> {
                    onRowClickListener.onClick(rows.get(finalI));
                });
            }
        }
    }

    public void setNumberColumns(Integer numberColumns) {
        this.numberColumns = numberColumns;
    }

    public void setWrapText(boolean isWrapText) {
        this.wrapText = isWrapText;
        this.requestLayout();
    }

    public void setHeaderBackground(int color) {
        this.tableA.setBackgroundColor(color);
        this.tableB.setBackgroundColor(color);
    }

    void setupHeader() {
        if (tableA == null || tableB == null) return;
        this.tableA.removeAllViews();
        this.tableA.removeAllViewsInLayout();
        this.tableB.removeAllViews();
        this.tableB.removeAllViewsInLayout();
        // add some table rows
        this.addTableRowToTableA();
        this.addTableRowToTableB();
        this.resizeHeaderHeight();
        this.resizeDividerHorizontalHeight();
        this.invalidate();
    }

    void setupRow() {
        if (tableC == null || tableD == null) return;
        this.tableC.removeAllViewsInLayout();
        this.tableD.removeAllViewsInLayout();
        this.generateTableC_AndTable_D();
        this.resizeBodyTableRowHeight();
        this.resizeDividerHorizontalHeight();
        this.setupOnClickListener();
        this.invalidate();

    }

    // initalized components
    private void initComponents() {

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new CustomHorizontalScrollView(this.context);
        this.horizontalScrollViewB.setOverScrollMode(OVER_SCROLL_NEVER);
        this.horizontalScrollViewB.setHorizontalScrollBarEnabled(false);
        this.horizontalScrollViewD = new CustomHorizontalScrollView(this.context);
        this.horizontalScrollViewD.setOverScrollMode(OVER_SCROLL_NEVER);
        this.scrollViewC = new CustomScrollView(this.context);
        this.scrollViewC.setVerticalScrollBarEnabled(false);
        this.scrollViewD = new CustomScrollView(this.context);
        this.scrollViewD.setOverScrollMode(OVER_SCROLL_NEVER);

        this.tableA.setBackgroundColor(getResources().getColor(R.color.color_11));
        this.tableB.setBackgroundColor(getResources().getColor(R.color.color_11));

        this.dividerHorizontal = new View(this.context);
        this.directionLeft = new ImageView(this.context);
        this.directionRight = new ImageView(this.context);

    }


    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = Math.max(rowAHeight, rowBHeight);

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableC.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {
            if (!(this.tableC.getChildAt(x) instanceof TableRow) && !(this.tableD.getChildAt(x) instanceof TableRow))
                continue;
            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = Math.max(rowAHeight, rowBHeight);

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    void resizeDividerHorizontalHeight() {
        int totalHeight = 0;
        int tableA_ChildCount = this.tableA.getChildCount();
        int tableC_ChildCount = this.tableC.getChildCount();
        for (int x = 0; x < tableA_ChildCount; x++) {
            TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(x);
            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            totalHeight += rowAHeight;
        }
        for (int x = 0; x < tableC_ChildCount; x++) {

            View productNameBodyTableRow = this.tableC.getChildAt(x);
            int rowAHeight = this.viewHeight(productNameBodyTableRow);
            totalHeight += rowAHeight;
        }
        LayoutParams params = (RelativeLayout.LayoutParams) this.dividerHorizontal.getLayoutParams();
        params.height = totalHeight - (params.bottomMargin + params.topMargin);
    }


    // we add the components here in our TableMainLayout
    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    private void addComponentToMainLayout() {
        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        RelativeLayout.LayoutParams componentDividerHorizontal = new RelativeLayout.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics()),
                LayoutParams.WRAP_CONTENT
        );
        componentDividerHorizontal.addRule(RelativeLayout.START_OF, this.horizontalScrollViewB.getId());
        this.dividerHorizontal.setBackgroundColor(getResources().getColor(R.color.color_10));

        RelativeLayout.LayoutParams directionLeftParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen._13sdp),
                getResources().getDimensionPixelSize(R.dimen._13sdp)
        );
        directionLeftParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen._10sdp), 0, 0);
        directionLeftParams.addRule(RelativeLayout.ALIGN_END, this.tableA.getId());
        RelativeLayout.LayoutParams directionRightParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen._13sdp),
                getResources().getDimensionPixelSize(R.dimen._13sdp)
        );
        directionRightParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen._10sdp), 0, 0);
        directionRightParams.addRule(RelativeLayout.ALIGN_END, this.horizontalScrollViewB.getId());

        directionLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_left_disable));
        directionRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_right_disable));


        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(directionLeft, directionLeftParams);
        this.addView(directionRight, directionRightParams);
        this.addView(this.dividerHorizontal, componentDividerHorizontal);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);


    }

    private void addTableRowToTableA() {
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        this.tableB.addView(this.componentBTableRow());

    }

    // set essential component IDs
    @SuppressLint("ResourceType")
    private void setComponentsId() {
        this.tableA.setId(1);
        this.horizontalScrollViewB.setId(2);
        this.scrollViewC.setId(3);
        this.scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // generate table row of table A
    TableRow componentATableRow() {
        TableRow componentATableRow = new TableRow(this.context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellWidth, TableRow.LayoutParams.MATCH_PARENT);
        if (!this.headers.isEmpty()) {
            TextView textView = this.headerTextView(this.headers.get(0), columnAligns.size() <= 0 ? Gravity.CENTER : columnAligns.get(0));
            componentATableRow.addView(textView, params);
        }
        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.size();

        TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellWidth, TableRow.LayoutParams.MATCH_PARENT);
        for (int x = 1; x < (headerFieldCount); x++) {
            TextView textView = this.headerTextView(this.headers.get(x), x >= columnAligns.size() - 1 ? Gravity.CENTER : columnAligns.get(x));
            textView.setWidth(headerCellWidth);
            componentBTableRow.addView(textView, params);
        }
        return componentBTableRow;
    }

    // header standard TextView
    TextView headerTextView(Title header, int gravity) {
        PTextView headerTextView = new PTextView(this.context);
        headerTextView.setText(header.name);
        headerTextView.setTextSize(TextHeaderEnum.H5);
        headerTextView.setTextStyle(FontStyleEnum.BOLD);
        headerTextView.setTextColor(header.color);
//        headerTextView.setTextColor(TextColorEnum.Color4);
        headerTextView.setGravity(gravity);
        if (!this.wrapText) {
            headerTextView.setMaxLines(1);
            headerTextView.setEllipsize(TextUtils.TruncateAt.END);
        }
        headerTextView.setPadding(getResources().getDimensionPixelSize(R.dimen._7sdp), getResources().getDimensionPixelSize(R.dimen._10sdp), getResources().getDimensionPixelSize(R.dimen._7sdp), getResources().getDimensionPixelSize(R.dimen._10sdp));

        return headerTextView;
    }

    // table cell standard TextView
    TextView bodyTextView(Title label, int gravity) {
        PTextView bodyTextView = new PTextView(this.context);
        bodyTextView.setTextSize(TextHeaderEnum.H5);
        bodyTextView.setTextStyle(FontStyleEnum.NORMAL);
        bodyTextView.setTextColor(label.color);
//        bodyTextView.setTextColor(TextColorEnum.Color4);
        bodyTextView.setText(label.name);
        bodyTextView.setGravity(gravity);
        if (!this.wrapText) {
            bodyTextView.setMaxLines(1);
            bodyTextView.setEllipsize(TextUtils.TruncateAt.END);
        }
        bodyTextView.setPadding(getResources().getDimensionPixelSize(R.dimen._7sdp), getResources().getDimensionPixelSize(R.dimen._10sdp), getResources().getDimensionPixelSize(R.dimen._7sdp), getResources().getDimensionPixelSize(R.dimen._10sdp));
        return bodyTextView;
    }


    // generate table row of table C and table D
    private void generateTableC_AndTable_D() {
        for (Row row : this.rows) {
            Title rowHeader = row.titles.size() > 0 ? row.titles.get(0) : new Title("");
            List<Title> rowInfo = row.titles.size() > 1 ? row.titles.subList(1, row.titles.size()) : new ArrayList<>();
            TableRow tableRowForTableC = this.tableRowForTableC(rowHeader);
            TableRow taleRowForTableD = this.taleRowForTableD(rowInfo);
            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);
        }
    }

    // a TableRow for table C
    TableRow tableRowForTableC(Title rowHeader) {
        View dividerVertical1 = new View(this.context);
        RelativeLayout.LayoutParams componentDividerVertical = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics())
        );
        dividerVertical1.setLayoutParams(componentDividerVertical);
        dividerVertical1.setBackgroundColor(getResources().getColor(R.color.color_10));
        TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellWidth, TableRow.LayoutParams.MATCH_PARENT);
        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.bodyTextView(rowHeader, columnAligns.size() <= 0 ? Gravity.CENTER : columnAligns.get(0));
        textView.setLayoutParams(params);
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView);
        linearLayout.addView(dividerVertical1);
        tableRowForTableC.addView(linearLayout);

        return tableRowForTableC;
    }

    TableRow taleRowForTableD(List<Title> row) {
        RelativeLayout.LayoutParams componentDividerVertical = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics())
        );
        View dividerVertical2 = new View(this.context);
        dividerVertical2.setLayoutParams(componentDividerVertical);
        dividerVertical2.setBackgroundColor(getResources().getColor(R.color.color_10));
        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();

        TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellWidth, TableRow.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout1 = new LinearLayout(this.context);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout linearLayout2 = new LinearLayout(this.context);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        for (int x = 0; x < loopCount; x++) {
            TextView textViewD = this.bodyTextView(x >= row.size() ? new Title("") : row.get(x), x + 1 >= columnAligns.size() - 1 ? Gravity.CENTER : columnAligns.get(x + 1));
            textViewD.setWidth(headerCellWidth);
            linearLayout1.addView(textViewD, params);

        }
        linearLayout2.addView(linearLayout1);
        linearLayout2.addView(dividerVertical2);
        taleRowForTableD.addView(linearLayout2);
        return taleRowForTableD;

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }


    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    private class CustomHorizontalScrollView extends HorizontalScrollView {

        public CustomHorizontalScrollView(Context context) {
            super(context);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if (horizontalScrollViewB.getChildAt(0).getBottom() <= l) {
                //scroll view is at bottom
                directionRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_right_disable));
            } else {
                directionRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_right));
            }
            if (l == 0) {
                //scroll view is not at bottom
                directionLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_left_disable));
            } else {
                directionLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_direction_left));
            }

            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);

            }
        }
    }

    // scroll view custom class
    private class CustomScrollView extends ScrollView {

        public CustomScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }

    public interface OnRowClickListener {
        void onClick(Row row);
    }
}


