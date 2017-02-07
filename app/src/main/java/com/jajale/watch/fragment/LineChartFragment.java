package com.jajale.watch.fragment;


import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.thirdpart.linechare.Line;
import com.jajale.watch.thirdpart.linechare.LineChartView;
import com.jajale.watch.thirdpart.linechare.LinePoint;
import com.jajale.watch.thirdpart.linechare.TextAlign;
import com.jajale.watch.utils.L;

/**
 * Created by lilonghui on 2015/11/13.
 * Email:lilonghui@bjjajale.com
 */
public abstract class LineChartFragment extends HealthBaseFragment {

    public static final String TAG = "LineChartFragment";
    private LineChartView chart;
    private Activity mActivity;
    private int minHorSize = 0;// 最小水平值
    private int minVerSize = 0;// 最小垂直值
    private int horSizeofDots = 4;// 两点之间水平间距
    private float horSizeofDotsTemp;
    private int verSizeofDots = 5;// 竖直坐标间距
    private int horDivideLineCount = 0;// 水平分割线数量
    private int verDivideLineCount = 0;// 数值分割线数量
    private int maxY = 150;// 纵坐标标注的最大值

    //测试使用数据
    private String[] ages;
    private int[] upers;
    private int[] standards;
    private int[] lowers;
    private int[] myData;
    private int[] myMonth;
    private int[] months;

    abstract String[] getAges();

    abstract int[] getMydatas();

    abstract int[] getMyMonths();

    abstract int[] getUpers();

    abstract int[] getStandards();

    abstract int[] getLowers();

    abstract int[] getMonths();

    abstract String getIndicate();

    abstract String getHighText();

    abstract String getLowtext();

    abstract String unit();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ages = getAges();
        myMonth = getMyMonths();
        upers = getUpers();
        standards = getStandards();
        myData = getMydatas();
        lowers = getLowers();
        months = getMonths();


        minVerSize = lowers[0] > 20 ? 20 : 0;
        maxY = upers[upers.length - 1] + 10;
    }

    //    public static LineChartFragment newInstance() {
