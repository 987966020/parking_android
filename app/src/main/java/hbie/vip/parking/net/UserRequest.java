package hbie.vip.parking.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hbie.vip.parking.bean.AppInfo;
import hbie.vip.parking.bean.UserInfo;
import hbie.vip.parking.net.request.constant.UserNetConstant;
import hbie.vip.parking.utils.LogUtils;
import hbie.vip.parking.utils.NetBaseUtils;

/**
 * Created by mac on 16/5/9.
 */
public class UserRequest {
    private Context mContext;
    private Handler handler;
    private UserInfo user;
    private AppInfo appInfo;
    private String TAG = "UserRequest.class";

    public UserRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;
        user = new UserInfo(mContext);
        user.readData(mContext);
        appInfo=new AppInfo();
        appInfo.readData(mContext);
    }

    public UserRequest(Context context) {
        super();
        this.mContext = context;
        user = new UserInfo();
        user.readData(mContext);
        appInfo=new AppInfo();
        appInfo.readData(mContext);
    }

    /**
     * 登录
     *
     * @param phone
     * @param pwd
     * @param KEY
     */
    public void Login(final String phone, final String pwd, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("password", pwd));
//                params.add(new BasicNameValuePair("DeviceToken", appInfo.getUmengDeivceToken()));
//                params.add(new BasicNameValuePair("DeviceState", appInfo.getDeviceState()));
                LogUtils.i("TuiSong", "device_token" + params.toString());
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_LOGIN, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     *
     * @param userIdTemp
     */
    public void obainUserData(final String userIdTemp, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("memberId", userIdTemp));
                try {
                    String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_DATA, params, mContext);
                    JSONObject json = new JSONObject(result);
                    LogUtils.i("UserData", "---->" + json.toString());
                    int state = json.getInt("state");
                    if (state == 1) {
                        user.setUserName(json.getString("MemberName"));
//                        user.setUserGender(json.getString("MemberSex"));
//                        user.setUserCityId(json.getString("MemberCityId"));
//                        user.setUserPosition(json.getString("MemberPostition"));
//                        user.setUserCompany(json.getString("MemberCompany"));
                        user.setUserImg(json.getString("MemberImg"));
                        user.writeData(mContext);
                    }
                    msg.what = KEY;
                    msg.obj = state;
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            };
        }.start();
    }
    /**
     * 判断是否已注册
     *
     * @param phone
     * @param KEY
     */
    public void isCanRegister(final String phone, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_IS_REG, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 验证码
     *
     * @param phone
     * @param KEY
     */
    public void getVerficationCode(final String phone, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_VERIFICATION_CODE, params, mContext);
                LogUtils.i("TempSDFG", "json-->" + result);
                LogUtils.i("TempSDFG", "json-->" + UserNetConstant.NET_USER_GET_VERIFICATION_CODE);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 忘记密码
     *
     * @param code,password
     * @param KEY
     */
    public void forgetpwd(final String code,final String password, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", user.getUserPhone()));
                params.add(new BasicNameValuePair("code", code));
                params.add(new BasicNameValuePair("password", password));
//                params.add(new BasicNameValuePair("MemberPass", user.getUserPassword()));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_FORHET, params, mContext);
                LogUtils.i(TAG, "修改用户的密码--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 修改用户的密码
     *
     * @param old_password,new_password
     * @param KEY
     */
    public void updateUserPassWordtwo(final String old_password,final String new_password, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", user.getUserPhone()));
                params.add(new BasicNameValuePair("new_password", new_password));
                params.add(new BasicNameValuePair("old_password", old_password));
//                params.add(new BasicNameValuePair("MemberPass", user.getUserPassword()));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_UPDATE_PASS_TWO, params, mContext);
                LogUtils.i(TAG, "修改用户的密码--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 注册
     * @param phone,pwd,code,gender,avatar,username,addr,cardid
     *
     * @param KEY
     */
    public void register(final String phone,final String pwd,final String code,final String gender,final String avatar,final String  username,final String addr,final String cardid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("password", pwd));
                params.add(new BasicNameValuePair("code",code));
                params.add(new BasicNameValuePair("sex", gender));
                params.add(new BasicNameValuePair("avatar", avatar));
                params.add(new BasicNameValuePair("name", username));
                params.add(new BasicNameValuePair("addr",addr));
                params.add(new BasicNameValuePair("cardid",cardid));
                LogUtils.i("TuiSong", "device_token" + params.toString());
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_REGISTER, params, mContext);
                LogUtils.i("UserRegister", "---->" + result);

                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 上传图片
     *
     * @param img
     * @param KEY
     */
    public void updateUserImg(final String img, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("upfile", img));
                String result = NetBaseUtils.getResponseForImg(UserNetConstant.NET_USER_UPLOAD_HEAD_IMG, params, mContext);
                LogUtils.i(TAG, "修改用户的头像--->" + img);
                LogUtils.i(TAG, "修改用户的头像--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 获取用户信息
     *
     * @param userid
     * @param KEY
     */
    public void GetUserInfo(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_INFO_BY_ID, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 更新用户头像
     *
     * @param userid
     * @param KEY
     */
    public void updateavatar(final String userid,final  String avatar, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("avatar", avatar));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_UPDATE_AVATAR, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 更新手机号码
     *
     * @param userid,phone,code,old_password
     * @param KEY
     */
    public void updateUserPhone(final String userid,final  String phone,final String code,final String old_password, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("code", code));
                params.add(new BasicNameValuePair("old_password", old_password));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_UPDATE_PHONE, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 更新个人资料
     *
     * @param userid
     * @param KEY
     */
    public void updateUserInfo(final String userid,final  String name,final String addr,final String sex,final String cardid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("addr", addr));
                params.add(new BasicNameValuePair("cardid", cardid));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("sex", sex));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_UPDATE_INFO, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 新增个人车辆
     *
     * @param userid
     * @param KEY
     */
    public void addCar(final String userid,final String carnumber,final String brand,final String cartype,final String enginenumber, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("carnumber", carnumber));
                params.add(new BasicNameValuePair("brand", brand));
                params.add(new BasicNameValuePair("cartype", cartype));
                params.add(new BasicNameValuePair("enginenumber", enginenumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_ADD_CAR, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    public void updateCar(final String userid,final  String carid,final String carnumber,final String brand,final String cartype,final String enginenumber, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("carid", carid));
                params.add(new BasicNameValuePair("carnumber", carnumber));
                params.add(new BasicNameValuePair("brand", brand));
                params.add(new BasicNameValuePair("cartype", cartype));
                params.add(new BasicNameValuePair("enginenumber", enginenumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_UPDATE_CAR, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 根据ID获取个人资料
     *
     * @param userid
     * @param KEY
     */
    public void getMemberByUserId(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_INFO_BY_ID, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 用户修改当前常用汽车
     *
     * @param userid,carnumber
     * @param KEY
     */
    public void updateMemberCurrentCar(final String userid,final String carnumber, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("carnumber", carnumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_UPDATE_CURRENT_CAR, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 用户获取车辆列表
     *
     * @param userid
     * @param KEY
     */
    public void GetCarList(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_CAR_LIST, params, mContext);
                Log.e("yyyy",result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 解绑当前车辆
     *
     * @param userid,carnumber
     * @param KEY
     */
    public void unBindCar(final String userid,final String carnumber, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("carnumber", carnumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_UN_BIND_CAR, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 获取车辆当前位置
     *
     * @param userid,carnumber
     * @param KEY
     */
    public void getCarLocation(final String userid,final String carnumber, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("carnumber", carnumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_CAR_LOCATION, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 获取车辆未缴费信息
     *
     * @param carnumber
     * @param KEY
     */
    public void getExpenseInfo(final String carnumber, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("carnumber", carnumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_CAR_EXPENSEINFO, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                LogUtils.i("1111111111111111111",result);
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 获取车辆未缴费信息
     *
     * @param carnumber
     * @param KEY
     */
    public void getallcarunpay(final String carnumber, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("carnumber", carnumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_CAR_EXPENSEALL, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                LogUtils.i("1111111111111111111",result);
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 获取用户的当前车辆信息
     *
     * @param userid,carnumber
     * @param KEY
     */
    public void getMemberCurrentCar(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_CURRENT_CAR, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 获取用户的所有未缴费信息
     *
     * @param userid,carnumber
     * @param KEY
     */
    public void getUnpayOrderListByOwner(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                LogUtils.i("userid", "-------->" + userid);
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_UNPAY_ORDER_BY_OWNER, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 获取用户的所有已缴费信息
     *
     * @param userid
     * @param KEY
     */
    public void getCarHistoryOrder(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_GET_CAR_HISTORY_ORDER, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtils.i("历时账单", "---------->" + result);
            };
        }.start();
    }
    /**
     * 发送短信
     *
     */
    public void SendMeaasge(final String phone,final String content,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("content", content));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.POST_SEND_MESSAGE, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                LogUtils.i("发送短信","---------->"+result);
            };
        }.start();
    }

    /**
     * 编辑银行卡信息
     *
     * @param  userid,cartnumber
     * @param  KEY
     */
    public void AddBankCard (final String userid,final String banktype,final String bankno,final String username,final String branch,final int KEY){
        new Thread(){
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid",userid));
                params.add(new BasicNameValuePair("banktype",banktype));
                params.add(new BasicNameValuePair("bankno",bankno));
                params.add(new BasicNameValuePair("username",username));
                params.add(new BasicNameValuePair("branch", branch));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_ADD_CART,params,mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            };
        }.start();
    }
    /**
     * 通过车牌号查询车辆是否是其他用户的当前驾驶车辆
     * @param userid,carnumber
     * @param KEY
     */
    public void CheckCurrentCar(final String userid,final String carnumber,final int KEY){
        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid",userid));
                params.add(new BasicNameValuePair("carnumber",carnumber));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_CHECK_CURRENTCar,params,mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();;
    }
    /**
     * 支付缴费信息
     * @param userid,orderno,type,money,status
     * @param KEY
     */
    public void PAYFREE(final String userid,final String orderno,final String type,final String money,final String status,final int KEY){
        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid",userid));
                params.add(new BasicNameValuePair("orderno",orderno));
                params.add(new BasicNameValuePair("type",type));
                params.add(new BasicNameValuePair("money",money));
                params.add(new BasicNameValuePair("status",status));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_CHECK_CPAYFREE,params,mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();;
    }
    /**
     * 支付缴费信息
     * @param versionid,type
     * @param KEY
     */
    public void NET_USER_CHECK_CHECKVERSON(final String versionid,final String type,final int KEY){
        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("versionid",versionid));
                params.add(new BasicNameValuePair("type",type));
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.NET_USER_CHECK_CHECKVERSON,params,mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();;
    }
    /**
     * 刷新
     * @param KEY
     */
    public void AlipayNotify(final int KEY){
        new Thread(){
            Message msg = Message.obtain();
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String result = NetBaseUtils.getResponseForPost(UserNetConstant.AlipayNotify,params,mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();;
    }
}
