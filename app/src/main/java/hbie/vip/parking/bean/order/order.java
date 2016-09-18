package hbie.vip.parking.bean.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 16/5/17.
 */
public class order implements Serializable {


    /**
     * status : success
     * data : [{"order_no":"20160823140321103","car_number":"闽EL3963","brand":"大众","car_type":"1","park":"软件园一号停车场","position":"","owner_name":"王欣","pay_user_name":"王欣","park_code":"01","owner_id":1464194421822,"park_date":1471795200000,"start_time":1471834800000,"end_time":1471838400000,"start_time_str":"2016-08-22 11:00:00","end_time_str":"2016-08-22 12:00:00","price":2,"time":"1小时1分钟","pay_user_id":1464194421822,"park_time":61,"pay_type":0,"status":1,"pay_status":1,"money":10}]
     */

    private String status;
    /**
     * order_no : 20160823140321103
     * car_number : 闽EL3963
     * brand : 大众
     * car_type : 1
     * park : 软件园一号停车场
     * position :
     * owner_name : 王欣
     * pay_user_name : 王欣
     * park_code : 01
     * owner_id : 1464194421822
     * park_date : 1471795200000
     * start_time : 1471834800000
     * end_time : 1471838400000
     * start_time_str : 2016-08-22 11:00:00
     * end_time_str : 2016-08-22 12:00:00
     * price : 2
     * time : 1小时1分钟
     * pay_user_id : 1464194421822
     * park_time : 61
     * pay_type : 0
     * status : 1
     * pay_status : 1
     * money : 10
     */

    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private String order_no;
        private String car_number;
        private String brand;
        private String car_type;
        private String park;
        private String position;
        private String owner_name;
        private String pay_user_name;
        private String park_code;
        private String owner_id;
        private String park_date;
        private String start_time;
        private String end_time;
        private String start_time_str;
        private String end_time_str;
        private String price;
        private String time;
        private String pay_user_id;
        private String park_time;
        private String pay_type;
        private String status;
        private String pay_status;
        private String money;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public String getPark() {
            return park;
        }

        public void setPark(String park) {
            this.park = park;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getOwner_name() {
            return owner_name;
        }

        public void setOwner_name(String owner_name) {
            this.owner_name = owner_name;
        }

        public String getPay_user_name() {
            return pay_user_name;
        }

        public void setPay_user_name(String pay_user_name) {
            this.pay_user_name = pay_user_name;
        }

        public String getPark_code() {
            return park_code;
        }

        public void setPark_code(String park_code) {
            this.park_code = park_code;
        }

        public String getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(String owner_id) {
            this.owner_id = owner_id;
        }

        public String getPark_date() {
            return park_date;
        }

        public void setPark_date(String park_date) {
            this.park_date = park_date;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getStart_time_str() {
            return start_time_str;
        }

        public void setStart_time_str(String start_time_str) {
            this.start_time_str = start_time_str;
        }

        public String getEnd_time_str() {
            return end_time_str;
        }

        public void setEnd_time_str(String end_time_str) {
            this.end_time_str = end_time_str;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPay_user_id() {
            return pay_user_id;
        }

        public void setPay_user_id(String pay_user_id) {
            this.pay_user_id = pay_user_id;
        }

        public String getPark_time() {
            return park_time;
        }

        public void setPark_time(String park_time) {
            this.park_time = park_time;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}

