package mhwang.com.takecareofmoney;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import mhwang.com.abstracts.PagerFragment;
import mhwang.com.bean.Record;
import mhwang.com.database.DBUtil;
import mhwang.com.util.DateUtil;
import mhwang.com.util.LogUitl;

/**
 * 项目名称：
 * 类描述：
 * 作者：王明海
 * 创建时间：2016/4/7
 */
public class MoneyReportFragment extends PagerFragment {
    private View mView;

    public final static String[] months = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月",
            "9月", "10月", "11月", "12月"};

//    public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

    public final static int LINE_DAYS = 31;
    public final static int MAX_MONEY = 2000;
    public final static int ONE_KILOMETER = 1000;
    public final static int ONE_YEAR_MONTH = 12;

    public int[] days;
    private LineChartView chartTop;
    private ColumnChartView chartBottom;

    private LineChartData lineData;
    private ColumnChartData columnData;

    private ImageButton ib_previous;
    private ImageButton ib_next;
    private TextView tv_year;


    double[] monthIncomes;
    double[] monthOutcomes;
    double[] monthSurpluses;
    private int year;

    private void showLog(String msg){
        Log.d("--MoneyReportFragment--->", msg);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        // 获取当前月份
        int curMonth = DateUtil.getInstance().getMonth();
        getMonthData(year, curMonth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_money_report,null);
        chartTop = (LineChartView) mView.findViewById(R.id.chart_top);
        chartBottom = (ColumnChartView) mView.findViewById(R.id.chart_bottom);
        ib_previous = (ImageButton) mView.findViewById(R.id.ib_money_report_previous);
        ib_next = (ImageButton) mView.findViewById(R.id.ib_money_report_next);
        tv_year = (TextView) mView.findViewById(R.id.tv_money_report_year);
        initEvent();
        // Generate and set data for line chart
        // 初始化折线数据
//        generateInitialLineData();
        initLineData();
        // *** BOTTOM COLUMN CHART ***


//        generateColumnData();
        initColumnData();
        return mView;
    }

    private void initEvent(){
        tv_year.setText(year+"年");
        ib_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year -= 1;
                updateMonthData(year);
            }
        });

        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year += 1;
                updateMonthData(year);
            }
        });
    }

    /** 更新月份数据
     * @param year
     */
    private void updateMonthData(int year){
        showLog("update year "+year +"data");
        int curYear = DateUtil.getInstance().getYear();
        int curMonth = DateUtil.getInstance().getMonth();
        if (year != curYear) {
            getMonthData(year, ONE_YEAR_MONTH);
        }else {
            showLog("update this year "+curYear +" data");
            getMonthData(year,curMonth);
        }
        initLineData();
        initColumnData();
        tv_year.setText(year+"年");
    }

    /**
     *  初始化数据
     */
    private void initData(){
        monthIncomes = new double[ONE_YEAR_MONTH];
        monthOutcomes = new double[ONE_YEAR_MONTH];
        monthSurpluses = new double[ONE_YEAR_MONTH];
        year = DateUtil.getInstance().getYear();
    }

    /**
     *  重置月份数据
     */
    private void resetMonthsData(){
        for(int i = 0; i < ONE_YEAR_MONTH; i++){
            monthIncomes[i] = 0.00;
        }
    }

    /** 获取每个月的数据
     * @param monthCounts
     */
    private void getMonthData(int year ,int monthCounts){
        resetMonthsData();

        DBUtil dbUtil = DBUtil.getInstance(getActivity());

        for (int i = 1; i <= monthCounts; i++){
            ArrayList<Record> records = dbUtil.readRecordsByMonth(year,i);
            double monthOutcome = 0.00;
            double monthIncome = 0.00;
            for(Record record : records){
                if (record.getStatus().equals("支出")){
                    monthOutcome += record.getMoney();
                }else{
                    monthIncome += record.getMoney();
                }
            }
            monthIncomes[i-1] = monthIncome;
            monthOutcomes[i-1] = monthOutcome;
            monthSurpluses[i-1] = Math.abs(monthIncome - monthOutcome);
            LogUitl.showLog("MoneyReportFragment","month "+i+" income is "+monthIncome);
            LogUitl.showLog("MoneyReportFragment","month "+i+" outcome is "+monthOutcome);
            LogUitl.showLog("MoneyReportFragment","month "+i+" surplus is "+monthSurpluses[i-1]);
        }
    }

    /**
     *  初始化月份柱形数据
     */
    private void initColumnData(){
        int numSubcolumns = 1;
        int numColumns = months.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                float KSurplus = (float) monthSurpluses[i] / ONE_KILOMETER;
                showLog("the column " + i + " Kdata is " + KSurplus);
                values.add(new SubcolumnValue(KSurplus, ChartUtils.pickColor()));
            }

            axisValues.add(new AxisValue(i).setLabel(months[i]));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).
                setHasLines(true).setTextColor(Color.BLACK));
        columnData.setAxisYLeft(new Axis().setHasLines(true).
                setMaxLabelChars(2).setTextColor(Color.BLACK).setName("单位(千)"));

        chartBottom.setColumnChartData(columnData);

        // Set value touch listener that will trigger changes for chartTop.
        chartBottom.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);

//        chartBottom.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 40, 12, 0);
        chartBottom.setMaximumViewport(v);
        chartBottom.setCurrentViewport(v);
        chartBottom.setZoomType(ZoomType.HORIZONTAL);

