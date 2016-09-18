package hbie.vip.parking.activity.wheel;


import android.app.Activity;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import hbie.vip.parking.AppApplication;
import hbie.vip.parking.db.PlaceManager;
import hbie.vip.parking.task.tool.ThreadPoolUtils_db;
import hbie.vip.parking.utils.LogUtils;

public class WheelBaseActivity extends Activity {

    private static final String TAG = "WheelBaseActivity";
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - id
     */
    protected Map<String, String> mIdDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的id
     */
    protected String mCurrentId = "";
    /**
     * 获取省市区的db数据
     */

    protected void initProvinceDatas() {
        LogUtils.i(TAG,"initProvinceDatas------");
        if (AppApplication.provinceList != null && AppApplication.provinceList.size() > 0) {// 有值
            LogUtils.i("City", "滚轮数据" + AppApplication.provinceList.size());
            Data(AppApplication.provinceList);
        } else {
            ReadProvinceFromDBTask readProvinceFromDBTask = new ReadProvinceFromDBTask();
            try {
                AppApplication.provinceList = readProvinceFromDBTask.executeOnExecutor(ThreadPoolUtils_db.threadPool).get();
                LogUtils.i("City", "滚轮数据获取" + AppApplication.provinceList);
                Data(AppApplication.provinceList);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class ReadProvinceFromDBTask extends AsyncTask<String, Integer, ArrayList<ProvinceModel>> {

        @Override
        protected ArrayList<ProvinceModel> doInBackground(String... params) {
            ArrayList<ProvinceModel> provinces = new ArrayList<ProvinceModel>();
            PlaceManager placeManager = new PlaceManager();
            provinces = placeManager.GetAllDates(getApplicationContext());
            return provinces;
        }

        @Override
        protected void onPostExecute(ArrayList<ProvinceModel> result) {
            super.onPostExecute(result);
        }
    }

    protected void Data(ArrayList<ProvinceModel> provinceList) {
        if (provinceList != null && !provinceList.isEmpty()) {
            mCurrentProviceName = provinceList.get(0).getName();
            List<CityModel> cityList = provinceList.get(0).getCityList();
            if (cityList != null && !cityList.isEmpty()) {
                mCurrentCityName = cityList.get(0).getName();
                List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                mCurrentDistrictName = districtList.get(0).getName();
                mCurrentId = districtList.get(0).getId();
            }
        }
        // */
        mProvinceDatas = new String[provinceList.size()];
        for (int i = 0; i < provinceList.size(); i++) {
            // 遍历所有省的数据
            mProvinceDatas[i] = provinceList.get(i).getName();
            List<CityModel> cityList = provinceList.get(i).getCityList();
            String[] cityNames = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                // 遍历省下面的所有市的数据
                cityNames[j] = cityList.get(j).getName();
                List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                String[] distrinctNameArray = new String[districtList.size()];
                DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                for (int k = 0; k < districtList.size(); k++) {
                    // 遍历市下面所有区/县的数据
                    DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getId());
                    // 区/县对于的邮编，保存到mZipcodeDatasMap
                    mIdDatasMap.put(districtList.get(k).getName(), districtList.get(k).getId());
                    distrinctArray[k] = districtModel;
                    distrinctNameArray[k] = districtModel.getName();
                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
            }
            // 省-市的数据，保存到mCitisDatasMap
            mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
        }
    }
}
