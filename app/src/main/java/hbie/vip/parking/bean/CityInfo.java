package hbie.vip.parking.bean;

import java.io.Serializable;

/**
 * Created by mac on 16/6/2.
 */

public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String cityName;
    private String cityId;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "CityInfo [cityName=" + cityName + ", cityId=" + cityId + "]";
    }

}