//        chartBottom.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                SelectedValue sv = chartBottom.getSelectedValue();
//                if (!sv.isSet()) {
//                    generateInitialLineData();
//                }
//
//            }
//        });
    }

    private void generateColumnData() {

        int numSubcolumns = 1;
        int numColumns = months.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            axisValues.add(new AxisValue(i).setLabel(months[i]));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

        chartBottom.setColumnChartData(columnData);

        // Set value touch listener that will trigger changes for chartTop.
        chartBottom.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);

        chartBottom.setZoomType(ZoomType.HORIZONTAL);

         chartBottom.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 SelectedValue sv = chartBottom.getSelectedValue();
                 if (!sv.isSet()) {
                     generateInitialLineData();
                 }

             }
         });

    }

    /**
     *  初始化拆线数据
     */
    private void initLineData(){
        int numValues = LINE_DAYS;
        days = new int[LINE_DAYS];
        for (int i = 1; i <= days.length; i++){
            days[i-1] = i;
        }

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> mIncomes = new ArrayList<PointValue>();
        List<PointValue> mOutcomes = new ArrayList<PointValue>();
        // 初始化横坐标数据和拆线数据
        for (int i = 0; i < numValues; ++i) {

            mIncomes.add(new PointValue(i, 0));
            mOutcomes.add(new PointValue(i, 0));

            axisValues.add(new AxisValue(i).setLabel(Integer.toString(days[i])));
        }

        // 创建拆线对象
        Line incomeLine = new Line(mIncomes);
        Line outcomeLine = new Line(mOutcomes);

        incomeLine.setColor(ChartUtils.COLOR_RED).setCubic(true);
        outcomeLine.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(incomeLine);
        lines.add(outcomeLine);

        lineData = new LineChartData(lines);
        Axis X = new Axis(axisValues);
        X.setTextColor(Color.BLACK);
        X.setName("单位(日)");
        X.setHasLines(true);
        lineData.setAxisXBottom(X);
        Axis Y = new Axis();
        Y.setHasLines(true);
        Y.setMaxLabelChars(2);
        Y.setTextColor(Color.BLACK);
        Y.setName("单位(元)");
        lineData.setAxisYLeft(Y);

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, MAX_MONEY, LINE_DAYS, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);


    }

    /**
     * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
     * will select value on column chart.
     */
    private void generateInitialLineData() {
        int numValues = 30;
        days = new int[30];
        for (int i = 1; i <= days.length; i++){
            days[i-1] = i;
        }
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, i));
            axisValues.add(new AxisValue(i).setLabel(Integer.toString(days[i])));
        }

        Line line = new Line(values);

        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        // 我加的下面一句
//        lineData.setValueLabelsTextColor(ChartUtils.COLOR_RED);
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 15000, 30, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    /**  更新拆线数据
     * @param month
     * @param range
     */
    private void updateLineData(int month,float range){
        LogUitl.showLog("MoneyReportFragment","update the month "+month+" line data");
        // 读取这个月份的每一天的数据
        int year = DateUtil.getInstance().getYear();
        double[] dayIncomes = new double[LINE_DAYS];
        double[] dayOutcomes = new double[LINE_DAYS];
        DBUtil util = DBUtil.getInstance(getActivity());
        for (int i = 0; i < dayIncomes.length; i++){
            ArrayList<Record> records = util.readRecordsByDay(year,month,i+1);
            double incomes = 0.00;
            double outcomes = 0.00;
            LogUitl.showLog("MoneyReportFragment","this month day "+(i+1)+"record size is"
            +records.size());
            for(Record record : records){
                if(record.getStatus().equals("支出")){
                    outcomes += record.getMoney();
                }else{
                    incomes += record.getMoney();
                }
            }

            dayIncomes[i] = incomes;
            dayOutcomes[i] = outcomes;
            LogUitl.showLog("MoneyReportFragment","this month day "+(i+1)
                    +" incomes is "+incomes+" outcomes is "+outcomes);
        }

        // 修改拆线数据
        Line incomeLine = lineData.getLines().get(0);
        incomeLine.setColor(Color.GREEN);
        Line outcomeLine = lineData.getLines().get(1);
        outcomeLine.setColor(Color.RED);

        // 修改收入拆线
        ArrayList<PointValue> incomeValus = (ArrayList<PointValue>) incomeLine.getValues();
        for(int i = 0; i < incomeValus.size(); i++){
            PointValue value = incomeValus.get(i);
            value.setTarget(value.getX(),(float)dayIncomes[i]*range);
        }

        // 修改支出拆线
        ArrayList<PointValue> outcomeValus = (ArrayList<PointValue>) outcomeLine.getValues();
        for(int i = 0; i < outcomeValus.size(); i++){
            PointValue value = outcomeValus.get(i);
            value.setTarget(value.getX(),(float)dayOutcomes[i]*range);
        }

        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(300);

    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * range);
        }

        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(300);
    }

    @Override
    public void updateData() {

    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
//            generateLineData(value.getColor(), 100);
            updateLineData(columnIndex+1,1);
        }

        @Override
        public void onValueDeselected() {
            updateLineData(-1, 0);
//            generateLineData(ChartUtils.COLOR_GREEN, 0);

        }
    }

}