//        LineChartFragment fragment = new LineChartFragment();
//        return fragment;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_linechart, null);

        TextView tv_indicate = (TextView) view.findViewById(R.id.tv_indicate);
        TextView tv_lower = (TextView) view.findViewById(R.id.tv_lower);
        TextView tv_uper = (TextView) view.findViewById(R.id.tv_uper);
        tv_indicate.setText(getIndicate() + "(" + unit() + ")");
        tv_lower.setText(getLowtext());
        tv_uper.setText(getHighText());
        chart = (LineChartView) view.findViewById(R.id.linechart);
        generateLineOptions(ages);
        return view;
    }


    /**
     * 设置生成折线所需的参数
     */
    private void generateLineOptions(final String[] string_x) {

        horSizeofDotsTemp = horSizeofDots / ((float) string_x.length / 12);// horSizeofDots/?,?所代表的数字决定了可见区域显示的点数(即length/?)
        // 设置图表可见区域的最小水平值、最小垂直值、最大水平值、最大垂直值
        chart.setViewPort(minHorSize, minVerSize, (string_x.length - 1)
                * horSizeofDotsTemp, maxY + verSizeofDots);
        // 从左至右:水平间距；水平两点之间的分割线数量；竖直间距；竖直两点之间的分割线数量
        chart.setGridSize(horSizeofDots, horDivideLineCount, verSizeofDots,
                verDivideLineCount);

        Line line_most = generateLine(0, (upers.length - 1) * horSizeofDots,
                horSizeofDots, upers).setColor(0xFFffa000)
                .setFilled(false).setFilledColor(0x00F7E5E5).smoothLine(0);

        Line line_normal = generateLine(0, (standards.length - 1) * horSizeofDots,
                horSizeofDots, standards).setColor(0xFF6acb73)
                .setFilled(false).setFilledColor(0x00F7E5E5).smoothLine(0);

        Line line_mini = generateLine(0, (lowers.length - 1) * horSizeofDots,
                horSizeofDots, lowers).setColor(0xFFe75470)
                .setFilled(false).setFilledColor(0xAAF7E5E5).smoothLine(0);


        for (LinePoint point : line_most.getPoints()) {
            point.setVisible(true);// 设置拐点可见
            point.setType(LinePoint.Type.CIRCLE);// 设置拐点为圆形
            point.setRadius(6);// 设置点的初始大小
//            point.getStrokePaint().setColor(Color.RED);// 设置拐点描边的颜色，如果不设置使用默认值
        }
        for (LinePoint point : line_normal.getPoints()) {
            point.setVisible(true);// 设置拐点可见
            point.setType(LinePoint.Type.CIRCLE);// 设置拐点为圆形
            point.setRadius(6);// 设置点的初始大小
//            point.getStrokePaint().setColor(Color.GREEN);// 设置拐点描边的颜色，如果不设置使用默认值
        }
        for (LinePoint point : line_mini.getPoints()) {
            point.setVisible(true);// 设置拐点可见
            point.setType(LinePoint.Type.CIRCLE);// 设置拐点为圆形
            point.setRadius(6);// 设置点的初始大小
//            point.getStrokePaint().setColor(Color.YELLOW);// 设置拐点描边的颜色，如果不设置使用默认值
        }


        chart.addLine(line_most);
        chart.addLine(line_normal);
        chart.addLine(line_mini);

        if (myData.length > 0&&myMonth.length>0) {


//            Line line_mydata = generateLine(0, (myData.length - 1) * horSizeofDots,
//                    horSizeofDots, myData).setColor(0xFF2fbbef)
//                    .setFilled(false).setFilledColor(0xAAF7E5E5).smoothLine(0);
            L.e("line___________22myData.length=="+myData.length);
            L.e("line___________22myMonth.length==" + myMonth.length);

            Line line_mydata = generateMyLine(0, (myData.length - 1) * horSizeofDots,
                    horSizeofDots, months, myMonth, myData).setColor(0xFF2fbbef)
                    .setFilled(false).setFilledColor(0xAAF7E5E5).smoothLine(0);
            for (LinePoint point : line_mydata.getPoints()) {
                point.setVisible(true);// 设置拐点可见
                point.setType(LinePoint.Type.CIRCLE);// 设置拐点为圆形
                point.setRadius(6);// 设置点的初始大小
//            point.getStrokePaint().setColor(Color.YELLOW);// 设置拐点描边的颜色，如果不设置使用默认值
            }
            chart.addLine(line_mydata);
        }
        Paint main = new Paint();
        main.setColor(0x66848484);// 设置主网格线的颜色
        main.setStrokeWidth(0);
        Paint sub = new Paint();
        sub.setColor(0x55848484);// 设置辅网格线的颜色
        sub.setStrokeWidth(0);
        chart.setHorizontalGridStyle(main, sub);
        chart.setVerticalGridStyle(main, sub);
        chart.setVerValuesMarginsDP(8, 0, 0, 0);// 设置竖直坐标值的边距
        chart.setHorValuesMarginsDP(0, 5, 0, 0);// 设置水平坐标值的边距
        chart.setViewPortMarginsDP(30, 30, 18, 10);// 设置网格区域的边距

        Paint horValPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        horValPaint.setColor(0xff848484);// 设置坐标值字体的颜色
        horValPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10, getResources()
                        .getDisplayMetrics()));// 设置坐标值字体的大小
        Paint verValPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        verValPaint.setColor(0xff848484);
        verValPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10, getResources()
                        .getDisplayMetrics()));
        chart.setMainValuesStyle(horValPaint, verValPaint);


        //画横竖坐标
        SparseArray<String> horValues = new SparseArray<String>();
        for (int i = 0; i < string_x.length; i++) {
            horValues.put(i * horSizeofDots, string_x[i]);// i*xx的值决定了坐标值的起点位置
        }

        chart.setHorValuesText(horValues);

        SparseArray<String> verValues = new SparseArray<String>();
        for (int i = minVerSize; i <= maxY; i += verSizeofDots) {
            verValues.put(i, i + "");
        }
        chart.setVerValuesText(verValues);

        /**
         * 给每个拐点设置点击监听
         */
        chart.setOnPointClickListener(new LineChartView.OnChartPointClickListener() {
            @Override
            public void onPointClick(LinePoint point, Line line) {
                // 将所有点设置成不可见
                for (LinePoint p : line.getPoints()) {
                    p.setRadius(6);
                    p.setTextVisible(false);// 如果设为true，之前选中的点的值不会消失
                }
                point.setRadius(10);
                point.setTextVisible(true);// 将选中的点设置成可见
                point.setText(getIndicate() + ":" + String.valueOf(point.getY())
                        + unit());// 设置要显示的值
                if (string_x.length <= 1) {
                    if (point.getX() == 1 * horSizeofDots) {// 往后移一个点
                        point.setTextAlign(TextAlign.RIGHT | TextAlign.TOP);
                    } else if (point.getX() == (string_x.length - 1) * horSizeofDots) {
                        point.setTextAlign(TextAlign.LEFT | TextAlign.TOP);
                    } else {
                        point.setTextAlign(TextAlign.TOP);// 设置字显示的位置
                    }
                } else {
                    if (point.getX() == 0) {
                        point.setTextAlign(TextAlign.RIGHT | TextAlign.TOP);
                    } else if (point.getX() == (string_x.length - 1) * horSizeofDots) {
                        point.setTextAlign(TextAlign.LEFT | TextAlign.TOP);
                    } else {
                        point.setTextAlign(TextAlign.TOP);// 设置字显示的位置
                    }
                }
                point.getTextPaint().setColor(0xffff6701);// 设置显示的字体颜色
            }
        });
    }


    public void refreshMydata(int month[], int data[]) {
        myData = data;
        myMonth = month;
        L.e("line___________11myData.length=="+myData.length);
        L.e("line___________11myMonth.length==" + myMonth.length);
        if (chart != null) {
            L.e("----------refreshMydata----------");
            chart.removeAllLines();
            generateLineOptions(ages);
        }
    }

    /**
     * 创建一条折线
     *
     * @param startX 折线X方向的最小值
     * @param endX   折线X方向的最大值
     * @param step   两点之间的水平方向的间隔距离
     * @return
     */
    private Line generateLine(int startX, int endX, int step, int[] Y) {
        Line line = new Line(getActivity());
        for (int i = startX; i <= endX; i += step) {
            line.addPoint(new LinePoint(getActivity(), i, Y[i / step]));
        }
        return line;
    }

    private Line generateMyLine(int startX, int endX, int step, int ages[], int[] X, int[] Y) {
        Line line = new Line(getActivity());
        int[] mX = new int[X.length];
        int[] kX = new int[ages.length];
        for (int i = 0; i <ages.length ; i++) {
            kX[i] = i*step;
        }

        for (int i = 0; i < ages.length - 1; i++) {

            for (int j = 0; j < X.length; j++) {
                if (X[j] > ages[i] && X[j] <= ages[i + 1]) {
                    L.e("X[j]=========="+X[j]);
                    double a=X[j] - ages[i];
                    double b=ages[i + 1] - ages[i];
                    double c=kX[i+1]-kX[i];
                    double  scale = a / b;

                    double d=scale*c+kX[i];
                    L.e("d=========="+d);
                    mX[j]= (int)d;
                }

            }

        }

        for (int i = 0; i < X.length; i++) {

            line.addPoint(new LinePoint(getActivity(), mX[i], Y[i]));
        }


        return line;
    }
}
