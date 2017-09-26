package com.headlth.management.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.GridView;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.adapter.ImageBucketAdapter;
import com.headlth.management.entity.ImageBucket;
import com.headlth.management.utils.AlbumHelper;


import java.util.List;
import java.io.Serializable;


import android.content.Intent;

import android.graphics.BitmapFactory;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/8/4.
 */
@ContentView(R.layout.activity_image_bucket)
public class PhotoalbumActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;




    List<ImageBucket> dataList;
    GridView gridView;
    ImageBucketAdapter adapter;// 自定义的适配器
    AlbumHelper helper;
    static Activity activity;
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    public static Bitmap bimap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("相册");
        activity=this;
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // /**
        // * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
        // */
        // dataList = new ArrayList<Entity>();
        // for(int i=-0;i<10;i++){
        // Entity entity = new Entity(R.drawable.picture, false);
        // dataList.add(entity);
        // }
        dataList = helper.getImagesBucketList(false);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(PhotoalbumActivity.this, dataList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
                 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
                 */
                // if(dataList.get(position).isSelected()){
                // dataList.get(position).setSelected(false);
                // }else{
                // dataList.get(position).setSelected(true);
                // }
                /**
                 * 通知适配器，绑定的数据发生了改变，应当刷新视图
                 */
                // adapter.notifyDataSetChanged();
                Intent intent = new Intent(PhotoalbumActivity.this, ImageGridActivity.class);
                intent.putExtra("text",getIntent().getStringExtra("text"));
                intent.putExtra(PhotoalbumActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
                startActivity(intent);
            }

        });
    }
}
