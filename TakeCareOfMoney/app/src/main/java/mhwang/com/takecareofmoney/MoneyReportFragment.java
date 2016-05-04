package mhwang.com.takecareofmoney;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public final static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec",};

//    public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

    public final static int LINE_DAYS = 31;

    public int[] days;
    private LineChartView chartTop;
    private ColumnChartView chartBottom;

    private LineChartData lineData;
    private ColumnChartData columnData;

    double[] monthIncomes;
    double[] monthOutcomes;
    double[] monthSurpluses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_money_report,null);
        chartTop = (LineChartView) mView.findViewById(R.id.chart_top);

        // Generate and set data for line chart
        // 初始化折线数据
//        generateInitialLineData();
        initLineData();
        // *** BOTTOM COLUMN CHART ***

        chartBottom = (ColumnChartView) mView.findViewById(R.id.chart_bottom);

//        generateColumnData();
        initColumnData();
        return mView;
    }

    /**
     *  初始化数据
     */
    private void initData(){
        // 获取当前月份
        int curMonth = DateUtil.getInstance().getMonth();
        getMonthData(curMonth);
    }

    /** 获取每个月的数据
     * @param month
     */
    private void getMonthData(int month){
        monthIncomes = new double[month];
        monthOutcomes = new double[month];
        monthSurpluses = new double[month];
        DBUtil dbUtil = DBUtil.getInstance(getActivity());
        for (int i = 1; i <= month; i++){
            ArrayList<Record> records = dbUtil.readMonthRecords(i);
            double monthOutcome = 0.00;
            double monthIncome = 0.00;
            for(Record record : records){
                if (record.getStatus() == "支出"){
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
                if (i < monthSurpluses.length) {
                    values.add(new SubcolumnValue((float) monthSurpluses[i], ChartUtils.pickColor()));
                }else{
                    values.add(new SubcolumnValue(0, ChartUtils.pickColor()));
                }
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
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 150, LINE_DAYS, 0);
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
        Viewport v = new Viewport(0, 150, 30, 0);
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
        double[] dayIncomes = new double[LINE_DAYS];
        double[] dayOutcomes = new double[LINE_DAYS];
        DBUtil util = DBUtil.getInstance(getActivity());
        for (int i = 0; i < dayIncomes.length; i++){
            ArrayList<Record> records = util.readRecordsByDay(month,i+1);
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
        Line outcomeLine = lineData.getLines().get(1);

        // 修改收入拆线
        ArrayList<PointValue> incomeValus = (ArrayList<PointValue>) incomeLine.getValues();
        for(int i = 0; i < incomeValus.size(); i++){
            PointValue value = incomeValus.get(i);
            value.setTarget(value.getX(),(float)dayIncomes[i]);
        }

        // 修改支出拆线
        ArrayList<PointValue> outcomeValus = (ArrayList<PointValue>) outcomeLine.getValues();
        for(int i = 0; i < outcomeValus.size(); i++){
            PointValue value = outcomeValus.get(i);
            value.setTarget(value.getX(),(float)dayOutcomes[i]);
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
            updateLineData(columnIndex+1,100);
        }

        @Override
        public void onValueDeselected() {

            generateLineData(ChartUtils.COLOR_GREEN, 0);

        }
    }

}
