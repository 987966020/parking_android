package hbie.vip.parking.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;

import hbie.vip.parking.bean.AppInfo;
import hbie.vip.parking.bean.PlaceInfo;
import hbie.vip.parking.db.PlaceManager;
import hbie.vip.parking.net.request.CityRequest;
import hbie.vip.parking.task.constants.MessageConstants;
import hbie.vip.parking.utils.NetBaseUtils;

/**
 * Created by mac on 16/6/1.
 */
public class FindAllCityTask extends AsyncTask<Integer, Integer, Boolean> {

    private Context mContext;
    private CityRequest netRequest;
    private ArrayList<PlaceInfo> places;
    private AppInfo appInfo;

    public FindAllCityTask(Context context) {
        this.mContext = context;
        appInfo = new AppInfo();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        boolean result = false;
        if (NetBaseUtils.isConnnected(mContext)) {
            try {
                PlaceManager manager = new PlaceManager();
                netRequest = new CityRequest(mContext);
                places = netRequest.doFindAllCity();
                result = manager.AddPlaceList(mContext, places);
                if (result) {
                    appInfo.setUpdateCity(false);
                } else {
                    appInfo.setUpdateCity(true);
                }
                appInfo.writeData(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {// ture为需要更新，false不需要更新
            Intent intent = new Intent(); // Itent就是我们要发送的内容
            intent.setAction(MessageConstants.REFRESH_CITY); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
            // 发送广播
            mContext.sendBroadcast(intent);
        }
    }
}
