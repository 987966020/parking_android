package hbie.vip.parking.net.request.constant;

/**
 * Created by mac on 16/5/9.
 */
public interface UserNetConstant extends NetBaseConstant {
    /**
     * 获取临时MasterKey键值 /Basic/SetMasterKey
     */
    public final static String NET_GET_MASTER_KAY = NET_BASE_HOST + "/Basic/SetMasterKey";
    /**
     * 用户注册 /User/UserSet/CostalRegister
     */
    public final static String NET_USER_REGISTER = NET_BASE_HOST + "/index.php?g=apps&m=index&a=AppRegister";
    /**
     * 用户注册 /User/UserSet/CostalRegister
     */
    public final static String NET_USER_FORHET = NET_BASE_HOST + "/index.php?g=apps&m=index&a=forgetpwd";
    /**
     * 用户登陆 /User/UserSet/CostalLogin
     */
    public final static String NET_USER_LOGIN = NET_BASE_HOST + "/index.php?g=apps&m=index&a=applogin";
    /**
     * 判断用户是否已注册 /User/UserSet/IsRegPhone
     */
    public final static String NET_USER_IS_REG = NET_BASE_HOST + "/index.php?g=apps&m=index&a=isregphone";
    /**
     * 获取验证码 /Basic/GetSms
     */
    public final static String NET_USER_GET_VERIFICATION_CODE = NET_BASE_HOST + "/index.php?g=apps&m=index&a=SendMobileCode";
    /**
     * 获取用户信息 /User/UserSet/getMemberByUserId
     */
    public final static String NET_USER_GET_DATA = NET_BASE_HOST + "/User/UserSet/getMemberByUserId";
    /**
     * 修改用户个人头像接口 /User/UserSet/updateavatar
     */
    public final static String NET_USER_UPDATE_AVATAR = NET_BASE_HOST + "/index.php?g=apps&m=index&a=updateavatar";
    /**
     * 修改用户个人资料接口 /User/UserSet/updateavatar
     */
    public final static String NET_USER_UPDATE_INFO = NET_BASE_HOST + "/index.php?g=apps&m=index&a=updatemember";
    /**
     * 用户修改当前常用汽车 /User/UserSet/updateavatar
     */
    public final static String NET_USER_UPDATE_CURRENT_CAR = NET_BASE_HOST + "/index.php?g=apps&m=index&a=updateMemberCurrentCar";
    /**
     * 解绑车辆
     */
    public final static String NET_USER_UN_BIND_CAR = NET_BASE_HOST + "/index.php?g=apps&m=index&a=unBindCar";
    /**
     * 获取车辆当前位置
     */
    public final static String NET_USER_GET_CAR_LOCATION = NET_BASE_HOST + "/index.php?g=apps&m=index&a=getcarlocation";
    /**
     * 获取车辆未缴费信息
     */
    public final static String NET_USER_GET_CAR_EXPENSEINFO = NET_BASE_HOST + "/index.php?g=apps&m=index&a=getExpenseInfo";
    /**http://parking.xiaocool.net/index.php?g=apps&m=index&a=getAllOrderByCarNumber&carnumber=闽EL3963
     * /**
     * 获取车辆未缴费信息
     */
    public final static String NET_USER_GET_CAR_EXPENSEALL= NET_BASE_HOST + "/index.php?g=apps&m=index&a=getAllOrderByCarNumber";

    /**
     * 获取用户当前车辆信息
     */
    public final static String NET_USER_GET_CURRENT_CAR = NET_BASE_HOST + "/index.php?g=apps&m=index&a=getMemberCurrentCar";
    /**
     * 发送短信
     */
    public final static String POST_SEND_MESSAGE = NET_BASE_HOST + "/index.php?g=apps&m=index&a=Send";
    /**
     * 获取用户所有未缴费信息
     */
    public final static String NET_USER_GET_UNPAY_ORDER_BY_OWNER = NET_BASE_HOST + "/index.php?g=apps&m=index&a=getUnpayOrderListByOwner";
    /**
     * 获取用户所有历史信息
     */
//    public final static String NET_USER_GET_CAR_HISTORY_ORDER = NET_BASE_HOST + "/index.php?g=apps&m=index&a=getCarHistoryOrder";
    public final static String NET_USER_GET_CAR_HISTORY_ORDER = "http://parking.xiaocool.net/index.php?g=apps&m=index&a=getCarHistoryOrderByPayUserId";

