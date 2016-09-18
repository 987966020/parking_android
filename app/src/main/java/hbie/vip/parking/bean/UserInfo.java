package hbie.vip.parking.bean;

import android.content.Context;

import java.io.Serializable;

import hbie.vip.parking.db.sp.UserSp;
import hbie.vip.parking.utils.LogUtils;

/**
 * Created by mac on 16/5/9.
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;// 用户ID
    private String userIdTemp;
    private String userPassword;// 用户密码
    private String userImg;// 用户头像
    private String userPhone;// 用户手机号
    private String userCode;// 验证码
    private String userName;// 姓名
    private String userSex;// 性别
    private String userAddr;//地址
    private String userCardid;//身份证号
    private String userBranch;//银行卡号
    private String userCityId;// 城市ID
    private String userCity;// 城市名
    private String userBanktype;
    private String userBankno;
    private String Currentcar;
    private String userCurrentCarId;
    private String userCurrentCarBrand;
    private String userCurrentCarType;
    private String userCurrentCarEnginenumber;

    public UserInfo() {

    }

    public UserInfo(Context context) {
        readData(context);
    }

    public void readData(Context context) {
        UserSp sp = new UserSp(context);
        sp.read(this);
    }

    public void writeData(Context context) {
        UserSp sp = new UserSp(context);
        sp.write(this);
    }


    public void clearData(Context context) {
        UserSp sp = new UserSp(context);
        sp.clear();
    }

    public void clearCar(Context context){//单独清除车辆信息的方法。在其他页面调用即可
        UserSp sp = new UserSp(context);
        sp.clearCar();
    }
    public void clearDataExceptPhone(Context mContext) {
        // TODO Auto-generated method stub
        UserSp sp = new UserSp(mContext);
        UserInfo user = new UserInfo();
        user.setUserPhone(sp.read().getUserPhone());
        clearData(mContext);
        user.writeData(mContext);
    }
    public void clearPhone(Context mContext){
        UserSp sp = new UserSp(mContext);
        sp.clearPhone();
    }

    /**
     * 已登录过得，自动进入
     *
     * @return
     */
    public boolean isLogined() {
        if (this.getUserId().equals("")) {
            return false;
        }
        return true;
    }
    public String getUserId() {
        if (userId == null) {
            return "";
        } else if (userId.equals("null")) {
            return "";
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIdTemp() {
        if (userIdTemp == null) {
            return "";
        } else if (userIdTemp.equals("null")) {
            return "";
        }
        return userIdTemp;
    }

    public String getUserCardid() {
        if(userCardid==null){
            return "";
        }else if(userCardid.equals("null")){
            return "";
        }
        return userCardid;
    }
    public String getUserCityId() {
        if (userCityId == null) {
            return "2";
        } else if (userCityId.equals("null")) {
            return "2";
        }
        return userCityId;
    }

    public void setUserCityId(String userCityId) {
        this.userCityId = userCityId;
    }
    public String getUserCity() {
        if (userCity == null) {
            return "";
        } else if (userCity.equals("null")) {
            return "";
        }
        return userCity;
    }
    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }


    public void setUserCardid(String userCardid) {
        this.userCardid = userCardid;
    }

    public void setUserIdTemp(String userIdTemp) {
        this.userIdTemp = userIdTemp;
    }

    public String getUserPhone() {
        if (userPhone == null) {
            return "";
        } else if (userPhone.equals("null")) {
            return "";
        }
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCode() {
        if (userCode == null) {
            return "";
        } else if (userCode.equals("null")) {
            return "";
        }
        return userCode;
    }
    public void setuserAddr(String userAddr) {
        this.userAddr = userAddr;
        LogUtils.i("UserData", "---->setUserPosition-------11111111-" + userAddr);
    }
    public String getUserAddr(){
        if (userAddr == null) {
            return "";
        } else if (userAddr.equals("null")) {
            return "";
        }
        return userAddr;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        if (userName == null) {
            return "";
        } else if (userName.equals("null")) {
            return "";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        if (userPassword == null) {
            return "";
        } else if (userPassword.equals("null")) {
            return "";
        }
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserImg() {
        if (userImg == null) {
            return "";
        } else if (userImg.equals("null")) {
            return "";
        }
        return userImg;
    }
    public String getUserSex() {
        if (userSex == null) {
            return "1";
        } else if (userSex.equals("null")) {
            return "1";
        }
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setCurrentcar(String currentcar) {
        Currentcar = currentcar;
    }

    public String getCurrentcar() {
        if (Currentcar == null) {
            return "暂无";
        } else if (Currentcar.equals("null")) {
            return "暂无";
        }
        return Currentcar;
    }

    public void setUserCurrentCarId(String userCurrentCarId) {
        this.userCurrentCarId = userCurrentCarId;
    }

    public String getUserCurrentCarId() {
        if(userCurrentCarId==null){
            return "";
        }else if(userCurrentCarId.equals("null")){
            return "";
        }
        return userCurrentCarId;
    }

    public String getUserCurrentCarBrand() {
        if(userCurrentCarBrand==null){
            return "";
        }else if(userCurrentCarBrand.equals("null")){
            return "";
        }
        return userCurrentCarBrand;
    }

    public void setUserCurrentCarBrand(String userCurrentCarBrand) {
        this.userCurrentCarBrand = userCurrentCarBrand;
    }

    public String getUserCurrentCarEnginenumber() {
        if(userCurrentCarEnginenumber==null){
            return "";
        }else if(userCurrentCarEnginenumber.equals("null")){
            return "";
        }
        return userCurrentCarEnginenumber;
    }

    public void setUserCurrentCarEnginenumber(String userCurrentCarEnginenumber) {
        this.userCurrentCarEnginenumber = userCurrentCarEnginenumber;
    }

    public void setUserCurrentCarType(String userCurrentCarType) {
        this.userCurrentCarType = userCurrentCarType;
    }

    public String getUserCurrentCarType() {
        if(userCurrentCarType==null){
            return "";
        }else if(userCurrentCarType.equals("null")){
            return "";
        }
        return userCurrentCarType;
    }

    public void setUserBankno(String userBankno) {
        this.userBankno = userBankno;
    }

    public String getUserBankno() {
        if (userBankno == null) {
            return "";
        } else if (userBankno.equals("null")) {
            return "";
        }
        return userBankno;
    }

    public void setUserBanktype(String userBanktype) {
        this.userBanktype = userBanktype;
    }

    public String getUserBanktype() {
        if (userBanktype == null) {
            return "";
        } else if (userBanktype.equals("null")) {
            return "";
        }
        return userBanktype;
    }

    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }

    public String getUserBranch() {
        if (userBranch == null) {
            return "";
        } else if (userBranch.equals("null")) {
            return "";
        }
        return userBranch;
    }

    @Override
    public String toString() {
        return "UserInfo [userId=" + userId + ", userIdTemp=" + userIdTemp + ", userPassword=" + userPassword + ", userImg=" + userImg + ", userPhone=" + userPhone + ", userCode=" + userCode
                + ", userName=" + userName + ",userAddr="+userAddr+ ",userCardid="+userCardid+",userBranch="+userBranch+",userBanktype="+userBanktype+",userBankno="+userBankno+",Currentcar="+Currentcar+"]";
    }


}
