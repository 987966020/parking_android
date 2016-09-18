package hbie.vip.parking.net.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hbie.vip.parking.bean.PlaceInfo;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.net.request.constant.CityNetConstant;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;

/**
 * Created by mac on 16/6/1.
 */

public class CityRequest {

    private Context mContext;
    private Handler handler;
    private String TAG = "CityRequest";

    public CityRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;
    }

    public CityRequest(Context context) {
        super();
        this.mContext = context;
    }

    /**
     * 获取全部城市
     *
     * @param KEY
     */
    public void GetAllCity(final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String result = NetBaseUtils.getResponseForPost(CityNetConstant.NET_GET_ALL_CITY, params, mContext);
                LogUtils.i("AllCity", "获取全部城市--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }

    /**
     * 获取全部城市
     */
    public ArrayList<PlaceInfo> doFindAllCity() {
        // TODO Auto-generated method stub
        ArrayList<PlaceInfo> places = new ArrayList<PlaceInfo>();
        String result = NetBaseUtils.getResponseForGet(CityNetConstant.NET_GET_ALL_CITY, mContext);
        try {
            JSONArray jsonArrayProvice = new JSONArray(result);
            for (int i = 0; i < jsonArrayProvice.length(); i++) {
                JSONObject jsonProvince = jsonArrayProvice.getJSONObject(i);
                String provinceName = jsonProvince.getString("region_name");
                String provinceId = jsonProvince.getString("region_id");
                JSONArray jsonArrayCity = jsonProvince.getJSONArray("son");
                for (int j = 0; j < jsonArrayCity.length(); j++) {
                    JSONObject jsonCity = jsonArrayCity.getJSONObject(j);
                    String cityName = jsonCity.getString("region_name");
                    String cityId = jsonCity.getString("region_id");
                    JSONArray jsonArrayDistrict = jsonCity.getJSONArray("son");
                    for (int k = 0; k < jsonArrayDistrict.length(); k++) {
                        JSONObject jsonDistrict = jsonArrayDistrict.getJSONObject(k);
                        PlaceInfo place = new PlaceInfo();
                        place.setProvinceName(provinceName);
                        place.setProvinceId(provinceId);
                        place.setCityId(cityId);
                        place.setCityName(cityName);
                        place.setDistrictName(jsonDistrict.getString("region_name"));
                        place.setDistrictId(jsonDistrict.getString("region_id"));
                        places.add(place);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return places;
    }

    /**
     * 获取省
     *
     * @param KEY
     */
    public void GetCity(final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String result = NetBaseUtils.getResponseForGet(CityNetConstant.NET_GET_CITY, mContext);
                LogUtils.i(TAG, "获取省--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }

    /**
     * 获取市、区接口
     *
     * @param region_id
     * @param KEY
     */
    public void GetTown(final String region_id, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("region_id", region_id));
                String result = NetBaseUtils.getResponseForPost(CityNetConstant.NET_GET_TOWN, params, mContext);
                LogUtils.i(TAG, "获取市区--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }

    /**
     * 调取用户基本资料所在省
     *
     * @param user
     * @param url
     * @param KEY
     */
    public void GetUserCity(final UserInfo user, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("MemberCityId", user.getUserCityId()));
                try {
                    String result = NetBaseUtils.getResponseForPost(CityNetConstant.NET_GET_USER_CITY, params, mContext);
                    JSONObject json = new JSONObject(result);
                    LogUtils.i(TAG, "调取用户基本资料所在省" + json);
                    // int state=json.getInt("state");
                    msg.what = KEY;
                    // msg.arg1=state;
                    user.setUserCity(json.getString("region_name"));
                    user.writeData(mContext);
                    // msg.obj = obj;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            };
        }.start();
    }

    public void GetUserCity(final String memberCity) {
        // TODO Auto-generated method stub
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("MemberCityId", memberCity));
                try {
                    String result = NetBaseUtils.getResponseForPost(CityNetConstant.NET_GET_USER_CITY, params, mContext);
                    JSONObject json = new JSONObject(result);
                    LogUtils.i(TAG, "调取用户基本资料所在省" + json);
                    // int state=json.getInt("state");
                    // msg.what = KEY;
                    // msg.arg1=state;
                    // msg.obj = obj;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            };
        }.start();
    }
}
