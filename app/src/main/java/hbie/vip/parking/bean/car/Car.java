package hbie.vip.parking.bean.car;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 16/5/16.
 */
public class Car implements Serializable {
    /**
     * status : success
     * data : [{{"car_number":"\u4eacB12335","brand":"\u5954\u9a70","car_type":"\u5c0f\u578b\u8f66",
     * "engine_number":"12345633","id":1463274439568,"owner_id":1462436523046,"opt_time":1463274439568,
     * "is_current_car":0}]
     */

    private String status;
    /**
     * car_number : 16
     * brand : 597
     * car_type : AsfasdfasdfA
     * engine_number : Asdfasfdasdfasdfa
     * id : 5971813.png,5971639.png,5971913.png
     * owner_id : 1462021714
     * opt_time : 潘宁
     * is_current_car : 1
     */

    private List<CarData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CarData> getData() {
        return data;
    }

    public void setData(List<CarData> data) {
        this.data = data;
    }


    public static class CarData implements Serializable {
        private String id;
        private String carnumber;
        private String brand;
        private String cartype;
        private String enginenumber;
        private String ownerid;
        private String opttime;
        private String iscurrentcar;
        private String isCar;

        public String getIsCar() {
            return isCar;
        }

        public void setIsCar(String isCar) {
            this.isCar = isCar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCarnumber() {
            return carnumber;
        }

        public void setCarnumber(String carnumber) {
            this.carnumber = carnumber;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCartype() {
            return cartype;
        }

        public void setCartype(String cartype) {
            this.cartype = cartype;
        }

        public String getEnginenumber() {
            return enginenumber;
        }

        public void setEnginenumber(String enginenumber) {
            this.enginenumber = enginenumber;
        }

        public String getIscurrentcar() {
            return iscurrentcar;
        }

        public void setIscurrentcar(String iscurrentcar) {
            this.iscurrentcar = iscurrentcar;
        }

        public String getOpttime() {
            return opttime;
        }

        public void setOpttime(String opttime) {
            this.opttime = opttime;
        }

        public String getOwnerid() {
            return ownerid;
        }

        public void setOwnerid(String ownerid) {
            this.ownerid = ownerid;
        }
        @Override
        public String toString() {
            return "CarData{" +
                    "id='" + id + '\'' +
                    ", ownerid='" + ownerid + '\'' +
                    ", carnumber='" + carnumber + '\'' +
                    ", cartype='" + cartype + '\'' +
                    ", brand='" + brand + '\'' +
                    ", enginenumber=" + enginenumber +
                    ", opttime='" + opttime + '\'' +
                    '}';
        }
    }
    @Override
    public String toString() {
        return "Car{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
