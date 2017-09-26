package com.headlth.management.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.utils.ImageProcessingUtils;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by abc on 2016/11/24.
 */

@ContentView(R.layout.activity_getheart)//复用我的处方布局
public class GetHeartActivity extends BaseActivity {





    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private Button view_publictitle_back;

    @ViewInject(R.id.activity_getheart_know_layout)
    private LinearLayout activity_getheart_know_layout;
    @ViewInject(R.id.activity_getheart_heartrateimage)
    private LinearLayout activity_getheart_heartrateimage;

    @ViewInject(R.id.activity_getheart_heartrate_layout)
    private RelativeLayout activity_getheart_heartrate_layout;


    @ViewInject(R.id.activity_getheart_onoff)
    private ImageView activity_getheart_onoff;
    @ViewInject(R.id.activity_getheart_heartrate)
    private TextView activity_getheart_heartrate;

    @ViewInject(R.id.getheart_preview)
    private SurfaceView getheart_preview;





    boolean Switch;//开关


    public static Activity activity;



    private PackageManager packageManager ;
    private int permission;

    private Timer timer = new Timer();
    private Timer timertime = new Timer();
    private TimerTask task;
    private static int gx;
    private static int j;

    private static double flag = 1;
    private Handler handler;
    private String title = "pulse";
    private XYSeries series;
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView chart;
    private XYMultipleSeriesRenderer renderer;
    private Context context;
    private int addX = -1;
    double addY;
    int[] xv = new int[300];
    int[] yv = new int[300];
    int[] hua = new int[]{9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7, 6, 7, 8, 9, 10, 11, 10, 10};

