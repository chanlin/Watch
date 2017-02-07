package com.jajale.watch.image;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jajale.watch.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application{

	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions options;
	public RequestQueue mQueue;
	public static MyApplication app;
//	public static EventBusAllData mEventBus;
	public static String loginkey = "fa8476c5-51ef-4fec-82b1-296f99e42360";//网络请求数据时所需要的
	public static String userId = "16544";//网络请求数据时所需要的

	@Override
	public void onCreate() {
		super.onCreate();
		this.app = this;
//		mEventBus = new EventBusAllData();
		
		mQueue = Volley.newRequestQueue(this);
		initImageLoader(getApplicationContext());

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_doc)// 加载等待 时显示的图片
				.showImageForEmptyUri(R.drawable.ic_dir)// 加载数据为空时显示的图片
				.showImageOnFail(R.drawable.ic_doc)// 加载失败时显示的图片
				.cacheInMemory().cacheOnDisc() /**
				 * .displayer(new
				 * RoundedBitmapDisplayer(20))
				 **/
				.build();
	}

	//EventBus传值
//	public EventBusAllData getEventBus(){
//		return this.mEventBus;
//	}
	
	//Volley网络请求的队列
	public RequestQueue getQueue(){
        return this.mQueue;
    }
	
	public static MyApplication getApp(){
		return app;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not
																				// common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

}
