package hbie.vip.parking.net.request.constant;

/**
 * Created by mac on 16/6/1.
 */

public interface CityNetConstant extends NetBaseConstant {
    /**
     * 获取所有城市 /Basic/City
     */
    public final static String NET_GET_ALL_CITY = NET_OLDBASE_HOST + "/Basic/City";
    /**
     * 获取省接口 /Basic/GetCity
     */
    public final static String NET_GET_CITY = NET_OLDBASE_HOST + "/Basic/GetCity";
    /**
     * 获取市、区接口 /Basic/GetTown
     */
    public final static String NET_GET_TOWN = NET_OLDBASE_HOST + "/Basic/GetTown";
    /**
     * 调取用户基本资料所在省 /Basic/GetUserCity
     */
    public final static String NET_GET_USER_CITY = NET_OLDBASE_HOST + "/Basic/GetUserCity";
    /**
     * 锁定所在位置 /Basic/GetCityId
     */
    public final static String NET_GET_CITY_ID = NET_OLDBASE_HOST + "/Basic/GetCityId";
    /**
     * 获取找专家城市列表/Experts/Index/GetServiceCity
     */
    public final static String NET_GET_SERVICE_CITY = NET_OLDBASE_HOST + "/Experts/Index/GetServiceCity";

    /**
     * 获取资讯收藏列表 /News/GetNewCollect
     */
    public final static String NET_GETNEWCOLLECT = NET_OLDBASE_HOST + "/News/GetNewCollect";

    /**
     * 取消资讯收藏/News/DelCollect
     */
    public final static String NET_DELCOLLECT=NET_OLDBASE_HOST+"/News/DelCollect";
}
