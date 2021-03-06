package com.example.may.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class HchoActivity extends AppCompatActivity {
    private TextView tv_1;
    private TextView tv_2;
    private SQLiteOpenHelper dbHelper;

    private LineChartView lineChart;
    private static Context context;

    private ArrayList<String> xRay;
    private ArrayList<Double> mPoint;



    private float[][] randomNumbersTab;
    private String[] time = null;//X轴的标注
    private Double[] scoreArray = null;//图表的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    public HchoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcho);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        xRay=new ArrayList<>();
        mPoint=new ArrayList<>();

        dbHelper = new MyDatabaseHelper((Context) this, "data.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Hcho",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                double content = cursor.getDouble(cursor.getColumnIndex("content"));
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                //将时间转化成yyyy-MM-dd HH:mm:ss形式
                Date nowTime = new Date(time);
                SimpleDateFormat sdFormatter = new SimpleDateFormat("HH:mm:ss");
                String retStrFormatNowDate = sdFormatter.format(nowTime);

                Log.d("Mainactivity", "时间"+retStrFormatNowDate);
                Log.d("Mainactivity", "甲醛浓度"+content);

                xRay.add(retStrFormatNowDate);
                mPoint.add(content);

            }while(cursor.moveToNext());
        }
        cursor.close();

        tv_1 = findViewById(R.id.tv_1);
        double ho = getIntent().getDoubleExtra("hcho",0);
        tv_1.setText(ho+" mg/L");

        if( ho < 40){
            tv_2.setText("空气清新，甲醛含量很低");
        }else if(ho < 80){
            tv_2.setText("空气清新，甲醛含量符合国家标准");
        }else if(ho < 120){
            tv_2.setText("空气中弥漫着甲醛的味道，请开窗通风");
        }else if(ho < 200){
            tv_2.setText("甲醛含量超标！");
        }else {
            tv_2.setText("甲醛含量严重超标，请及时采取有效措施");
        }

        lineChart = (LineChartView) findViewById(R.id.chart);
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化

    }
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#1bd1a5"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
        line.setStrokeWidth(1);//线条的粗细，默认是3
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        //		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.clear();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        //	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#000000"));//黑色

        axisX.setName("时间");  //表格名称
        axisX.setTextSize(12);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线
//
//

        Axis axisY = new Axis();  //Y轴
        axisY.setName("含量");
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.parseColor("#000000"));//黑色
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    //
//    /**
//     * X 轴的显示
//     */
    private void getAxisXLables() {
        mAxisXValues.clear();
        for (int i = 0; i < xRay.size(); i++) {
            AxisValue mValue=new AxisValue(i).setLabel(xRay.get(i));
            mAxisXValues.add(mValue);
        }
//        mAxisXValues.addAll(xRay);
    }

    //    /**
//     * 图表的每个点的显示
//     */
    private void getAxisPoints() {
        mPointValues.clear();
        for (int i = 0; i < mPoint.size(); i++) {
            double d=mPoint.get(i);
            mPointValues.add(new PointValue((float) i,  (float)d));
        }
    }
}


