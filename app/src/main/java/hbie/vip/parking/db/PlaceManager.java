package hbie.vip.parking.db;


import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import hbie.vip.parking.activity.wheel.CityModel;
import hbie.vip.parking.activity.wheel.DistrictModel;
import hbie.vip.parking.activity.wheel.ProvinceModel;
import hbie.vip.parking.bean.PlaceInfo;

public class PlaceManager extends DBManager {
    private static PlaceManager instance = null;

    // 单例模式
    public static PlaceManager getInstance() {
        if (null == instance) {
            synchronized (PlaceManager.class) {
                if (instance == null) {
                    instance = new PlaceManager();
                }
            }
        }
        return instance;
    }

    /**
     * 把获取到的place添加到本地数据库中，所有属性
     *
     * @param placeList
     * @return
     */
    public boolean AddPlaceList(Context context, ArrayList<PlaceInfo> placeList) {
        ArrayList<ContentValues> valuesList = new ArrayList<ContentValues>();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        for (int i = 0; i < placeList.size(); i++) {
            ContentValues initialValues = new ContentValues();
            initialValues = CreateValues(placeList.get(i));
            valuesList.add(initialValues);
        }
        return placeDaoInface.addPlaceList(valuesList);
    }

    public ArrayList<ProvinceModel> GetAllDates(Context context) {
        ArrayList<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        provinceList = placeDaoInface.getProvinceModelList();
        return provinceList;
    }

    // 根据districtId,获取place
    public PlaceInfo ReadPlaceByDistrictId(Context context, String districtId) {
        PlaceInfo place = new PlaceInfo();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        place = placeDaoInface.getPlaceByDistrictId(districtId);
        return place;
    }

    // 根据provinceName,获取provinceModel
    public ProvinceModel ReadProvinceByName(Context context, String provinceName) {
        ProvinceModel province = new ProvinceModel();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        province = placeDaoInface.getPlaceByProvinceName(provinceName);
        return province;
    }

    // 根据cityName,获取cityModel
    public CityModel ReadCityModelByName(Context context, String cityName) {
        CityModel city = new CityModel();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        city = placeDaoInface.getPlaceByCityName(cityName);
        return city;
    }

    // 根据districtName,获取districtModel
    public DistrictModel ReadDistrictModelByName(Context context, String districtName) {
        DistrictModel district = new DistrictModel();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        district = placeDaoInface.getPlaceByDistrictName(districtName);
        return district;
    }

    // 获取placeList 省
    public ArrayList<PlaceInfo> ReadProvinceList(Context context) {
        ArrayList<PlaceInfo> provinceList = new ArrayList<PlaceInfo>();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        provinceList = placeDaoInface.getProvinceList();
        return provinceList;
    }

    // 获取cityList 市
    public ArrayList<PlaceInfo> ReadCityList(Context context, String provinceId) {
        ArrayList<PlaceInfo> provinceList = new ArrayList<PlaceInfo>();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        provinceList = placeDaoInface.getCityListByProvinceId(provinceId);
        return provinceList;
    }

    // 获取DistrictList 县区
    public ArrayList<PlaceInfo> ReadProvinceList(Context context, String cityId) {
        ArrayList<PlaceInfo> provinceList = new ArrayList<PlaceInfo>();
        PlaceDaoInface placeDaoInface = new PlaceDaoImpl(context);
        provinceList = placeDaoInface.getDistrictListByCityId(cityId);
        return provinceList;
    }

    public static ContentValues CreateValues(PlaceInfo place) {
        ContentValues values = new ContentValues();
        values.put(Const_DB.DATABASE_TABLE_PLACE_PROVINCE_ID, place.getProvinceId());
        values.put(Const_DB.DATABASE_TABLE_PLACE_PROVINCE_NAME, place.getProvinceName());
        values.put(Const_DB.DATABASE_TABLE_PLACE_CITY_ID, place.getCityId());
        values.put(Const_DB.DATABASE_TABLE_PLACE_CITY_NAME, place.getCityName());
        values.put(Const_DB.DATABASE_TABLE_PLACE_DISTRICT_ID, place.getDistrictId());
        values.put(Const_DB.DATABASE_TABLE_PLACE_DISTRICT_NAME, place.getDistrictName());
        return values;
    }

}
