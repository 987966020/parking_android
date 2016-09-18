package hbie.vip.parking.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hbie.vip.parking.R;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.utils.IntentUtils;

/**
 * 欢迎界面
 * Created by mac on 16/5/26.
 */
public class SplashActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private Context mContext;
    private ProgressDialog dialog;
    private static final int sleepTime = 3000;
    private SharedPreferences sp;
    boolean isFirstLoc = true;// 是否首次定位
//    private LocationClient mLocationClient = null;
    private String cityId;
    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 引导图片资源
    private static final int[] pics = { R.mipmap.nsp1, R.mipmap.nsp2, R.mipmap.nsp3 };
    private LinearLayout ll_point;
    private TextView tv;
    // 底部小点的图片
    private ImageView[] points;
    // 记录当前选中位置
    private int currentIndex;
    private static Boolean isExit = false;
    private long firstTime = 0;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view = View.inflate(this, R.layout.activity_flash, null);
        ExitApplication.getInstance().addActivity(this);
        setContentView(view);

        mContext = this;
        ExitApplication.getInstance().addActivity(this);
        sp = getSharedPreferences("list", MODE_PRIVATE);
        initView();
//        AppInfo appInfo = new AppInfo();
//        if (appInfo.isFristLogin(mContext)) {
        initData();
//        } else {
        // 动画
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        view.startAnimation(animation);
//            LoginToCanKa();
//        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager_flash);
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);
        viewPager.setVisibility(View.GONE);
        ll_point = (LinearLayout) findViewById(R.id.ll_flash);
        ll_point.setVisibility(View.GONE);
        tv = (TextView) findViewById(R.id.tv_flash);
        tv.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            // 防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            // 加载图片资源
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        viewPager.setVisibility(View.VISIBLE);
        // 设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
//                    AppInfo appInfo = new AppInfo();
//                    PackageManager pm = mContext.getPackageManager();
//                    PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
//                    String currentVersion = "" + pi.versionCode;
//                    appInfo.setLastVersionCode(currentVersion);
//                    appInfo.writeData(mContext);
//                    UserInfo user = new UserInfo();
//                    user.readData(mContext);
//                    if (user.isLogined()) {// 已登录
//                        dialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
//                        dialog.setMessage("正在登录...");
//                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        dialog.show();
//                        LoginToCanKa();
//                    } else {// 未登录
                    IntentUtils.getIntent(SplashActivity.this, LoginActivity.class);
                    finish();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 初始化底部小点
        initPoint();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {
        points = new ImageView[pics.length];
        ll_point.setVisibility(View.VISIBLE);
        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) ll_point.getChildAt(i);
            // 默认都设为灰色
            points[i].setEnabled(true);
            // 给每个小点设置监听
            points[i].setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    /**
     * 当前页面滑动时调用
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurDot(arg0);

    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
//        tv.setVisibility(View.VISIBLE);
        if (positon == pics.length - 1) {
            ll_point.setVisibility(View.GONE);
//            tv.setText("立即体验");
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
//            tv.setVisibility(View.VISIBLE);
//            tv.setText(">>");
            ll_point.setVisibility(View.VISIBLE);
        }
    }

//    private void LoginToCanKa() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                UserInfo user = new UserInfo(mContext);
//                if (user.isLogined()) {// 已登录
//                    if (!DemoHXSDKHelper.getInstance().isLogined()) {
//                        // ** 免登陆情况 加载所有本地群和会话
//                        // 不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
//                        // 加上的话保证进了主页面会话和群组都已经load完毕
//                        long start = System.currentTimeMillis();
//                        EMChatManager.getInstance().loadAllConversations();
//                        long costTime = System.currentTimeMillis() - start;
//                        // 等待sleeptime时长
//                        if (sleepTime - costTime > 0) {
//                            try {
//                                Thread.sleep(sleepTime - costTime);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } else {
//                        try {
//                            Thread.sleep(sleepTime);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
////					IntentUtils.getIntent(SplashActivity.this, HomeActivity.class);
////					if (dialog!=null) {
////						dialog.dismiss();
////					}
////					finish();
//                    new GetAllFoucsKaListTask().executeOnExecutor(ThreadPoolUtils_Net.threadPool);
//                } else {// 未登录
//                    IntentUtils.getIntent(SplashActivity.this, LoginActivity.class);
//                }
//            }
//        }, sleepTime);
//    }
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_LONG).show();  //提示消息
            firstTime = secondTime;// 更新firstTime
            return true;
        } else {
            System.exit(0);
        }
    }
    return super.onKeyDown(keyCode, event);
}



}