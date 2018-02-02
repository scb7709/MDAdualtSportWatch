package com.headlth.management.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.headlth.management.entity.AnimotionCoordinate;

public class Bimp {
	private Bimp() {
	}

	private static Bimp bitmp;

	public static int max = 0;
	public static boolean act_bool = true;
	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<AnimotionCoordinate> animotionCoordinate = new ArrayList<AnimotionCoordinate>();

	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public List<String> url = new ArrayList<String>();

	public static Bimp getInstance() {
		if (bitmp == null) {
			synchronized (Bimp.class) {
				if (bitmp == null) {
					bitmp = new Bimp();
				}
			}
		}
		return bitmp;
	}

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static Bitmap CompressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 50, baos);
		if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}


	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}


	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}


	public interface OnSaveSuccessListener {
		void onSuccess(String filepath);
	}

	/**
	 * 从本地path中获取bitmap，压缩后保存小图片到本地
	 *
	 * @param path 图片存放的路径
	 * @return 返回压缩后图片的存放路径
	 */

	public static void saveBitmap(String path, String fileName, OnSaveSuccessListener onSaveSuccessListener) {

		String compressdPicPath = "";

//      ★★★★★★★★★★★★★★重点★★★★★★★★★★★★★
      /*  //★如果不压缩直接从path获取bitmap，这个bitmap会很大，下面在压缩文件到100kb时，会循环很多次，
        // ★而且会因为迟迟达不到100k，options一直在递减为负数，直接报错
        //★ 即使原图不是太大，options不会递减为负数，也会循环多次，UI会卡顿，所以不推荐不经过压缩，直接获取到bitmap
        Bitmap bitmap=BitmapFactory.decodeFile(path);*/
//      ★★★★★★★★★★★★★★重点★★★★★★★★★★★★★
		Bitmap bitmap = decodeSampledBitmapFromPath(path, 720, 1280);
		if (bitmap == null) {
			onSaveSuccessListener.onSuccess(path);
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

/* options表示 如果不压缩是100，表示压缩率为0。如果是70，就表示压缩率是70，表示压缩30%; */
		int options = 100;
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		} catch (Exception e) {
			onSaveSuccessListener.onSuccess(path);
			try {
				baos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}


		while (baos.toByteArray().length / 1024 > 200) {
// 循环判断如果压缩后图片是否大于500kb继续压缩

			baos.reset();
			options -= 10;
			if (options < 11) {//为了防止图片大小一直达不到200kb，options一直在递减，当options<0时，下面的方法会报错
				// 也就是说即使达不到200kb，也就压缩到10了
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
				break;
			}
// 这里压缩options%，把压缩后的数据存放到baos中
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}

		String mDir = Environment.getExternalStorageDirectory() + "/maidong/image";
		File dir = new File(mDir);
		if (!dir.exists()) {
			dir.mkdirs();//文件不存在，则创建文件
		}
		File file = new File(mDir, fileName + ".png");
		FileOutputStream fOut = null;

		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
			onSaveSuccessListener.onSuccess(file.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据图片要显示的宽和高，对图片进行压缩，避免OOM
	 *
	 * @param path
	 * @param width  要显示的imageview的宽度
	 * @param height 要显示的imageview的高度
	 * @return
	 */
	private static Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {

//      获取图片的宽和高，并不把他加载到内存当中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = caculateInSampleSize(options, width, height);
//      使用获取到的inSampleSize再次解析图片(此时options里已经含有压缩比 options.inSampleSize，再次解析会得到压缩后的图片，不会oom了 )
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;

	}

	/**
	 * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
	 *
	 * @param options
	 * @param reqWidth  要显示的imageview的宽度
	 * @param reqHeight 要显示的imageview的高度
	 * @return
	 * @compressExpand 这个值是为了像预览图片这样的需求，他要比所要显示的imageview高宽要大一点，放大才能清晰
	 */
	private static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;

		int inSampleSize = 1;

		if (width >= reqWidth || height >= reqHeight) {

			int widthRadio = Math.round(width * 1.0f / reqWidth);
			int heightRadio = Math.round(width * 1.0f / reqHeight);

			inSampleSize = Math.max(widthRadio, heightRadio);

		}

		return inSampleSize;
	}

}