    //	private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static PowerManager.WakeLock wakeLock = null;
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];


    public enum TYPE {
        GREEN, RED
    }

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;




    private String HeartRate ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);

        activity = this;

        init();

    }

    private void init() {
        view_publictitle_title.setText("获取心率");
        packageManager = getPackageManager();
        permission = packageManager.checkPermission("android.permission.CAMERA", "com.headlth.management");
        if (PackageManager.PERMISSION_GRANTED != permission) {

            Toast.makeText(GetHeartActivity.this, "没有相机权限", Toast.LENGTH_SHORT).show();


        } else {

Log.i("DDDDDDDDD","cccc");
            //		曲线
            context = getApplicationContext();



            //这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线
            series = new XYSeries(title);

            //创建一个数据集的实例，这个数据集将被用来创建图表
            mDataset = new XYMultipleSeriesDataset();

            //将点集添加到这个数据集中
            mDataset.addSeries(series);

            //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
            int color = Color.GREEN;
            PointStyle style = PointStyle.CIRCLE;
            renderer = buildRenderer(color, style, true);

            //设置好图表的样式
            setChartSettings(renderer, "X", "Y", 0, 300, 4, 16, Color.WHITE, Color.WHITE);

            //生成图表
            chart = ChartFactory.getLineChartView(context, mDataset, renderer);

            //将图表添加到布局中去
            activity_getheart_heartrateimage.addView(chart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));


		/*	       thread = new Thread(){
               public void arrayList(int u) {
	    		   ArrayList arrayList = new ArrayList();
	    		   arrayList.add(HardwareControler.readADC());
	   		}
	       };*/
            //这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what==1)
                        updateChart();
                    super.handleMessage(msg);
                }
            };

            task = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
         /*   TimerTask  tasktime = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            };*/

            timer.schedule(task, 1, 20);           //曲线
         //   timertime.schedule(tasktime, 1000, 1000);




            previewHolder = getheart_preview.getHolder();
            previewHolder.addCallback(surfaceCallback);
            previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        if (PackageManager.PERMISSION_GRANTED != permission) {
            Toast.makeText(GetHeartActivity.this, "没有相机权限", Toast.LENGTH_SHORT).show();

        } else {
            wakeLock.acquire();
            camera = Camera.open();
            startTime = System.currentTimeMillis();
        }
    }
    @Event(value = {R.id.view_publictitle_back
            , R.id.activity_getheart_know
            , R.id.activity_getheart_onoff

    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                Intent intent=new Intent().putExtra("heartrate", HeartRate);
                intent.setAction("getHeartRateReceiver");
                sendBroadcast(intent);
                finish();
                break;
            case R.id.activity_getheart_know:
                activity_getheart_know_layout.setVisibility(View.GONE);
                activity_getheart_heartrateimage.setVisibility(View.VISIBLE);
                activity_getheart_heartrate_layout.setVisibility(View.VISIBLE);
                Switch = true;
                activity_getheart_onoff.setImageResource(R.mipmap.heartrate_on);
                break;
            case R.id.activity_getheart_onoff:
                if (Switch) {
                    activity_getheart_onoff.setImageResource(R.mipmap.heartrate_off);
                } else {
                    activity_getheart_onoff.setImageResource(R.mipmap.heartrate_on);
                }
                Switch = !Switch;
                break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    //	曲线
    @Override
    public void onDestroy() {
        //当结束程序时关掉Timer
        timer.cancel();
        super.onDestroy();
    }


    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.RED);
//		r.setPointStyle(null);
//		r.setFillPoints(fill);
        r.setLineWidth(1);
        renderer.addSeriesRenderer(r);
        return renderer;
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,
                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        //有关对图表的渲染可参看api文档
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.GREEN);
        renderer.setXLabels(20);
        renderer.setYLabels(10);
        renderer.setXTitle("Time");
        renderer.setYTitle("mmHg");
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setPointSize((float) 3);
        renderer.setShowLegend(false);
    }

    private void updateChart() {


        //设置好下一个需要增加的节点
        //    	addX = 10;
        //addY = (int)(Math.random() * 90 + 50);
        //		addY = (int)(HardwareControler.readADC());
        //    	addY=10+addY;
        //    	if(addY>1400)
        //    		addY=10;
        if (flag == 1)
            addY = 10;
        else {
//			addY=250;
            flag = 1;
            if (gx < 200) {
                if (hua[20] > 1) {
                    //  timee=0;
                    if(Switch){
                        Toast.makeText(GetHeartActivity.this, "请用您的指尖盖住摄像头镜头！", Toast.LENGTH_SHORT).show();
                    }
                    hua[20] = 0;
                }
                hua[20]++;
                return;
            } else {


                hua[20] = 10;
            }
            j = 0;

        }
        if (j < 20) {
            addY = hua[j];
            j++;
        }

        //移除数据集中旧的点集
        mDataset.removeSeries(series);

        //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
        int length = series.getItemCount();
        int bz = 0;
        //		addX = length;
        if (length > 300) {
            length = 300;
            bz = 1;
        }
        addX = length;
        //将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
        for (int i = 0; i < length; i++) {
            xv[i] = (int) series.getX(i) - bz;
            yv[i] = (int) series.getY(i);
        }

        //点集先清空，为了做成新的点集而准备
        series.clear();
        mDataset.addSeries(series);
        //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
        //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
        series.add(addX, addY);
        for (int k = 0; k < length; k++) {
            series.add(xv[k], yv[k]);
        }


        //在数据集中添加新的点集
        //		mDataset.addSeries(series);

        //视图更新，没有这一步，曲线不会呈现动态
        //如果在非UI主线程中，需要调用postInvalidate()，具体参考api
        chart.invalidate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private  Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null)
                throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null)
                throw new NullPointerException();
            if (!processing.compareAndSet(false, true))
                return;
            int width = size.width;
            int height = size.height;
            //图像处理
            int imgAvg = ImageProcessingUtils.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            gx = imgAvg;
           // text1.setText("平均像素值是" + String.valueOf(imgAvg));
            //像素平均值imgAvg,日志
            //			Log.i(TAG, "imgAvg=" + imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    flag = 0;
                  //  text2.setText("脉冲数是               " + String.valueOf(beats));
                    //					Log.e(TAG, "BEAT!! beats=" + beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize)
                averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                //				image.postInvalidate();
            }
//获取系统结束时间（ms）
            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 2) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180 || imgAvg < 200) {
                    //获取系统开始时间（ms）
                    startTime = System.currentTimeMillis();
                    //beats心跳总数
                    beats = 0;
                    processing.set(false);
                    return;
                }
                //				Log.e(TAG, "totalTimeInSecs=" + totalTimeInSecs + " beats="+ beats);
                if (beatsIndex == beatsArraySize)
                    beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;
                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                //	text.setText("心率"+String.valueOf(beatsAvg)+"  zhi:"+String.valueOf(beatsArray.length) +"    "+String.valueOf(beatsIndex)+"    "+String.valueOf(beatsArrayAvg)+"    "+String.valueOf(beatsArrayCnt));

              if (Switch){
                  activity_getheart_heartrate.setText(String.valueOf(beatsAvg));
                  HeartRate=String.valueOf(beatsAvg);
              }

//获取系统时间（ms）
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };

    private  SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                //				Log.e("PreviewDemo-surfaceCallback","Exception in setPreviewDisplay()", t);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                //				Log.d(TAG, "Using width=" + size.width + " height="	+ size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height,
                                                      Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea)
                        result = size;
                }
            }
        }
        return result;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            Intent intent=new Intent().putExtra("heartrate", HeartRate);
            intent.setAction("getHeartRateReceiver");
            sendBroadcast(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
