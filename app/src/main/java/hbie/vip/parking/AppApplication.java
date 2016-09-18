package hbie.vip.parking;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import hbie.vip.parking.activity.wheel.ProvinceModel;
import hbie.vip.parking.bean.AppInfo;
import hbie.vip.parking.bean.CityInfo;
import hbie.vip.parking.db.PlaceManager;
import hbie.vip.parking.task.FindAllCityTask;
import hbie.vip.parking.task.constants.MessageConstants;
import hbie.vip.parking.task.pool.ThreadPoolUtils_Net;
import hbie.vip.parking.task.tool.ThreadPoolUtils_db;
import hbie.vip.parking.utils.Instruction_Utils;
import hbie.vip.parking.utils.LogUtils;


/**
 * Created by mac on 16/5/13.
 */
public class AppApplication extends Application implements ReceiverInterface {
    public static Context applicationContext;
    private static AppApplication instance;
    private AppInfo appInfo;
    private IntentFilter myIntentFilter;
    public static ArrayList<String> selectedTags = new ArrayList<String>();
    public static int systemCount;
    public static int informationCount;
    public static int professionCircleCount;
    public static ArrayList<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
    public static CityInfo province, city, country;
    /**
     * 把全国的省市区的信息以json的格式保存
     */
    public static JSONObject mJsonObj;
    @Override
    public void onCreate() {
        LogUtils.i("AllCity", "Application－－－begin" );
        super.onCreate();
        applicationContext = this;
        instance = this;

        /**
         * 以下这两句话的意思是 全局监听 异常 捕获的类
         */
//		 CrashHandler crashHandler = CrashHandler.getInstance();
//		 crashHandler.init(getApplicationContext());
// 注册广播
        regiserRadio(MessageConstants.REFRESHS_PROJECTION);

        appInfo = new AppInfo();
        appInfo.readData(applicationContext);
        LogUtils.i("AllCity", "Application" + appInfo.getUpdateCity());
        if (appInfo.getUpdateCity()) {// 需要更新城市Id
            // 获取同步所有城市
            FindAllCityTask findAllCityTask = new FindAllCityTask(getApplicationContext());
            findAllCityTask.executeOnExecutor(ThreadPoolUtils_Net.threadPool);
        }

        initImageLoader(getApplicationContext());

    }

    protected boolean isContains(ArrayList<String> List, String string) {
        if (List != null) {
            for (int i = 0; i < List.size(); i++) {
                if (List.get(i).equals(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** 获取Application */
    public static AppApplication getInstance() {
        return instance;
    }


    /** 初始化ImageLoader */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "parking/Cache");// 获取到缓存的目录地址
        LogUtils.d("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
                // max width,max height，即保存的每个缓存文件的最大长宽default=device screen dimensions
                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
                        // Can slow ImageLoader, use it carefully (Better don't use
                        // it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 1).tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                        // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
                        // 1024))
                        // You can pass your own memory cache
                        // .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCache(new WeakMemoryCache())
                        // implementation你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(30 * 1024 * 1024)
                        // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5加密
                        // .discCacheFileNameGenerator(new
                        // HashCodeFileNameGenerator())// 将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(1000) // 缓存的File数量
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                        // connectTimeout (5s), readTimeout(30s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }
    private void synchronousAllCity() {
        // 发送同步所有城市 的指令
        Instruction_Utils.sendInstrustion(getApplicationContext(), Instruction_Utils.SYNCHRONOUS_ALL_CITY);
    }

    @Override
    public void regiserRadio(String[] actions) {
        Log.i("AppApplication","regiserRadio");
        // TODO Auto-generated method stub
        myIntentFilter = new IntentFilter();
        for (int i = 0; i < actions.length; i++) {
            myIntentFilter.addAction(actions[i]);
        }
        // 注册广播
        getApplicationContext().registerReceiver(IntentReceicer, myIntentFilter);

    }

    //    @Override
//    public void regiserRadio(String[] actions) {
//
//    }
//
//    @Override
//    public void regiserRadio(String[] actions) {
//        // TODO Auto-generated method stub
//        myIntentFilter = new IntentFilter();
//        for (int i = 0; i < actions.length; i++) {
//            myIntentFilter.addAction(actions[i]);
//        }
//        // 注册广播
//        getApplicationContext().registerReceiver(IntentReceicer, myIntentFilter);
//    }
    @Override
    public void destroyRadio() {
        // TODO Auto-generated method stub
        // 注销广播
        getApplicationContext().unregisterReceiver(IntentReceicer);
    }
    private BroadcastReceiver IntentReceicer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dealWithRadio(intent);
        }
    };
    @Override
    public void dealWithRadio(Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (action.equals(MessageConstants.REFRESH_CITY)) {
            ReadProvinceFromDBTask readProvinceFromDBTask = new ReadProvinceFromDBTask();
            readProvinceFromDBTask.executeOnExecutor(ThreadPoolUtils_db.threadPool);
        }
    }
    private class ReadProvinceFromDBTask extends AsyncTask<String, Integer, ArrayList<ProvinceModel>> {

        @Override
        protected ArrayList<ProvinceModel> doInBackground(String... params) {
            LogUtils.i("City", "begin广播数据获取" );
            ArrayList<ProvinceModel> provinces = new ArrayList<ProvinceModel>();
            PlaceManager placeManager = new PlaceManager();
            provinces = placeManager.GetAllDates(getApplicationContext());
            return provinces;
        }

        @Override
        protected void onPostExecute(ArrayList<ProvinceModel> result) {
            super.onPostExecute(result);
            provinceList = result;
            LogUtils.i("City", "广播数据获取" + provinceList.size());
        }
    }
}

