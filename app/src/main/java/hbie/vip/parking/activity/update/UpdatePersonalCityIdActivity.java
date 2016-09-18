package hbie.vip.parking.activity.update;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import hbie.vip.parking.R;
import hbie.vip.parking.activity.wheel.WheelBaseActivity;
import hbie.vip.parking.activity.wheel.widget.OnWheelChangedListener;
import hbie.vip.parking.activity.wheel.widget.WheelView;
import hbie.vip.parking.activity.wheel.widget.adapter.ArrayWheelAdapter;
import hbie.vip.parking.app.ExitApplication;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.net.NetUtil;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.ToastUtils;

/**
 * 修改城市
 *
 * @author Administrator
 *
 */
public class UpdatePersonalCityIdActivity extends WheelBaseActivity implements View.OnClickListener, OnWheelChangedListener {
    private RelativeLayout back;
    private Button tv_submit;
    private WheelView mViewProvince, mViewCity, mViewDistrict;
    private Context mContext;
    private UserInfo user;
    private String TAG="UpdatePersonalCityIdActivity";
    private static final int UPD_KEY = 1;
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPD_KEY:
                    if (msg.obj != null) {
                        LogUtils.i("city", "查询标签" + msg.obj);
                        try {
                            String result = (String) msg.obj;
                            JSONObject json = new JSONObject(result);
                            String state = json.getString("state");
                            if (state.equals("1")) {
                                ToastUtils.ToastShort(mContext, "更新成功");
//                                user.setUserCity(mCurrentProviceName);
//                                user.setUserCityId(mCurrentId);
                                user.writeData(mContext);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_personal_city);
        ExitApplication.getInstance().addActivity(this);
        mContext = this;
        initView();
    }

    /**
     * 初始化并设置监听
     */
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.relativeLayout_update_personal_city_back);
        tv_submit = (Button) findViewById(R.id.bt_update_personal_city_submit);
        mViewProvince = (WheelView) findViewById(R.id.wheel_update_personal_city_province);
        mViewCity = (WheelView) findViewById(R.id.wheel_update_personal_city_city);
        mViewDistrict = (WheelView) findViewById(R.id.wheel_update_personal_city_district);
        user = new UserInfo();
        setUpListener();
        initProvinceDatas();
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(UpdatePersonalCityIdActivity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayout_update_personal_city_back:
                finish();// 返回上一层
                break;
            case R.id.bt_update_personal_city_submit:
                UpdateCityId(mCurrentId);
                break;
        }
    }

    // 更新地址
    private void UpdateCityId(String Id) {
        if (NetUtil.isConnnected(mContext) == true) {
//            new UserRequest(mContext, handler).updateUserCity(Id, UPD_KEY);
        } else {
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentId = mIdDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        Log.i(TAG, String.valueOf(mProvinceDatas.length));
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }
}

