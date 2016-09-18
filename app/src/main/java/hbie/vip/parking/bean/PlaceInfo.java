package hbie.vip.parking.bean;

import java.io.Serializable;

public class PlaceInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String provinceName;
    private String provinceId;
    private String cityName;
    private String cityId;
    private String districtName;
    private String districtId;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

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

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "PlaceInfo [provinceName=" + provinceName + ", provinceId=" + provinceId + ", cityName=" + cityName + ", cityId=" + cityId + ", districtName=" + districtName + ", districtId="
                + districtId + "]";
    }
}
