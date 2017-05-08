package com.headlth.management.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.headlth.management.R;
import com.headlth.management.adapter.ImageGridAdapter;
import com.headlth.management.entity.ImageItem;
import com.headlth.management.utils.AlbumHelper;
import com.headlth.management.utils.Bimp;
import com.headlth.management.utils.DiskBitmap;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_image_grid)
public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	// ArrayList<Entity> dataList;//
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;//
	AlbumHelper helper;
	Button bt;
	@ViewInject(R.id.activity_prescritiondetials_back)
	private RelativeLayout photo_bt_back2;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多可分享9张图片", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
					/*Intent intent = new Intent(ImageGridActivity.this, PublishedActivity.class);
					startActivity(intent);
					Bimp.act_bool = false;*/
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.getInstance().url.size() < 9) {
						Bimp.getInstance().url.add(list.get(i));
						Bimp.getInstance().bmp.add(DiskBitmap.getDiskBitmap(list.get(i),ImageGridActivity.this));
					}
				}
				startActivity(new Intent(ImageGridActivity.this,ShareActivity.class).putExtra("share","nofirst").putExtra("text",getIntent().getStringExtra("text")));
				ShareActivity.activity.finish();
				PhotoalbumActivity.activity.finish();
				finish();
			}

		});
	}
	private void initView() {
		photo_bt_back2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				//adapter.notifyDataSetChanged();
			}

		});

	}
}
