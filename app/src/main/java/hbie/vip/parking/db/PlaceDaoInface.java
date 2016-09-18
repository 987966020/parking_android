package hbie.vip.parking.db;

import android.content.ContentValues;

import java.util.ArrayList;

import hbie.vip.parking.activity.wheel.CityModel;
import hbie.vip.parking.activity.wheel.DistrictModel;
import hbie.vip.parking.activity.wheel.ProvinceModel;
import hbie.vip.parking.bean.PlaceInfo;

/**
 * @author 帮手数据库服务接口
 */
public interface PlaceDaoInface {
    /**
     * 添加placeList到数据库
     *
     * @param list
     * @return
     * */
    public boolean addPlaceList(ArrayList<ContentValues> list);

    /**
     * 获取placeList 省列表
     *
     * @return
     */
    public ArrayList<PlaceInfo> getProvinceList();

    /**
     * 获取placeList 省列表
     *
     * @return
     */
    public ArrayList<ProvinceModel> getProvinceModelList();

    /**
     * 获取placeList 市列表
     *
     * @param provinceId
     * @return
     */
    public ArrayList<PlaceInfo> getCityListByProvinceId(String provinceId);

    /**
     * 获取placeList 市列表
     *
     * @param provinceId
     * @return
     */
    public ArrayList<CityModel> getCityModelListByProvinceId(String provinceId);

    /**
     * 获取placeList 县区列表
     *
     * @param cityId
     * @return
     */
    public ArrayList<PlaceInfo> getDistrictListByCityId(String cityId);

    /**
     * 获取placeList 县区列表
     *
     * @param cityId
     * @return
     */
    public ArrayList<DistrictModel> getDistrictModelListByCityId(String cityId);

    /**
     * 获取place
     *
     * @param districtId
     * @return
     */
    public PlaceInfo getPlaceByDistrictId(String districtId);

    /**
     * 获取place
     *
     * @param provinceName
     * @return
     */
    public ProvinceModel getPlaceByProvinceName(String provinceName);

    /**
     * 获取place
     *
     * @param cityName
     * @return
     */
    public CityModel getPlaceByCityName(String cityName);

    /**
     * 获取place
     *
     * @param districtName
     * @return
     */
    public DistrictModel getPlaceByDistrictName(String districtName);

}

