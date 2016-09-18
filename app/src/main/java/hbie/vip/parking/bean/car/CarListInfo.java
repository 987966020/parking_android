package hbie.vip.parking.bean.car;

import java.io.Serializable;

/**
 * 车辆
 */
public class CarListInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String carId;
    private String carNumber;
    private String carBrand;

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarId() {
//        if(carId)
        return carId;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarBrand() {
        return carBrand;
    }
}