    /**
     * 获取用户车辆列表
     */
    public final static String NET_USER_GET_CAR_LIST = NET_BASE_HOST + "/index.php?g=apps&m=index&a=GetCarList";
    /**
     * 根据个人编号获取个人资料接口
     *
     */
    public final static String NET_USER_GET_INFO_BY_ID = NET_BASE_HOST+"/index.php?g=apps&m=index&a=getMemberByUserId";
    /**
     * 注册个人车辆接口
     */
    public final static String NET_USER_ADD_CAR = NET_BASE_HOST + "/index.php?g=apps&m=index&a=InsertCarInfo";
    public final static String NET_USER_UPDATE_CAR = NET_BASE_HOST + "/index.php?g=apps&m=index&a=UpdateCarInfo";
    /**
     * 修改用户个人资料接口[密码]
     */
    public final static String NET_USER_UPDATE_PASS = NET_BASE_HOST + "/index.php?g=apps&m=index&a=UpdatePass";
    public final static String NET_USER_UPDATE_PASS_TWO = NET_BASE_HOST + "/index.php?g=apps&m=index&a=changePasswordByPhone";
    /**
     * 修改用户个人资料接口[姓名]
     */
    public final static String NET_USER_UPDATE_NAME = NET_BASE_HOST + "/User/UserSet/UpdateUserName";
    /**
     * 修改用户个人资料接口[职位]
     */
    public final static String NET_USER_UPDATE_POSTION = NET_BASE_HOST + "/User/UserSet/UpdateUserPostition";
    /**
     * 修改用户个人资料接口[性别]
     */
    public final static String NET_USER_UPDATE_SEX = NET_BASE_HOST + "/User/UserSet/UpdateUserSex";
    /**
     * 修改用户个人资料接口[邮箱] /User/UserSet/UpdateUserEmail
     */
    public final static String NET_USER_UPDATE_EMAIL = NET_BASE_HOST + "/User/UserSet/UpdateUserEmail";
    /**
     * 修改用户个人资料接口[所在城市] /User/UserSet/UpdateUserCityId
     */
    public final static String NET_USER_UPDATE_CITY = NET_BASE_HOST + "/User/UserSet/UpdateUserCityId";
    /**
     * 修改用户个人资料接口[公司名称] /User/UserSet/UpdateUserCompany
     */
    public final static String NET_USER_UPDATE_COMPANY = NET_BASE_HOST + "/User/UserSet/UpdateUserCompany";
    /**
     * 修改用户个人资料接口[注册手机号] /User/UserSet/UpdatePhone
     */
    public final static String NET_USER_UPDATE_PHONE = NET_BASE_HOST + "/index.php?g=apps&m=index&a=updatephone";
    /**
     * 头像上传 /User/UserSet/UploadMemberImg
     */
    public final static String NET_USER_UPLOAD_HEAD_IMG = NET_BASE_HOST +"/index.php?g=apps&m=index&a=WriteMicroblog_upload";
    /**
     * 添加工作经验 /User/UserSet/AddUndergo
     */
    public final static String NET_USER_ADD_WORKING = NET_BASE_HOST + "/User/UserSet/AddUndergo";
    /**
     * 修改工作经验 /User/UserSet/UpdateUndergo
     */
    public final static String NET_USER_UPDATE_WORKING = NET_BASE_HOST + "/User/UserSet/UpdateUndergo";
    /**
     * 查询工作经验 /User/UserSet/GetUndergo
     */
    public final static String NET_USER_GET_WORKING = NET_BASE_HOST + "/User/UserSet/GetUndergo";
    /**
     * 附近的人 /User/UserSet/GetNearMember
     */
    public final static String NET_USER_GET_NEARMEMBER = NET_BASE_HOST + "/User/UserSet/GetNearMember";
    /**
     * 附近的人申请好友 /User/UserSet/ApplyFriends
     */
    public final static String NET_USER_APPLY_FRIENDS = NET_BASE_HOST + "/User/UserSet/ApplyFriends";
    /**
     * 附近的人申请好友 /User/UserSet/ApplyFriends
     */
    public final static String NET_USER_GET_WORKLIST_IMG = NET_BASE_HOST + "/User/UserSet/GetMemberWorkLast";
    /**
     * 提交意见反馈 /User/UsetSet/SubmitIdea
     */
    public final static String NET_USER_SUBMIT_IDEA = NET_BASE_HOST + "/User/UserSet/SubmitIdea";
    /**
     * 获取系统消息 /User/UserSet/SystemMessage
     */
    public final static String NET_USER_GET_SYSTEM_MESSAGE = NET_BASE_HOST + "/User/UserSet/SystemMessage";
    /**
     * 系统消息已读 /User/UserSet/ReadMessage
     */
    public final static String NET_USER_GET_SYSTEM_MESSAGE_READ = NET_BASE_HOST + "/User/UserSet/SystemMessage";
    /**
     * 系统消息未读消息数量 /User/UserSet/UnReadMessageNum
     */
    public final static String NET_USER_GET_SYSTEM_MESSAGE_UN_READ_NUM = NET_BASE_HOST + "/User/UserSet/UnReadMessageNum";
    /**
     * 获取所有好友申请记录 /User/UserSet/GetApplyFirend
     */
    public final static String NET_USER_GET_APPLY_FIREND = NET_BASE_HOST + "/User/UserSet/GetApplyFirend";
    /**
     * 删除申请好友记录 /User/UserSet/DelApplyFirend
     */
    public final static String NET_USER_DEL_APPLY_FIREND = NET_BASE_HOST + "/User/UserSet/DelApplyFirend";
    /**
     * 删除申请好友记录 /User/UserSet/GetAddress
     */
    public final static String NET_USER_GET_ADDRESS = NET_BASE_HOST + "/User/UserSet/GetAddress";
    /**
     * 获取安卓版本号 Basic/android_version
     */
    public final static String NET_USER_GET_ANDROID_VERSION = NET_BASE_HOST + "/Basic/android_version";
    /**
     * 获取安卓最新安装包Basic/GetAndroid
     */
    public final static String NET_USER_GET_ANDROID = NET_BASE_HOST + "/Basic/GetAndroid";
    /**
     * 获取用户好友数量/User/UserSet/GetMemberFriendNum
     */
    public final static String NET_USER_GET_FERIEND_COUNT=NET_BASE_HOST + "/User/UserSet/GetMemberFriendNum";
    /**
     * 获取粉丝列表数量[大咖第一视角] /User/UserSet/GetFansNum
     */
    public final static String NET_USER_GET_FANS_COUNT=NET_BASE_HOST + "/User/UserSet/GetFansNum";
    /**
     * 获取全部已关注大咖列表数量/User/UserSet/GetAllFoucsKaNum
     */
    public final static String NET_USER_GET_FOCUSKA_COUNT=NET_BASE_HOST + "/User/UserSet/GetAllFoucsKaNum";
    /**
     * 编辑银行卡信息
     */
    public final static String NET_USER_ADD_CART=NET_BASE_HOST+"/index.php?g=apps&m=index&a=updatememberbankinfo";
    /**
     * 通过车牌号查询车辆是否是其他用户的当前驾驶车辆
     */
    public final static String NET_USER_CHECK_CURRENTCar=NET_USER_ADD_CART+"/index.php?g=apps&m=index&a=checkCurrentCar";
    /**
     * 通过车牌号查询车辆是否是其他用户的当前驾驶车辆
     */
    public final static String NET_USER_CHECK_CPAYFREE=NET_USER_ADD_CART+"/index.php?g=apps&m=index&a=payFees";
    //检测版本
    public final static String NET_USER_CHECK_CHECKVERSON=NET_USER_ADD_CART+"/index.php?g=apps&m=index&a=CheckVersion";
    //检测版本
    public final static String AlipayNotify=NET_USER_ADD_CART+"/index.php?g=apps&m=index&a=AlipayNotify";
}
