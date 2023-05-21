package com.nxnu.sjxy.vision.demo.java;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.mlkit.vision.demo.R;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class AfterTrainingActivity extends AppCompatActivity {


    private RadarChart radar;
    List<RadarEntry> list;
    private int[] analyzeData = new int[]{0,0,0,0,0,0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_training);
        // 获取传递来的分析数据
        Bundle bundle = this.getIntent().getExtras();
        for (String item : (String[]) bundle.get("analyzeData")){
            switch (item.substring(0,7)){
                case "leftElb":
                    analyzeData[0]++;
                    analyzeData[7]++;
                    break;
                case "rightEl":
                    analyzeData[1]++;
                    analyzeData[7]++;
                    break;
                case "leftSho":
                    analyzeData[2]++;
                    analyzeData[7]++;
                    break;
                case "rightSh":
                    analyzeData[3]++;
                    analyzeData[7]++;
                    break;
                case "leftKne":
                    analyzeData[4]++;
                    analyzeData[7]++;
                    break;
                case "rightKn":
                    analyzeData[5]++;
                    analyzeData[7]++;
                    break;
                default:
                    analyzeData[6]++;
                    analyzeData[7]++;
            }
        }

        CircleProgressView circleProgressView = findViewById(R.id.circleView);

        // 创建一个动画对象
        int score = (int) (((float)analyzeData[6]) / ((float)analyzeData
        [7]) * 100.0);
        ValueAnimator animator = ValueAnimator.ofFloat(0, score);
        animator.setDuration(3000); // 设置动画的持续时间，单位为毫秒

        // 设置动画更新监听器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                circleProgressView.setValue(progress); // 更新进度值
            }
        });

        // 启动动画
        animator.start();

        radar = (RadarChart) findViewById(R.id.radar);
        radar.setSkipWebLineCount(7);
        radar.setWebLineWidth(0);
        list=new ArrayList<>();

        list.add(new RadarEntry(analyzeData[0]));
        list.add(new RadarEntry(analyzeData[1]));
        list.add(new RadarEntry(analyzeData[2]));
        list.add(new RadarEntry(analyzeData[3]));
        list.add(new RadarEntry(analyzeData[4]));
        list.add(new RadarEntry(analyzeData[5]));

        XAxis xAxis=radar.getXAxis();
        xAxis.setTextColor(Color.BLACK);//X轴字体颜色
        xAxis.setTextSize(10);     //X轴字体大小
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final String[] mActivities = new String[]{"左手肘", "右手肘", "左肩膀", "右肩膀", "左膝盖", "右膝盖"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        RadarDataSet radarDataSet=new RadarDataSet(list,"部位错误次数统计");

        radarDataSet.setColor(Color.RED);
        // radarDataSet置填充颜色
        radarDataSet.setFillColor(Color.RED);
        // radarDataSet置填充透明度
        radarDataSet.setFillAlpha(40);
        // radarDataSet置启用填充
        radarDataSet.setDrawFilled(true);
        RadarData radarData=new RadarData(radarDataSet);
        radar.setData(radarData);

        //Y轴最小值不设置会导致数据中最小值默认成为Y轴最小值
        radar.getYAxis().setAxisMinimum(0);
        radar.getYAxis().setDrawLabels(false);
        radar.getYAxis().setLabelCount(5);

    }

}